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

package de.learnlib.alex.testing.repositories;

import de.learnlib.alex.testing.entities.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for test results.
 */
@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    /**
     * Get all test results of a test.
     *
     * @param testId
     *         The ID of the test.
     * @param pageable
     *         The pageable object.
     * @return The test results in descending order.
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("checkstyle:methodname")
    Page<TestResult> findAllByTest_IdOrderByTestReport_StartDateDesc(Long testId, Pageable pageable);
}
