/*
 * Copyright 2016 TU Dortmund
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

package de.learnlib.alex.testing.services.reporters;

import de.learnlib.alex.testing.entities.TestReportConfig;

/**
 * Create a report of a test result.
 *
 * @param <T> The type of the report.
 */
public abstract class TestResultReporter<T> {

    /**
     * Create a report of a test result.
     *
     * @param reportConfig The config to create a report from.
     * @return The report.
     */
    public abstract T createReport(TestReportConfig reportConfig);
}