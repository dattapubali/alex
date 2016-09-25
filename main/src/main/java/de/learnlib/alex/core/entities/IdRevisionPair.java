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

package de.learnlib.alex.core.entities;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Class to put an ID and a Revision together.
 */
@Embeddable
public class IdRevisionPair implements Serializable {

    private static final long serialVersionUID = -6906978252141016538L;

    /** The ID of a {@link Symbol}. */
    @NotNull
    protected Long id;

    /** The revision of a {@link Symbol}. */
    @NotNull
    protected Long revision;

    /**
     * Default constructor.
     */
    public IdRevisionPair() {
        // Nothing to do
    }

    /**
     * Advanced constructor which sets all the fields.
     *
     * @param id
     *         The ID.
     * @param revision
     *         The revision.
     */
    public IdRevisionPair(Long id, Long revision) {
        this.id = id;
        this.revision = revision;
    }

    /**
     * Advanced constructor which sets all the fields according to a Symbol.
     *
     * @param symbol
     *         The symbol to copy the ID and Revision from..
     */
    public IdRevisionPair(Symbol symbol) {
        if (symbol != null) {
            this.id = symbol.getId();
            this.revision = symbol.getRevision();
        }
    }

    /**
     * Get the ID.
     * 
     * @return The ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set a new ID.
     * 
     * @param id
     *            The new ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the revision.
     * 
     * @return The revision.
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * Set a new revision.
     * 
     * @param revision
     *            The new revision.
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    //CHECKSTYLE.OFF: AvoidInlineConditionals|MagicNumber|NeedBraces - auto generated by IntelliJ IDEA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdRevisionPair)) return false;

        IdRevisionPair that = (IdRevisionPair) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(revision != null ? !revision.equals(that.revision) : that.revision != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (revision != null ? revision.hashCode() : 0);
        return result;
    }
    //CHECKSTYLE.ON: MagicNumber

    @Override
    public String toString() {
        return "<" + id + ":" + revision + ">";
    }
}
