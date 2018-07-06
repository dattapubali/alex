/*
 * Copyright 2018 TU Dortmund
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
import de.learnlib.alex.auth.repositories.UserRepository;
import de.learnlib.alex.data.entities.Project;
import de.learnlib.alex.data.repositories.ProjectRepository;
import de.learnlib.alex.learning.entities.LearnerResult;
import de.learnlib.alex.learning.repositories.LearnerResultRepository;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@Ignore
public class LearnerResultRepositoryIT extends AbstractRepositoryIT {

    @Inject
    private UserRepository userRepository;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private LearnerResultRepository learnerResultRepository;

    @After
    public void tearDown() {
        // deleting the user should (!) also deleteMany all projects, groups, symbols, ... related to that user.
        userRepository.deleteAll();
    }

    @Test
    public void shouldSaveAValidLearnerResult() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result = createLearnerResult(user, project, 0L);

        learnerResultRepository.save(result);

        assertNotNull(result.getId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailToSaveALearnerResultWithoutAnUser() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result = createLearnerResult(null, project, 0L);

        learnerResultRepository.save(result); // should fail
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailToSaveALearnerResultWithoutAProject() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        projectRepository.save(project);
        //
        LearnerResult result = createLearnerResult(user, null, 0L);

        learnerResultRepository.save(result); // should fail
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailToSaveALearnerResultWithoutATestNo() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result = createLearnerResult(user, project, null);

        learnerResultRepository.save(result); // should fail
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailToSaveALearnerResultWithADuplicateTestNo() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result1 = createLearnerResult(user, project, 0L);
        learnerResultRepository.save(result1);
        LearnerResult result2 = createLearnerResult(user, project, 0L);

        learnerResultRepository.save(result2); // should fail
    }

    @Test
    public void shouldSaveLearnerResultsWithADuplicateTestNoInDifferentProjects() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project1 = createProject(user, "Test Project 1");
        project1 = projectRepository.save(project1);
        Project project2 = createProject(user, "Test Project 2");
        project2 = projectRepository.save(project2);
        //
        LearnerResult result1 = createLearnerResult(user, project1, 0L);
        learnerResultRepository.save(result1);
        LearnerResult result2 = createLearnerResult(user, project2, 0L);

        result2 = learnerResultRepository.save(result2);

        assertNotNull(result2.getId());
    }

    @Test
    public void shouldFetchAllLearnerResultsOfAProject() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result1 = createLearnerResult(user, project, 0L);
        learnerResultRepository.save(result1);
        LearnerResult result2 = createLearnerResult(user, project, 1L);
        learnerResultRepository.save(result2);

        List<LearnerResult> results = learnerResultRepository
                .findByProject_IdOrderByTestNoAsc(project.getId());

        assertThat(results.size(), is(equalTo(2)));
        assertThat(results, hasItem(equalTo(result1)));
        assertThat(results, hasItem(equalTo(result2)));
    }

    @Test
    public void shouldFetchLearnerResultsOfAProjectByTheirTestNo() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result1 = createLearnerResult(user, project, 0L);
        learnerResultRepository.save(result1);
        LearnerResult result2 = createLearnerResult(user, project, 1L);
        learnerResultRepository.save(result2);
        LearnerResult result3 = createLearnerResult(user, project, 2L);
        learnerResultRepository.save(result3);

        List<LearnerResult> results = learnerResultRepository.findByProject_IdAndTestNoIn(project.getId(), Arrays.asList(0L, 2L));

        assertThat(results.size(), is(equalTo(2)));
        assertThat(results, hasItem(equalTo(result1)));
        assertThat(results, not(hasItem(equalTo(result2))));
        assertThat(results, hasItem(equalTo(result3)));
    }

    @Test
    public void shouldFetchHighestTestNo() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result1 = createLearnerResult(user, project, 0L);
        learnerResultRepository.save(result1);
        LearnerResult result2 = createLearnerResult(user, project, 1L);
        learnerResultRepository.save(result2);

        Long highestTestNo = learnerResultRepository.findHighestTestNo(project.getId());

        assertThat(highestTestNo, is(equalTo(1L)));
    }

    @Test
    public void shouldFetchNullAsHighestTestNoIfNoLearnerResultsExists() {
        Long highestTestNo = learnerResultRepository.findHighestTestNo(-1L);

        assertNull(highestTestNo);
    }

    @Test
    public void shouldDeleteALearnerResult() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);
        //
        LearnerResult result = createLearnerResult(user, project, 0L);
        learnerResultRepository.save(result);

        Long deleteReturnValue = learnerResultRepository.deleteByProject_IdAndTestNoIn(
                project.getId(), Collections.singletonList(0L));

        assertThat(deleteReturnValue, is(equalTo(1L)));
        assertThat(learnerResultRepository.count(), is(equalTo(0L)));
    }

    @Test
    public void shouldNotDeleteAnNonExistingLearnerResult() {
        User user = createUser("alex@test.example");
        user = userRepository.save(user);
        //
        Project project = createProject(user, "Test Project");
        project = projectRepository.save(project);

        Long deleteReturnValue = learnerResultRepository.deleteByProject_IdAndTestNoIn(
                project.getId(), Collections.singletonList(-1L));

        assertThat(deleteReturnValue, is(equalTo(0L)));
    }


    static LearnerResult createLearnerResult(User user, Project project, Long testNo) {
        LearnerResult result = new LearnerResult();
        result.setProject(project);
        result.setTestNo(testNo);
        return result;
    }

}
