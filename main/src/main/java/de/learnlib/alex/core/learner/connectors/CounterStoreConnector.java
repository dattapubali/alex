package de.learnlib.alex.core.learner.connectors;

import de.learnlib.alex.core.dao.CounterDAO;
import de.learnlib.alex.core.dao.CounterDAOImpl;
import de.learnlib.alex.core.entities.Counter;
import de.learnlib.alex.core.entities.Project;
import de.learnlib.alex.core.entities.User;
import de.learnlib.alex.exceptions.NotFoundException;

/**
 * Connector to store and manage counters.
 */
public class CounterStoreConnector implements Connector {

    /**
     * The DAO to persist the counters to and fetch the counters from.
     */
    private CounterDAO counterDAO;

    /**
     * Default constructor.
     * Creates a new CounterDAO object.
     */
    public CounterStoreConnector() {
        this(new CounterDAOImpl());
    }

    public CounterStoreConnector(CounterDAO counterDAO) {
        this.counterDAO = counterDAO;
    }

    @Override
    public void reset() {
        // nothing to do here
    }

    @Override
    public void dispose() {
        // nothing to do here
    }

    public void set(Long userId, Long projectId, String name, Integer value) {
        Counter counter;
        try {
            counter = counterDAO.get(userId, projectId, name);
            counter.setValue(value);
            counterDAO.update(counter);
        } catch (NotFoundException e) {
            createCounter(userId, projectId, name, value);
        }
    }

    public void reset(Long projectId, String name) {
        // TODO: is this method even used?
        //set(projectId, name, 0);
    }

    public void increment(Long userId, Long projectId, String name) {
        Counter counter;
        try {
            counter = counterDAO.get(userId, projectId, name);
            counter.setValue(counter.getValue() + 1);
            counterDAO.update(counter);
        } catch (NotFoundException e) {
            createCounter(userId, projectId, name, 1);
        }
    }

    public Integer get(Long userId, Long projectId, String name) throws IllegalStateException {
        try {
            Counter counter;
            counter = counterDAO.get(userId, projectId, name);
            return counter.getValue();
        } catch (NotFoundException e) {
            throw new IllegalStateException("The counter '" + name + "' was not set and has no value!");
        }
    }

    void createCounter(Long userId, Long projectId, String name, Integer value) {
        Counter counter = new Counter();
        counter.setUser(new User(userId));
        counter.setProject(new Project(projectId));
        counter.setName(name);
        counter.setValue(value);
        counterDAO.create(counter);
    }
}
