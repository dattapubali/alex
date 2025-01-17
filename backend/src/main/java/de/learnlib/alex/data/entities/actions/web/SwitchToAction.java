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


package de.learnlib.alex.data.entities.actions.web;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.alex.common.utils.LoggerMarkers;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.learning.services.connectors.WebSiteConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Action to submit a specific element.
 */
@Entity
@DiscriminatorValue("web_switchTo")
@JsonTypeName("web_switchTo")
public class SwitchToAction extends WebSymbolAction {

    private static final long serialVersionUID = 5072169613597915144L;

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The target to switch to.
     */
    private enum TargetType {

        /** The parent frame. */
        PARENT_FRAME,

        /** The default browser frame. */
        DEFAULT_CONTENT,

        /** The last frame that has been visited. */
        LAST_FRAME
    }

    /**
     * The target type.
     */
    @NotNull
    private TargetType target;

    @Override
    protected ExecuteResult execute(WebSiteConnector connector) {
        try {
            switch (target) {
                case PARENT_FRAME:
                    connector.getDriver().switchTo().parentFrame();
                    break;
                case DEFAULT_CONTENT:
                    connector.getDriver().switchTo().defaultContent();
                    break;
                case LAST_FRAME:
                    connector.getDriver().switchTo().frame(connector.getLastFrame());
                    break;
                default:
                    throw new Exception("Undefined target type.");
            }
            LOGGER.info(LoggerMarkers.LEARNER, "Switch to '{}'", target);
            return getSuccessOutput();
        } catch (Exception e) {
            LOGGER.info(LoggerMarkers.LEARNER, "Could not switch to '{}'", target, e);
            return getFailedOutput();
        }
    }

    public TargetType getTarget() {
        return target;
    }

    public void setTarget(TargetType target) {
        this.target = target;
    }
}
