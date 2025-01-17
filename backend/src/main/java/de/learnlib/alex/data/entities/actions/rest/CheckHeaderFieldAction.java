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

package de.learnlib.alex.data.entities.actions.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.alex.common.utils.LoggerMarkers;
import de.learnlib.alex.common.utils.SearchHelper;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.learning.services.connectors.WebServiceConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * RESTSymbolAction to check the HTTP Header fields of the last request.
 */
@Entity
@DiscriminatorValue("rest_checkHeaderField")
@JsonTypeName("rest_checkHeaderField")
public class CheckHeaderFieldAction extends RESTSymbolAction {

    private static final long serialVersionUID = -7234083244640666736L;

    private static final Logger LOGGER = LogManager.getLogger();

    /** The key of the header field to check for the value. */
    @NotBlank
    @Column(name = "\"key\"")
    private String key;

    /** The expected value which should be in the header field. */
    @NotBlank
    @Column(name = "\"value\"")
    private String value;

    /** Field to determine if the search string is a regular expression. */
    @NotNull
    @Column(name = "\"regexp\"")
    private boolean regexp;

    @Override
    public ExecuteResult execute(WebServiceConnector connector) {
        List<Object> headerFieldValues = connector.getHeaders().get(key);
        if (headerFieldValues == null) {
            LOGGER.info(LoggerMarkers.LEARNER, "Could header {} against the value {}, because the header was not found (regExp: {}).",
                    key, value, regexp);
            return getFailedOutput();
        }

        boolean result;
        if (regexp) {
            result = searchWithRegex(headerFieldValues);
        } else {
            result = search(headerFieldValues);
        }

        LOGGER.info(LoggerMarkers.LEARNER, "Checked header {} with the value {} against {} => {} (regExp: {}).",
                key, headerFieldValues, value, result, regexp);
        if (result) {
            return getSuccessOutput();
        } else {
            return getFailedOutput();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    /**
     * Get the expected value of the header field. All variables and counters will be replaced with their values.
     *
     * @return The value to search for.
     */
    @Transient
    @JsonIgnore
    public String getValueWithVariableValues() {
        return insertVariableValues(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRegexp() {
        return regexp;
    }

    public void setRegexp(boolean regexp) {
        this.regexp = regexp;
    }

    private boolean search(List<Object> headerFieldValues) {
        return headerFieldValues.contains(getValueWithVariableValues());
    }

    private boolean searchWithRegex(List<Object> headerFieldValues) {
        for (Object headerFieldValue : headerFieldValues) {
            String headerFieldValueAsString = headerFieldValue.toString();
            if (SearchHelper.searchWithRegex(getValueWithVariableValues(), headerFieldValueAsString)) {
                return true;
            }
        }
        return false;
    }

}
