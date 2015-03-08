package de.learnlib.weblearner.entities.StoreSymbolActions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.weblearner.entities.ExecuteResult;
import de.learnlib.weblearner.entities.SymbolAction;
import de.learnlib.weblearner.learner.connectors.MultiConnector;
import de.learnlib.weblearner.learner.connectors.VariableStoreConnector;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("declareVariable")
@JsonTypeName("declareVariable")
public class DeclareVariableAction extends SymbolAction {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ExecuteResult execute(MultiConnector connector) {
        VariableStoreConnector storeConnector = connector.getConnector(VariableStoreConnector.class);
        try {
            storeConnector.declare(name);
            return ExecuteResult.OK;
        } catch (IllegalArgumentException e) {
            return ExecuteResult.FAILED;
        }
    }

}