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

package de.learnlib.alex.learning.entities.learnlibproxies.eqproxies;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.alex.learning.entities.learnlibproxies.CompactMealyMachineProxy;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.oracle.equivalence.SimulatorEQOracle;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

import java.io.Serializable;

/**
 * Proxy around a {@link de.learnlib.oracle.equivalence.SimulatorEQOracle}.
 * The Proxy is needed to make it easier to (de-)serialize the Transition into/ from JSON.
 */
@JsonTypeName("hypothesis")
public class HypothesisEQOracleProxy extends AbstractEquivalenceOracleProxy implements Serializable {

    private static final long serialVersionUID = -110995671060498443L;

    /** The Hypothesis to check against. */
    private CompactMealyMachineProxy hypothesis;

    /**
     * @return The Hypothesis to check against.
     */
    public CompactMealyMachineProxy getHypothesis() {
        return hypothesis;
    }

    /**
     * @param hypothesis The new Hypothesis to check against.
     */
    public void setHypothesis(CompactMealyMachineProxy hypothesis) {
        this.hypothesis = hypothesis;
    }

    @Override
    public void checkParameters() throws IllegalArgumentException {
        if (hypothesis == null || hypothesis.getNodes().size() == 0) {
            throw new IllegalArgumentException("Invalid Hypothesis to compare!");
        }
    }

    @Override
    public EquivalenceOracle<MealyMachine<?, String, ?, String>, String, Word<String>> createEqOracle(
            MembershipOracle<String, Word<String>> membershipOracle) {
        Alphabet<String> alphabet = hypothesis.createAlphabet();
        CompactMealy<String, String> compactMealy = hypothesis.createMealyMachine(alphabet);
        return new SimulatorEQOracle.MealySimulatorEQOracle<>(compactMealy);
    }
}
