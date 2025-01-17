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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.modelchecking.entities.LtsFormula;
import de.learnlib.alex.testing.entities.Test;
import de.learnlib.alex.testing.entities.TestExecutionConfig;
import de.learnlib.alex.testing.entities.TestReport;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Representation of a testing project with different symbols.
 */
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"})
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project implements Serializable {

    private static final long serialVersionUID = -6760395646972200067L;

    /** The maximum length for the project description. */
    private static final int MAX_DESCRIPTION_LENGTH = 250;

    /**
     * The project ID.
     */
    @Id
    @GeneratedValue
    private Long id;

    /** The user that owns this project. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private User user;

    /**
     * The name of the project. This property is required & must be unique.
     */
    @NotBlank
    private String name;

    /**
     * A text to describe the Project.
     */
    @Length(max = MAX_DESCRIPTION_LENGTH)
    private String description;

    /**
     * The URLs where instances of the target system a accessible.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @NotNull
    private List<ProjectUrl> urls;

    /**
     * The list of groups in the project.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JsonIgnore
    private Set<SymbolGroup> groups;

    /**
     * The list of test reports in the project.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JsonIgnore
    private Set<TestReport> testReports;

    /**
     * The symbols used to test.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private Set<Symbol> symbols;

    /** The tests of this project. */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private Set<Test> tests;

    /** The test configurations of this project. */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JsonIgnore
    private List<TestExecutionConfig> testExecutionConfigs;

    /**
     * The counters of the project.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private Set<Counter> counters;

    /**
     * The lts formulas of the project.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<LtsFormula> ltsFormulas;

    /**
     * Default constructor.
     */
    public Project() {
        this(0L);
    }

    /**
     * Constructor which set the ID.
     *
     * @param projectId The ID.
     */
    public Project(Long projectId) {
        this.id = projectId;
        this.groups = new HashSet<>();
        this.symbols = new HashSet<>();
        this.tests = new HashSet<>();
        this.testReports = new HashSet<>();
        this.testExecutionConfigs = new ArrayList<>();
        this.urls = new ArrayList<>();
        this.ltsFormulas = new ArrayList<>();
    }

    /**
     * Get the ID of the project.
     *
     * @return The ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of this project.
     *
     * @param id The new ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The user that owns the project.
     */
    @JsonIgnore
    public User getUser() {
        return user;
    }

    /**
     * @param user The new user that owns the project.
     */
    @JsonIgnore
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return The ID of the user, which is needed for the JSON.
     */
    @JsonProperty("user")
    public Long getUserId() {
        return user == null ? null : user.getId();
    }

    /**
     * @param userId The new ID of the user, which is needed for the JSON.
     */
    @JsonProperty("user")
    public void setUserId(Long userId) {
        this.user = new User(userId);
    }

    /**
     * Get the name of this project.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set a new name for the project. The name must be there and be unique.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the Project.
     *
     * @return The Project description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this project.
     *
     * @param description The new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the groups of the project.
     *
     * @return The related groups.
     */
    public Set<SymbolGroup> getGroups() {
        return groups;
    }

    /**
     * Set a new set of groups that are used in the project.
     *
     * @param groups The new set of groups.
     */
    public void setGroups(Set<SymbolGroup> groups) {
        this.groups = groups;
    }

    /**
     * Add one group to the project.
     *
     * @param group The group to add.
     */
    public void addGroup(SymbolGroup group) {
        this.groups.add(group);
        group.setProject(this);
    }

    /**
     * Get the set of symbols in the project.
     *
     * @return The Set of Symbols.
     */
    @JsonIgnore
    public Collection<Symbol> getSymbols() {
        return symbols;
    }

    /**
     * @param symbols the symbols to set
     */
    @JsonIgnore
    public void setSymbols(Set<Symbol> symbols) {
        this.symbols = symbols;
    }

    /**
     * Add a Symbol to the Project and set the Project in the Symbol.
     * This only establishes the bidirectional relation does nothing else,
     * e.g. it does not take care of the right id.
     *
     * @param symbol The Symbol to add.
     */
    public void addSymbol(Symbol symbol) {
        this.symbols.add(symbol);
        symbol.setProject(this);
    }

    @JsonIgnore
    public Collection<Test> getTests() {
        return tests;
    }

    @JsonIgnore
    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }

    @JsonIgnore
    public Set<TestReport> getTestReports() {
        return testReports;
    }

    @JsonIgnore
    public void setTestReports(Set<TestReport> testReports) {
        this.testReports = testReports;
    }

    /**
     * @return All the counters of the Project.
     */
    @JsonProperty
    public Set<Counter> getCounters() {
        return counters;
    }

    /**
     * @param counters The new set of counters for the project.
     */
    @JsonIgnore
    public void setCounters(Set<Counter> counters) {
        this.counters = counters;
    }

    public List<LtsFormula> getLtsFormulas() {
        return ltsFormulas;
    }

    public void setLtsFormulas(List<LtsFormula> ltsFormulas) {
        this.ltsFormulas = ltsFormulas;
    }

    public List<ProjectUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<ProjectUrl> urls) {
        this.urls = urls;
    }

    @JsonIgnore
    @Transient
    public ProjectUrl getDefaultUrl() {
        return this.urls.stream()
                .filter(ProjectUrl::isDefault)
                .findFirst()
                .orElse(null);
    }

    public List<TestExecutionConfig> getTestExecutionConfigs() {
        return testExecutionConfigs;
    }

    public void setTestExecutionConfigs(List<TestExecutionConfig> testExecutionConfigs) {
        this.testExecutionConfigs = testExecutionConfigs;
    }

    @Override
    @SuppressWarnings("checkstyle:needbraces") // Auto generated by IntelliJ
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[Project " + id + "]: " + user + ", " + name;
    }

}
