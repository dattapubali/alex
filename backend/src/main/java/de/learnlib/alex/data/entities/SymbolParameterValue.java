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

package de.learnlib.alex.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * The value of a symbol parameter.
 */
@Entity
public class SymbolParameterValue implements Serializable {

    private static final long serialVersionUID = 2125637165072908329L;

    /** The id of the value in the db. */
    @Id
    @GeneratedValue
    private Long id;

    /** The parameter for which its value is saved. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "symbolParameterId")
    private SymbolParameter parameter;

    /** The value for the parameter. */
    @Column(name = "\"value\"")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SymbolParameter getParameter() {
        return parameter;
    }

    public void setParameter(SymbolParameter parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
