/*
 * Copyright 2018 TU Dortmund
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

import de.learnlib.alex.testing.entities.TestCaseStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** The repository for test case steps. */
@Repository
public interface TestCaseStepRepository extends JpaRepository<TestCaseStep, Long> {

    /**
     * Delete all those test steps of a test whose ids are not specified to stay. This should be used when updating a
     * test case.
     *
     * @param testId
     *         The id of the test in the db.
     * @param testCaseIds
     *         The ids of the test case steps in the db.
     */
    @Transactional
    @SuppressWarnings("checkstyle:methodname")
    void deleteAllByTestCase_IdAndIdNotIn(Long testId, List<Long> testCaseIds);

    /**
     * Get all test case steps that use a certain symbol.
     *
     * @param projectId
     *         The id of the project.
     * @param symbolId
     *         The id of the symbol.
     * @return The list of steps that use the symbol.
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("checkstyle:methodname")
    List<TestCaseStep> findAllByTestCase_Project_IdAndSymbol_Id(Long projectId, Long symbolId);
}