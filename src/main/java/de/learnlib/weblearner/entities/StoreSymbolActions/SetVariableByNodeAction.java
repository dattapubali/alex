package de.learnlib.weblearner.entities.StoreSymbolActions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.learnlib.weblearner.entities.ExecuteResult;
import de.learnlib.weblearner.learner.connectors.MultiConnector;
import de.learnlib.weblearner.learner.connectors.VariableStoreConnector;
import de.learnlib.weblearner.learner.connectors.WebSiteConnector;
import org.openqa.selenium.NoSuchElementException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("setVariableByHTML")
@JsonTypeName("setVariableByHTML")
public class SetVariableByNodeAction extends SetVariableAction {

    @Override
    public ExecuteResult execute(MultiConnector connector) {
        VariableStoreConnector storeConnector = connector.getConnector(VariableStoreConnector.class);
        WebSiteConnector webSiteConnector = connector.getConnector(WebSiteConnector.class);

        try {
            String valueInTheNode = webSiteConnector.getElement(value).getText();
            storeConnector.set(name, valueInTheNode);
            return ExecuteResult.OK;
        } catch (IllegalStateException | NoSuchElementException e) {
            return ExecuteResult.FAILED;
        }
    }
}