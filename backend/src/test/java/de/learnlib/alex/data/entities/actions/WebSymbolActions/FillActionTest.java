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

package de.learnlib.alex.data.entities.actions.WebSymbolActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.data.entities.Symbol;
import de.learnlib.alex.data.entities.WebElementLocator;
import de.learnlib.alex.learning.services.connectors.WebSiteConnector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static de.learnlib.alex.data.entities.ExecuteResult.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FillActionTest {

    private FillAction f;

    private WebElementLocator node;

    @Before
    public void setUp() {
        Symbol symbol = new Symbol();

        node = new WebElementLocator();
        node.setSelector("#node");
        node.setType(WebElementLocator.Type.CSS);

        f = new FillAction();
        f.setSymbol(symbol);
        f.setNode(node);
        f.setValue("Lorem Ipsum");
    }

    @Test
    public void testJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(f);
        FillAction f2 = mapper.readValue(json, FillAction.class);

        assertEquals(f.getNode(), f2.getNode());
    }

    @Test
    public void testJSONFile() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(getClass().getResource("/actions/websymbolactions/FillTestData.json").toURI());
        WebSymbolAction obj = mapper.readValue(file, WebSymbolAction.class);

        assertTrue(obj instanceof FillAction);
        FillAction objAsAction = (FillAction) obj;
        assertEquals("#input", objAsAction.getNode().getSelector());
        assertEquals("Lorem Ipsum", objAsAction.getValue());
    }

    @Test
    public void shouldReturnOKIfNodeCouldBeFilled() {
        WebSiteConnector connector = mock(WebSiteConnector.class);
        WebElement element = mock(WebElement.class);
        given(connector.getElement(node)).willReturn(element);

        assertEquals(OK, f.execute(connector));
        verify(element).sendKeys(f.getValue());
    }

    @Test
    public void shouldReturnFaliedIfNodeCouldNotBeFilled() {
        WebSiteConnector connector = mock(WebSiteConnector.class);
        when(connector.getElement(node)).thenThrow(new NoSuchElementException(""));

        assertEquals(ExecuteResult.FAILED, f.execute(connector));
    }

}