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

/**
 * Interface that describes the basics of a Connector.
 */
public interface Connector {

    /**
     * Method called during the reset of the SUL.
     * Set the connector back to the initial state.
     *
     * @throws Exception If an exception occurred while the reset.
     */
    void reset() throws Exception;

    /**
     * Method that is called after a membership query has been executed.
     */
    void dispose();

    /**
     * Method that should be called after the learning that allows to do necessary clean ups.
     * After this method is called, the connector should not work anymore.
     */
    void post();
}
