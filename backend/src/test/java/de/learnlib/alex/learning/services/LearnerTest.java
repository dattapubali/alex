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

package de.learnlib.alex.learning.services;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.common.exceptions.NotFoundException;
import de.learnlib.alex.data.dao.SymbolDAO;
import de.learnlib.alex.data.entities.ExecuteResult;
import de.learnlib.alex.data.entities.ParameterizedSymbol;
import de.learnlib.alex.data.entities.Project;
import de.learnlib.alex.learning.dao.LearnerResultDAO;
import de.learnlib.alex.learning.entities.LearnerResult;
import de.learnlib.alex.learning.entities.LearnerResultStep;
import de.learnlib.alex.learning.entities.LearnerStartConfiguration;
import de.learnlib.alex.learning.entities.ReadOutputConfig;
import de.learnlib.alex.learning.entities.SymbolSet;
import de.learnlib.alex.learning.entities.webdrivers.AbstractWebDriverConfig;
import de.learnlib.alex.learning.entities.webdrivers.HtmlUnitDriverConfig;
import de.learnlib.alex.learning.services.connectors.ConnectorContextHandler;
import de.learnlib.alex.learning.services.connectors.PreparedConnectorContextHandlerFactory;
import de.learnlib.alex.learning.services.connectors.ConnectorManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class LearnerTest {

    private static final long   PROJECT_ID    = 42L;
    private static final int    SYMBOL_AMOUNT = 5;

    @Mock
    private PreparedConnectorContextHandlerFactory contextHandlerFactory;

    @Mock
    private ConnectorContextHandler contextHandler;

    @Mock
    private User user;

    @Mock
    private Project project;

    @Mock
    private LearnerStartConfiguration learnerConfiguration;

    @Mock
    private SymbolDAO symbolDAO;

    @Mock
    private LearnerResultDAO learnerResultDAO;

    @Mock
    private AbstractLearnerThread learnerThread;

    private Learner learner;

    @Before
    public void setUp() {

        AbstractWebDriverConfig driverConfig = new HtmlUnitDriverConfig();

        given(learnerConfiguration.getDriverConfig()).willReturn(driverConfig);
//        given(contextHandlerFactory.create(user, project, driverConfig))
//                .willReturn(contextHandler);
//        given(learnerThreadFactory.createThread(any(LearnerResult.class), any(ConnectorContextHandler.class)))
//                .willReturn(learnerThread);
//
//        learner = new Learner(symbolDAO, learnerResultDAO, algorithmService,
//                              contextHandlerFactory, learnerThreadFactory);
    }

    @Test
    public void shouldStartAThread() throws NotFoundException {
        System.out.println(user);
        System.out.println(project);
        System.out.println(learnerConfiguration);

        learner.start(user, project, learnerConfiguration);
    }

    @Test
    @Ignore
    public void shouldResumeAThread() throws NotFoundException {
        learner.start(user, project, learnerConfiguration);
        given(learnerThread.isFinished()).willReturn(true);

//        learner.resume(user, learnerConfiguration, 0);
    }

    @Test
    public void shouldStopAThread() throws NotFoundException {
        learner.stop(-1L);
    }

    @Test
    public void shouldCorrectlyTestIfTheUserHasAnActiveThread() throws NotFoundException {
        given(learnerThread.isFinished()).willReturn(false);
        assertFalse(learner.isActive(PROJECT_ID));

        learner.start(user, project, learnerConfiguration);

        boolean active = learner.isActive(PROJECT_ID);
        assertTrue(active);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldOnlyStartTheThreadOnce() throws NotFoundException {
        given(symbolDAO.getByIds(any(User.class), any(Long.class), any(List.class))).willReturn(new ArrayList());
        given(learnerResultDAO.createStep(any(LearnerResult.class), any(LearnerStartConfiguration.class)))
                .willReturn(new LearnerResultStep());
        given(learnerThread.isFinished()).willReturn(false);

        learner.start(user, project, learnerConfiguration);

        learner.start(user, project, learnerConfiguration); // should fail
    }

    @Test
    public void shouldReadTheCorrectOutputOfSomeSymbols() {
        ParameterizedSymbol resetSymbol = mock(ParameterizedSymbol.class);
        ParameterizedSymbol postSymbol = mock(ParameterizedSymbol.class);
        //
        List<ParameterizedSymbol> symbols = new ArrayList<>();
        for (int i = 0; i < SYMBOL_AMOUNT; i++) {
            ParameterizedSymbol symbol = mock(ParameterizedSymbol.class);
            given(symbol.execute(any(ConnectorManager.class))).willReturn(new ExecuteResult(true));
            symbols.add(symbol);
        }
        //
        ConnectorContextHandler ctxHandler = mock(ConnectorContextHandler.class);
        // TODO
        //        learner.setContextHandler(ctxHandler);
        //
        ConnectorManager connectorManager = mock(ConnectorManager.class);
        given(ctxHandler.createContext()).willReturn(connectorManager);

        final ReadOutputConfig config = new ReadOutputConfig();
        config.setDriverConfig(new HtmlUnitDriverConfig());
        config.setSymbols(new SymbolSet(resetSymbol, symbols, postSymbol));

        List<ExecuteResult> outputs = learner.readOutputs(user, project, config);

        assertEquals(symbols.size(), outputs.size());
        assertTrue("at least one output was not OK", outputs.stream().allMatch(r -> r.getOutput().equals("OK")));
        verify(connectorManager).dispose();
    }

}
