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

package de.learnlib.alex.data.entities.actions.WebSymbolActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.learnlib.alex.data.entities.Symbol;
import de.learnlib.alex.data.entities.WebElementLocator;
import de.learnlib.alex.learning.services.connectors.WebSiteConnector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckNodeActionTest {

    private CheckNodeAction checkNode;

    private WebElementLocator node;

    @Before
    public void setUp() {
        Symbol symbol = new Symbol();

        node = new WebElementLocator();
        node.setSelector("#node");
        node.setType(WebElementLocator.Type.CSS);

        checkNode = new CheckNodeAction();
        checkNode.setSymbol(symbol);
        checkNode.setNode(node);
    }

    @Test
    public void testJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(checkNode);
        CheckNodeAction c2 = mapper.readValue(json, CheckNodeAction.class);

        assertEquals(checkNode.getNode(), c2.getNode());
    }

    @Test
    public void testJSONFile() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(getClass().getResource("/actions/websymbolactions/CheckNodeTestData.json").toURI());
        WebSymbolAction obj = mapper.readValue(file, WebSymbolAction.class);

        assertTrue(obj instanceof CheckNodeAction);
        CheckNodeAction objAsAction = (CheckNodeAction) obj;
        assertEquals("#node", objAsAction.getNode().getSelector());
        assertEquals(WebElementLocator.Type.CSS, objAsAction.getNode().getType());
    }

    @Test
    public void shouldReturnOKIfNodeWasFound() {
        WebSiteConnector connector = mock(WebSiteConnector.class);
        WebElement element = mock(WebElement.class);
        given(connector.getElement(node)).willReturn(element);

        assertTrue(checkNode.execute(connector).isSuccess());
    }

    @Test
    public void shouldReturnFaliedIfNodeWasNotFound() {
        WebSiteConnector connector = mock(WebSiteConnector.class);
        when(connector.getElement(node)).thenThrow(new NoSuchElementException(""));

        assertFalse(checkNode.execute(connector).isSuccess());
    }

}
