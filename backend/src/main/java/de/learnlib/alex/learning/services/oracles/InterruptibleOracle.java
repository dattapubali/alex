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

package de.learnlib.alex.learning.services.oracles;

import de.learnlib.alex.common.exceptions.LearnerInterruptedException;
import de.learnlib.api.oracle.SymbolQueryOracle;

/**
 * Membership oracle that can be interrupted to pose queries to a delegate.
 *
 * @param <I>
 *         The input symbol type.
 * @param <O>
 *         The output symbol type.
 */
public class InterruptibleOracle<I, O> implements SymbolQueryOracle<I, O> {

    /** The oracle to delegate queries to. */
    private final SymbolQueryOracle<I, O> delegate;

    /** If the process has been interrupted. */
    private boolean interrupted;

    /**
     * Constructor.
     *
     * @param delegate
     *         {@link #delegate}
     */
    public InterruptibleOracle(SymbolQueryOracle<I, O> delegate) {
        this.delegate = delegate;
        this.interrupted = false;
    }

    @Override
    public O query(I i) {
        return delegate.query(i);
    }

    @Override
    public void reset() {
        if (interrupted) {
            throw new LearnerInterruptedException("Learning process aborted by user.");
        }
        delegate.reset();
    }

    /** Interrupt the oracle. */
    public void interrupt() {
        this.interrupted = true;
    }
}
