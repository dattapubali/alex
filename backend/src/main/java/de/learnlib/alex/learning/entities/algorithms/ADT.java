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


package de.learnlib.alex.learning.entities.algorithms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.algorithms.adt.automaton.ADTState;
import de.learnlib.algorithms.adt.learner.ADTLearner;
import de.learnlib.algorithms.adt.learner.ADTLearnerBuilder;
import de.learnlib.algorithms.adt.learner.ADTLearnerState;
import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.api.oracle.SymbolQueryOracle;
import net.automatalib.words.Alphabet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/** Wrapper class for the {@link de.learnlib.algorithms.adt.learner.ADTLearner} algorithm. */
@JsonTypeName("ADT")
public class ADT extends AbstractLearningAlgorithm<String, String> {

    @Override
    public LearningAlgorithm.MealyLearner<String, String> createLearner(
            Alphabet<String> alphabet,
            SymbolQueryOracle<String, String> oracle) {
        return new ADTLearnerBuilder<String, String>()
                .withAlphabet(alphabet)
                .withOracle(oracle)
                .create();
    }

    @Override
    public String getInternalData(LearningAlgorithm.MealyLearner<String, String> learner) {
        return "";
    }

    @Override
    public void resume(LearningAlgorithm.MealyLearner<String, String> learner, byte[] data)
            throws IOException, ClassNotFoundException {
        try (final ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            final ADTLearnerState<ADTState<String, String>, String, String> state
                    = (ADTLearnerState<ADTState<String, String>, String, String>) objectIn.readObject();
            ((ADTLearner<String, String>) learner).resume(state);
        }
    }
}
