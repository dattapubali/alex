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

package de.learnlib.alex.data.entities.actions.RESTSymbolActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.data.entities.Symbol;
import de.learnlib.alex.learning.services.connectors.WebServiceConnector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CheckForTextActionTest {

    @Mock
    private WebServiceConnector connector;

    @Mock
    private Symbol symbol;

    private CheckTextRestAction c;

    @Before
    public void setUp() {
        c = new CheckTextRestAction();
        c.setSymbol(symbol);
        c.setValue("Hello World");
        c.setRegexp(false);
    }

    @Test
    public void testJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(c);
        CheckTextRestAction c2 = mapper.readValue(json, CheckTextRestAction.class);

        assertEquals(c.getValue(), c2.getValue());
        assertEquals(c.isRegexp(), c2.isRegexp());
    }

    @Test
    public void testJSONFile() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(getClass().getResource("/actions/restsymbolactions/CheckForTextTestData.json").toURI());
        RESTSymbolAction obj = mapper.readValue(file, RESTSymbolAction.class);

        assertTrue(obj instanceof CheckTextRestAction);
        CheckTextRestAction objAsAction = (CheckTextRestAction) obj;
        assertEquals("Lorem Ipsum", objAsAction.getValue());
        assertEquals(true, objAsAction.isRegexp());
    }

    @Test
    public void shouldReturnOkIfBodyContainsTheText() {
        given(connector.getBody()).willReturn("{\"awesome_field\": \"Lorem Ipsum. Hello World! Fooooobar\"}");

        ExecuteResult result = c.execute(connector);

        assertTrue(result.isSuccess());
    }

    @Test
    public void shouldReturnFailedIfBodyContainsNotTheText() {
        given(connector.getBody()).willReturn("{\"awesome_field\": \"Lorem Ipsum. Fooooobar\"}");

        ExecuteResult result = c.execute(connector);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldReturnOKIfTextWasFoundWithRegexp() {
        c.setValue("F[oO]+ B[a]+r");
        c.setRegexp(true);
        given(connector.getBody()).willReturn("FoO Baaaaar");

        assertTrue(c.execute(connector).isSuccess());
    }

    @Test
    public void shouldReturnFailedIfTextWasNotFoundWithRegexp() {
        c.setValue("F[oO]+ B[a]+r");
        c.setRegexp(true);
        given(connector.getBody()).willReturn("F BAr");

        assertFalse(c.execute(connector).isSuccess());
    }

}
