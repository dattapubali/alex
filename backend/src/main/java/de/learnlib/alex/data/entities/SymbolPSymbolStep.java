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

package de.learnlib.alex.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.alex.learning.services.connectors.ConnectorManager;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Execute another symbol on the SUL.
 */
@Entity
@JsonTypeName("symbol")
public class SymbolPSymbolStep extends SymbolStep implements Serializable {

    /** The symbol to execute. */
    @OneToOne
    private ParameterizedSymbol pSymbol;

    @Override
    public ExecuteResult execute(int i, ConnectorManager connectors) {
        return pSymbol.execute(connectors);
    }

    @JsonProperty("pSymbol")
    public ParameterizedSymbol getPSymbol() {
        return pSymbol;
    }

    @JsonProperty("pSymbol")
    public void setPSymbol(ParameterizedSymbol symbol) {
        this.pSymbol = symbol;
    }
}