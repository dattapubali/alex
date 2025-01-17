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

package de.learnlib.alex.data.entities.actions.misc;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.alex.common.utils.LoggerMarkers;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.data.entities.SymbolAction;
import de.learnlib.alex.learning.services.connectors.ConnectorManager;
import de.learnlib.alex.learning.services.connectors.VariableStoreConnector;
import de.learnlib.alex.learning.services.connectors.WebServiceConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

/** Sets a variable by the status of an HTTP response. */
@Entity
@DiscriminatorValue("setVariableByHttpStatus")
@JsonTypeName("setVariableByHttpStatus")
public class SetVariableByHttpStatusAction extends SymbolAction {

    private static final long serialVersionUID = 7175288271151692417L;

    private static final Logger LOGGER = LogManager.getLogger();

    /** The name of the variable. */
    @NotEmpty
    private String name;

    @Override
    public ExecuteResult execute(ConnectorManager connector) {
        final WebServiceConnector webServiceConnector = connector.getConnector(WebServiceConnector.class);
        final VariableStoreConnector variableStore = connector.getConnector(VariableStoreConnector.class);

        try {
            final int status = webServiceConnector.getStatus();
            variableStore.set(name, String.valueOf(status));

            LOGGER.info(LoggerMarkers.LEARNER, "Set variable '{}' to status '{}'.", status);
            return getSuccessOutput();
        } catch (Exception e) {
            LOGGER.info(LoggerMarkers.LEARNER, "Could not set variable '{}'");
            return getFailedOutput();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
