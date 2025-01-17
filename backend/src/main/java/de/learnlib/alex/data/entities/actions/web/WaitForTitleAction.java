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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.alex.common.utils.LoggerMarkers;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.learning.services.connectors.WebSiteConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Action to wait for the title of a page to change.
 */
@Entity
@DiscriminatorValue("web_waitForTitle")
@JsonTypeName("web_waitForTitle")
public class WaitForTitleAction extends WebSymbolAction {

    private static final long serialVersionUID = -7416267361597106520L;

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Enumeration to specify the wait criterion.
     */
    public enum WaitCriterion {

        /**
         * If the title should be the value.
         */
        IS,

        /**
         * If the title should contain the value.
         */
        CONTAINS;

        /**
         * Parser function to handle the enum names case insensitive.
         *
         * @param name
         *         The enum name.
         * @return The corresponding WaitCriterion.
         * @throws IllegalArgumentException
         *         If the name could not be parsed.
         */
        @JsonCreator
        public static WaitCriterion fromString(String name) throws IllegalArgumentException {
            return WaitCriterion.valueOf(name.toUpperCase());
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * The value the title should match / contain.
     */
    @NotBlank
    @Column(name = "\"value\"")
    private String value;

    /**
     * Which criterion is used to wait for the title.
     */
    @NotNull
    private WaitCriterion waitCriterion;

    /**
     * How many seconds should be waited before the action fails.
     */
    @NotNull
    @Min(0)
    private long maxWaitTime;

    @Override
    protected ExecuteResult execute(WebSiteConnector connector) {
        if (maxWaitTime < 0) {
            return getFailedOutput();
        }

        final WebDriverWait wait = new WebDriverWait(connector.getDriver(), maxWaitTime);
        final String valueWithVariables = insertVariableValues(value);

        try {
            switch (waitCriterion) {
                case IS:
                    wait.until(wd -> wd.getTitle().equals(valueWithVariables));
                    break;
                case CONTAINS:
                    wait.until(wd -> wd.getTitle().contains(valueWithVariables));
                    break;
                default:
                    return getFailedOutput();
            }

            return getSuccessOutput();
        } catch (TimeoutException e) {
            LOGGER.info(LoggerMarkers.LEARNER, "Waiting on the title '{}' (criterion: '{}') timed out. "
                            + "Last known title was '{}'.",
                    valueWithVariables, waitCriterion, connector.getDriver().getTitle());
            return getFailedOutput();
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WaitCriterion getWaitCriterion() {
        return waitCriterion;
    }

    public void setWaitCriterion(WaitCriterion waitCriterion) {
        this.waitCriterion = waitCriterion;
    }

    public long getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(long maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

}
