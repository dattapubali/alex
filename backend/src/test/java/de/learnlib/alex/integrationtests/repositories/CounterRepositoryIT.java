/*
 * Copyright 2015 - 2019 TU Dortmund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.learnlib.alex.integrationtests.repositories;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.data.entities.Counter;
import de.learnlib.alex.data.entities.Project;
import de.learnlib.alex.data.repositories.CounterRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class CounterRepositoryIT extends AbstractRepositoryIT {

    @Inject
    private CounterRepository counterRepository;

    private User user;

    private Project project;

    @Before
    public void before() {
        User user = createUser("alex@test.example");
        this.user = userRepository.save(user);

        Project project = createProject(user, "Test Project 1");
        this.project = projectRepository.save(project);
    }

    @Test
    public void shouldCreateAValidCounter() {
        Counter counter = createCounter(project, "TestCounter");
        counter = counterRepository.save(counter);

        assertNotNull(counter.getId());
        assertThat(counterRepository.count(), is(equalTo(1L)));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailToSaveACounterWithoutAProject() {
        Counter counter = createCounter(null, "TestCounter");
        counterRepository.save(counter); // should fail
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailToSaveACounterWithADuplicateNames() {
        Counter counter1 = createCounter(project, "TestCounter");
        counterRepository.save(counter1);
        Counter counter2 = createCounter(project, "TestCounter");

        counterRepository.save(counter2); // should fail
    }

    @Test
    public void shouldSaveCountersWithADuplicateNamesInDifferentProjects() {
        Project project2 = createProject(user, "Test Project 2");
        project2 = projectRepository.save(project2);

        Counter counter1 = createCounter(project, "TestCounter");
        counterRepository.save(counter1);
        Counter counter2 = createCounter(project2, "TestCounter");
        counter2 = counterRepository.save(counter2);

        assertNotNull(counter2.getId());
    }

    @Test
    public void shouldFetchAllCountersOfAProject() {
        Counter counter1 = createCounter(project, "TestCounter1");
        counter1 = counterRepository.save(counter1);
        Counter counter2 = createCounter(project, "TestCounter2");
        counter2 = counterRepository.save(counter2);

        List<Counter> counters = counterRepository.findAllByProject(project);

        assertThat(counters.size(), is(equalTo(2)));
        assertThat(counters, hasItem(equalTo(counter1)));
        assertThat(counters, hasItem(equalTo(counter2)));
    }

    @Test
    public void shouldFetchCountersOfAProjectByTheirNames() {
        Counter counter1 = createCounter(project, "TestCounter1");
        counter1 = counterRepository.save(counter1);
        Counter counter2 = createCounter(project, "TestCounter2");
        counter2 = counterRepository.save(counter2);
        Counter counter3 = createCounter(project, "TestCounter3");
        counter3 = counterRepository.save(counter3);

        List<Counter> counters = counterRepository.findAllByProjectAndNameIn(project,
                "TestCounter1", "TestCounter3");

        assertThat(counters.size(), is(equalTo(2)));
        assertThat(counters, hasItem(equalTo(counter1)));
        assertThat(counters, not(hasItem(equalTo(counter2))));
        assertThat(counters, hasItem(equalTo(counter3)));
    }

    @Test
    public void shouldFetchOntCountersOfAProjectByItsName() {
        Counter counter = createCounter(project, "TestCounter1");
        counter = counterRepository.save(counter);

        Counter counterFromDB = counterRepository.findByProjectAndName(project, "TestCounter1");

        assertThat(counterFromDB, is(equalTo(counter)));
    }

    @Test
    public void shouldDeleteACounter() {
        Counter counter = createCounter(project, "TestCounter");
        counter = counterRepository.save(counter);

        counterRepository.deleteById(counter.getId());

        assertThat(counterRepository.count(), is(equalTo(0L)));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldThrowAnExceptionWhenDeletingAnNonExistingCounter() {
        counterRepository.deleteById(-1L);
    }


    private Counter createCounter(Project project, String name) {
        Counter counter = new Counter();
        counter.setProject(project);
        counter.setName(name);
        return counter;
    }

}
