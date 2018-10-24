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

import de.learnlib.api.oracle.SymbolQueryOracle;

public class StatisticsOracle<I, O> implements SymbolQueryOracle<I, O> {

    private final SymbolQueryOracle<I, O> delegate;

    private long symbolCount = 0;

    private long resetCount = 0;

    public StatisticsOracle(SymbolQueryOracle<I, O> delegate) {
        this.delegate = delegate;
    }

    @Override
    public O query(I i) {
        symbolCount++;
        return delegate.query(i);
    }

    @Override
    public void reset() {
        resetCount++;
        delegate.reset();
    }

    public void resetCounters() {
        resetCount = 0;
        symbolCount = 0;
    }

    public long getResetCount() {
        return resetCount;
    }

    public long getSymbolCount() {
        return symbolCount;
    }
}
