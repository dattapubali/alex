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

package de.learnlib.alex.learning.services.connectors;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.common.exceptions.NotFoundException;
import de.learnlib.alex.data.dao.CounterDAO;
import de.learnlib.alex.data.entities.Counter;
import de.learnlib.alex.data.entities.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Connector to store and manage counters.
 */
public class CounterStoreConnector implements Connector {

    private static final Logger LOGGER = LogManager.getLogger();

    /** The map that keeps track of all counters used by different urls. url -> (counterName -> counterValue). */
    private Map<String, Integer> countersMap;

    /** An instance of the counterDAO. */
    private CounterDAO counterDAO;

    /** The current project. */
    private Project project;

    /** The user that executes the experiment. */
    private User user;

    /**
     * Constructor.
     *
     * @param counterDAO
     *         An instance of the counterDAO.
     * @param user
     *         The current user.
     * @param project
     *         The current project.
     * @param counterList
     *         The list of counters in the database to initialize the map with.
     */
    public CounterStoreConnector(CounterDAO counterDAO, User user, Project project, List<Counter> counterList) {
        this.counterDAO = counterDAO;
        this.project = project;
        this.user = user;
        this.countersMap = new HashMap<>();
        counterList.forEach(counter -> this.countersMap.put(counter.getName(), counter.getValue()));
    }

    @Override
    public void reset() {
    }

    @Override
    public void dispose() {
        if (countersMap.isEmpty()) {
            return;
        }

        // get all counters from the db
        Map<String, Counter> counters = new HashMap<>();
        try {
            counterDAO.getAll(user, project.getId()).forEach(c -> counters.put(c.getName(), c));
        } catch (NotFoundException e) {
        }

        // create counters that have not yet been created
        // update counters that have been created
        for (String name : countersMap.keySet()) {
            try {
                boolean counterExists = counters.containsKey(name);
                if (counterExists) {
                    counters.get(name).setValue(Math.max(counters.get(name).getValue(), countersMap.get(name)));
                    counterDAO.update(user, counters.get(name));
                } else {
                    Counter counter = createCounter(project.getId(), name, countersMap.get(name));
                    counterDAO.create(user, counter);
                }
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void post() {
    }

    private Counter createCounter(Long projectId, String name, Integer value) {
        Counter counter = new Counter();
        counter.setProject(new Project(projectId));
        counter.setName(name);
        counter.setValue(value);
        return counter;
    }

    /**
     * Set the value of an existing counter. Creates a new counter implicitly with the specified name and value if it
     * does not exist yet.
     *
     * @param projectId
     *         The id of the project.
     * @param name
     *         The name of the counter.
     * @param value
     *         The value of the counter.
     */
    public void set(Long projectId, String name, Integer value) {
        countersMap.put(name, value);
        LOGGER.debug("Set the counter '{}' in the project <{}> to '{}'.", name, projectId, value);
    }

    /**
     * Increment a counter by a positive or negative value.
     *
     * @param projectId
     *         The id of the project.
     * @param name
     *         The name of the counter.
     * @param incrementBy
     *         The value to increment or decrement the counter by.
     * @throws IllegalStateException
     *         If the counter 'name' has not been set yet.
     */
    public void incrementBy(Long projectId, String name, int incrementBy) {
        if (!countersMap.containsKey(name)) {
            throw new IllegalStateException("Undefined counter: " + name);
        }

        countersMap.put(name, countersMap.get(name) + incrementBy);

        LOGGER.debug("Incremented the counter '{}' in the project <{}> of user <{}> to '{}'.", name, projectId,
                countersMap.get(name));
    }

    /**
     * Get the value of an existing counter.
     *
     * @param name
     *         The name of the counter.
     * @return The positive value of the counter.
     * @throws IllegalStateException
     *         If the counter 'name' has not been set yet.
     */
    public Integer get(String name) throws IllegalStateException {
        if (!countersMap.containsKey(name)) {
            throw new IllegalStateException("Undefined counter: " + name);
        }
        return countersMap.get(name);
    }

    /**
     * Updates the current store by variables in another store.
     *
     * @param storeToMerge
     *         The store with updated variables.
     * @param namesToMerge
     *         The names of the variables that should be transferred to this one.
     */
    public void merge(CounterStoreConnector storeToMerge, List<String> namesToMerge) {
        namesToMerge.forEach(name -> {
            if (storeToMerge.countersMap.containsKey(name)) {
                countersMap.put(name, storeToMerge.countersMap.get(name));
            }
        });
    }

    /**
     * Get the store as read only map.
     * @return The store.
     */
    public Map<String, Integer> getStore() {
        return Collections.unmodifiableMap(countersMap);
    }

    /**
     * Copies the connector.
     *
     * @return A copy of the connector.
     */
    public CounterStoreConnector copy() {
        return new CounterStoreConnector(counterDAO, user, project, new ArrayList<>());
    }
}
