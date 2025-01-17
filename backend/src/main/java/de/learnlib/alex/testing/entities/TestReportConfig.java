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

package de.learnlib.alex.testing.entities;

import java.util.Map;

/**
 * The config to create a report.
 */
public class TestReportConfig {

    /**
     * The results.
     */
    private Map<Long, TestResult> results;

    /**
     * The test suite the results belong to.
     */
    private TestSuite parent;

    /**
     * Constructor.
     */
    public TestReportConfig() {
    }

    public Map<Long, TestResult> getResults() {
        return results;
    }

    public void setResults(Map<Long, TestResult> results) {
        this.results = results;
    }

    public TestSuite getParent() {
        return parent;
    }

    public void setParent(TestSuite parent) {
        this.parent = parent;
    }
}
