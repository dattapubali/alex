package de.learnlib.alex.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * The model for a user
 * TODO: remove password and salt from json responses
 */
@Entity
public class User implements Serializable {

    // auto generated id for saving it into the db
    private static final long serialVersionUID = -3567360676364330143L;

    // the number of iterations to perform to create a secure hash
    private static final int HASH_ITERATIONS = 2048;

    /**
     * The unique id of the user
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The email address of the user he uses to login
     */
    @NotNull
    @Column(unique = true)
    private String email;

    /**
     * The hash of the users password
     */
    @NotNull
    private String password;

    /**
     * The salt that is used to hash the password
     */
    private String salt;

    /**
     * The role of the user
     */
    private UserRole role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    private Set<Project> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    private Set<SymbolGroup> symbolGroups;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    private Set<Symbol> symbols;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    private Set<SymbolAction> actions;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    private Set<Counter> counters;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    private Set<LearnerResult> learnerResults;

    public User() {
        role = UserRole.REGISTERED;
    }

    public User(Long id) {
        role = UserRole.REGISTERED;
        this.id = id;
    }

    @JsonIgnore
    public void setEncryptedPassword(String password) {
        this.salt = new SecureRandomNumberGenerator().nextBytes().toBase64();
        this.password = new Sha512Hash(password, this.salt, HASH_ITERATIONS).toBase64();
    }

    @JsonIgnore
    public boolean isValidPassword(String password) {
        String hashedPassword = new Sha512Hash(password, this.salt, HASH_ITERATIONS).toBase64();
        return hashedPassword.equals(this.password);
    }

    // auto generated equals and hashcode method

    /**
     * Two users are equals if they have the same id
     *
     * @param o Object to compare with
     * @return if both users have the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // auto generated getter & setter

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @JsonIgnore
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @JsonProperty("salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<SymbolGroup> getSymbolGroups() {
        return symbolGroups;
    }

    public void setSymbolGroups(Set<SymbolGroup> symbolGroups) {
        this.symbolGroups = symbolGroups;
    }

    public Set<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(Set<Symbol> symbols) {
        this.symbols = symbols;
    }

    public Set<SymbolAction> getActions() {
        return actions;
    }

    public void setActions(Set<SymbolAction> actions) {
        this.actions = actions;
    }

    public Set<Counter> getCounters() {
        return counters;
    }

    public void setCounters(Set<Counter> counters) {
        this.counters = counters;
    }

    public Set<LearnerResult> getLearnerResults() {
        return learnerResults;
    }

    public void setLearnerResults(Set<LearnerResult> learnerResults) {
        this.learnerResults = learnerResults;
    }

    @Override
    public String toString() {
        return "User [" + id + "]: " + email;
    }
}