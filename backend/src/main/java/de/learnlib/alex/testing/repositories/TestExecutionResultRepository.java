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

import de.learnlib.alex.testing.entities.TestExecutionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** The repository for test results. */
@Repository
public interface TestExecutionResultRepository extends JpaRepository<TestExecutionResult, Long> {

    /**
     * Count all results by used symbol ID.
     *
     * @param symbolId
     *         The ID of the symbol.
     * @return The count.
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("checkstyle:methodname")
    Long countAllBySymbol_Id(Long symbolId);
}
