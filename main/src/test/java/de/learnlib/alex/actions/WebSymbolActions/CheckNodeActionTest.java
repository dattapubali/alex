package de.learnlib.alex.actions.WebSymbolActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.learnlib.alex.core.entities.ExecuteResult;
import de.learnlib.alex.core.entities.Project;
import de.learnlib.alex.core.entities.User;
import de.learnlib.alex.core.learner.connectors.WebSiteConnector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static de.learnlib.alex.core.entities.ExecuteResult.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckNodeActionTest {

    @Mock
    private User user;

    @Mock
    private Project project;

    private CheckNodeAction checkNode;

    @Before
    public void setUp() {
        checkNode = new CheckNodeAction();
        checkNode.setUser(user);
        checkNode.setProject(project);
        checkNode.setValue("#node");
}

    @Test
    public void testJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(checkNode);
        CheckNodeAction c2 = mapper.readValue(json, CheckNodeAction.class);

        assertEquals(checkNode.getValue(), c2.getValue());
    }

    @Test
    public void testJSONFile() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(getClass().getResource("/actions/websymbolactions/CheckNodeTestData.json").toURI());
        WebSymbolAction obj = mapper.readValue(file, WebSymbolAction.class);

        assertTrue(obj instanceof CheckNodeAction);
        CheckNodeAction objAsAction = (CheckNodeAction) obj;
        assertEquals("#node", objAsAction.getValue());
    }

    @Test
    public void shouldReturnOKIfNodeWasFound() {
        WebSiteConnector connector = mock(WebSiteConnector.class);
        WebElement element = mock(WebElement.class);
        given(connector.getElement("#node")).willReturn(element);

        assertEquals(OK, checkNode.execute(connector));
    }

    @Test
    public void shouldReturnFaliedIfNodeWasNotFound() {
        WebSiteConnector connector = mock(WebSiteConnector.class);
        when(connector.getElement("#node")).thenThrow(new NoSuchElementException(""));

        assertEquals(ExecuteResult.FAILED, checkNode.execute(connector));
    }

}
