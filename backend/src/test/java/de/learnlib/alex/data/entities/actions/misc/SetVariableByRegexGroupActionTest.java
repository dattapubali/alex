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

package de.learnlib.alex.data.entities.actions.misc;

import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.data.entities.Project;
import de.learnlib.alex.data.entities.Symbol;
import de.learnlib.alex.learning.services.connectors.ConnectorManager;
import de.learnlib.alex.learning.services.connectors.VariableStoreConnector;
import de.learnlib.alex.learning.services.connectors.WebSiteConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

public class SetVariableByRegexGroupActionTest {

    private static final String VARIABLE_NAME = "var";

    private SetVariableByRegexGroupAction action;

    private ConnectorManager connectors;

    private WebSiteConnector webSiteConnector;

    private VariableStoreConnector variableStore;

    private WebDriver wd;

    @Before
    public void before() {
        this.webSiteConnector = Mockito.mock(WebSiteConnector.class);
        this.variableStore = new VariableStoreConnector();

        this.connectors = Mockito.mock(ConnectorManager.class);
        Mockito.when(connectors.getConnector(WebSiteConnector.class)).thenReturn(this.webSiteConnector);
        Mockito.when(connectors.getConnector(VariableStoreConnector.class)).thenReturn(this.variableStore);

        this.wd = Mockito.mock(WebDriver.class);
        Mockito.when(this.webSiteConnector.getDriver()).thenReturn(this.wd);

        final Project project = new Project(1L);

        final Symbol symbol = new Symbol();
        symbol.setId(1L);
        symbol.setProject(project);

        this.action = new SetVariableByRegexGroupAction();
        this.action.setName(VARIABLE_NAME);
        this.action.setSymbol(symbol);
        this.action.setRegex(".*?([0-9]+).*?");

        Mockito.when(wd.getPageSource()).thenReturn("a: 1, b: 2");
    }

    @Test
    public void shouldSetTheValueCorrectly() {
        action.setNthMatch(1);
        action.setMthGroup(1);

        final ExecuteResult result1 = action.executeAction(connectors);
        Assert.assertTrue(result1.isSuccess());
        Assert.assertEquals("1", variableStore.get(VARIABLE_NAME));

        action.setRegex(".*?([0-9]+).*?");
        action.setNthMatch(2);
        action.setMthGroup(1);

        final ExecuteResult result2 = action.executeAction(connectors);
        Assert.assertTrue(result2.isSuccess());
        Assert.assertEquals("2", variableStore.get(VARIABLE_NAME));
    }

    @Test
    public void shouldFailIfMatchIsOutOfBounds() {
        action.setNthMatch(5);
        action.setMthGroup(1);

        final ExecuteResult result = action.executeAction(connectors);
        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(variableStore.getStore().get(VARIABLE_NAME));
    }

    @Test
    public void shouldFailIfGroupIsOutOfBounds() {
        action.setNthMatch(1);
        action.setMthGroup(4);

        final ExecuteResult result = action.executeAction(connectors);
        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(variableStore.getStore().get(VARIABLE_NAME));
    }

    @Test
    public void shouldFailIfRegexDoesNotMatchBounds() {
        Mockito.when(wd.getPageSource()).thenReturn("abc");

        action.setNthMatch(1);
        action.setMthGroup(1);

        final ExecuteResult result = action.executeAction(connectors);
        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(variableStore.getStore().get(VARIABLE_NAME));
    }
}
