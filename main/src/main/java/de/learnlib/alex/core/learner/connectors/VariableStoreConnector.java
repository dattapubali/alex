package de.learnlib.alex.core.learner.connectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Connector to hold and manage variables.
 */
public class VariableStoreConnector implements Connector {

    /** Use the learner logger. */
    private static final Logger LOGGER = LogManager.getLogger("learner");

    /** The variable store. */
    private Map<String, String> store;

    /**
     * Default constructor.
     */
    public VariableStoreConnector() {
        this.store = new HashMap<>();
    }

    @Override
    public void reset() {
        store = new HashMap<>();
    }

    @Override
    public void dispose() {
        // nothing to do here
    }

    /**
     * Set a variable to a certain value.
     *
     * @param name
     *         The name of the variable to set.
     * @param value
     *         The value to set.
     */
    public void set(String name, String value) {
        store.put(name, value);
        LOGGER.debug("Set the variable '" + name + "' to the value '" + value + "'.");
    }

    /**
     * Check if a variable with a specific name exists.
     *
     * @param name
     *         The name to check.
     * @return true if the variable exists; false otherwise.
     */
    public boolean contains(String name) {
        return store.containsKey(name);
    }

    /**
     * Get the value of a variable.
     *
     * @param name
     *         The variable to get the value from.
     * @return The value of the variable.
     * @throws IllegalStateException
     *         If the variable was not set before.
     */
    public String get(String name) throws IllegalStateException {
        String result = store.get(name);
        if (result == null) {
            throw new IllegalStateException("The variable '" + name + "' was not set and has no value!");
        }
        return result;
    }
}
