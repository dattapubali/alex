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

package de.learnlib.alex.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.learnlib.alex.ALEXTestApplication;
import de.learnlib.alex.FakeAuthenticationFilter;
import de.learnlib.alex.core.dao.CounterDAO;
import de.learnlib.alex.core.dao.FileDAO;
import de.learnlib.alex.core.dao.LearnerResultDAO;
import de.learnlib.alex.core.dao.ProjectDAO;
import de.learnlib.alex.core.dao.SymbolDAO;
import de.learnlib.alex.core.dao.SymbolGroupDAO;
import de.learnlib.alex.core.dao.UserDAO;
import de.learnlib.alex.core.entities.LearnerResult;
import de.learnlib.alex.core.entities.Project;
import de.learnlib.alex.core.entities.User;
import de.learnlib.alex.core.entities.learnlibproxies.AlphabetProxy;
import de.learnlib.alex.core.learner.Learner;
import de.learnlib.alex.exceptions.NotFoundException;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.SimpleAlphabet;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ValidationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

public class LearnerResultResourceTest extends JerseyTest {

    private static final Long USER_TEST_ID = FakeAuthenticationFilter.FAKE_USER_ID;
    private static final long PROJECT_ID = 1L;
    private static final long RESULT_ID = 10L;
    private static final int TEST_RESULT_AMOUNT = 10;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private CounterDAO counterDAO;

    @Mock
    private SymbolGroupDAO symbolGroupDAO;

    @Mock
    private SymbolDAO symbolDAO;

    @Mock
    private LearnerResultDAO learnerResultDAO;

    @Mock
    private FileDAO fileDAO;

    @Mock
    private Learner learner;

    @Mock
    private User user;

    private Project project;

    @Override
    protected Application configure() {
        MockitoAnnotations.initMocks(this);

        return new ALEXTestApplication(userDAO, projectDAO, counterDAO, symbolGroupDAO, symbolDAO,
                                       learnerResultDAO, fileDAO, learner, LearnerResultResource.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        project = new Project();
        project.setId(PROJECT_ID);

        user = new User();
        user.setId(USER_TEST_ID);
        given(userDAO.getById(user.getId())).willReturn(user);
    }

    @Test
    public void shouldReturnAllFinalResults() throws NotFoundException, JsonProcessingException {
        List<LearnerResult> results = new LinkedList<>();
        for (long i = 0; i < TEST_RESULT_AMOUNT; i++) {
            Alphabet<String> sigma = new SimpleAlphabet<>();
            sigma.add("0");
            sigma.add("1");

            LearnerResult learnerResult = new LearnerResult();
            learnerResult.setUser(user);
            learnerResult.setProject(project);
            learnerResult.setTestNo(i);
            learnerResult.setStepNo(0L);
            learnerResult.setSigma(AlphabetProxy.createFrom(sigma));

            results.add(learnerResult);
        }
        given(learnerResultDAO.getAll(user.getId(), PROJECT_ID)).willReturn(results);

        Response response = target("/projects/" + PROJECT_ID + "/results").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(String.valueOf(TEST_RESULT_AMOUNT), response.getHeaderString("X-Total-Count"));

        List<LearnerResult> resultsInResponse = response.readEntity(new GenericType<List<LearnerResult>>() { });
        assertArrayEquals(results.toArray(), resultsInResponse.toArray());
    }

    @Test
    public void ensureThatGettingAllFinalResultsReturns404IfTheProjectIdIsInvalid() throws NotFoundException {
        given(learnerResultDAO.getAll(user.getId(), PROJECT_ID)).willThrow(NotFoundException.class);

        Response response = target("/projects/" + PROJECT_ID + "/results").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldReturnAllResultSteps() throws NotFoundException, JsonProcessingException {
        List<LearnerResult> results = new LinkedList<>();
        for (long i = 0; i < TEST_RESULT_AMOUNT; i++) {
            Alphabet<String> sigma = new SimpleAlphabet<>();
            sigma.add("0");
            sigma.add("1");

            LearnerResult learnerResult = new LearnerResult();
            learnerResult.setUser(user);
            learnerResult.setProject(project);
            learnerResult.setTestNo(RESULT_ID);
            learnerResult.setStepNo(i);
            learnerResult.setSigma(AlphabetProxy.createFrom(sigma));

            results.add(learnerResult);
        }
        given(learnerResultDAO.getAll(USER_TEST_ID, PROJECT_ID, RESULT_ID)).willReturn(results);

        String path = "/projects/" + PROJECT_ID + "/results/" + RESULT_ID + "/complete";
        Response response = target(path).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(String.valueOf(TEST_RESULT_AMOUNT), response.getHeaderString("X-Total-Count"));

        List<LearnerResult> resultsInResponse = response.readEntity(new GenericType<List<LearnerResult>>() { });
        assertArrayEquals(results.toArray(), resultsInResponse.toArray());
    }

    @Test
    public void ensureThatGettingAllResultsOfOneRunReturns404IfTheProjectIdIsInvalid() throws NotFoundException {
        given(learnerResultDAO.getAll(USER_TEST_ID, PROJECT_ID, RESULT_ID)).willThrow(NotFoundException.class);

        String path = "/projects/" + PROJECT_ID + "/results/" + RESULT_ID + "/complete";
        Response response = target(path).request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldGetAllStepsOfMultipleTestRuns() throws NotFoundException, JsonProcessingException {
        List<Long> ids = new LinkedList<>();
        List<List<LearnerResult>> results = new LinkedList<>();
        for (long i = 0; i < TEST_RESULT_AMOUNT; i++) {
            List<LearnerResult> tmpList = new LinkedList<>();

            for (Long j = 0L; j < TEST_RESULT_AMOUNT; j++) {
                Alphabet<String> sigma = new SimpleAlphabet<>();
                sigma.add("0");
                sigma.add("1");

                LearnerResult learnerResult = new LearnerResult();
                learnerResult.setProject(project);
                learnerResult.setTestNo(RESULT_ID + i);
                learnerResult.setStepNo(j);
                learnerResult.setSigma(AlphabetProxy.createFrom(sigma));

                tmpList.add(learnerResult);
            }
            ids.add(RESULT_ID + i);
            results.add(tmpList);
        }
        given(learnerResultDAO.getAll(user.getId(), PROJECT_ID, ids)).willReturn(results);

        StringBuilder idsAsString = new StringBuilder();
        for (Long id : ids) {
            idsAsString.append(String.valueOf(id));
            idsAsString.append(',');
        }
        idsAsString.setLength(idsAsString.length() - 1); // remove last ','

        String path = "/projects/" + PROJECT_ID + "/results/" + idsAsString + "/complete";
        Response response = target(path).request().get();

        String jsonInResponse = response.readEntity(String.class);
        System.out.println(jsonInResponse);
    }

    @Test
    public void shouldGetTheFinalResultOfOneTestRun() throws NotFoundException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // given
        Alphabet<String> sigma = new SimpleAlphabet<>();
        sigma.add("0");
        sigma.add("1");

        LearnerResult learnerResult = new LearnerResult();
        learnerResult.setUser(user);
        learnerResult.setProject(project);
        learnerResult.setTestNo(RESULT_ID);
        learnerResult.setStepNo(0L);
        learnerResult.setSigma(AlphabetProxy.createFrom(sigma));

        given(learnerResultDAO.get(USER_TEST_ID, PROJECT_ID, RESULT_ID)).willReturn(learnerResult);

        // when
        Response response = target("/projects/" + PROJECT_ID + "/results/" + RESULT_ID).request().get();

        // then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(learnerResult), response.readEntity(String.class));
    }

    @Test
    public void shouldDeleteAllStepsOfATestRun() throws NotFoundException {
        Response response = target("/projects/" + PROJECT_ID + "/results/" + RESULT_ID).request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        verify(learnerResultDAO).delete(user, PROJECT_ID, RESULT_ID);
    }

    @Test
    public void shouldDeleteMultipleTestResults() throws NotFoundException {
        Response response = target("/projects/" + PROJECT_ID + "/results/" + RESULT_ID + "," + (RESULT_ID + 1))
                                .request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        verify(learnerResultDAO).delete(user, PROJECT_ID, RESULT_ID, RESULT_ID + 1);
    }

    @Test
    public void shouldNotCrashIfNoTestNoToDeleteIsSpecified() {
        Response response = target("/projects/" + PROJECT_ID + "/results/").request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldReturnAnErrorIfYouTryToDeleteAnInvalidTestNo() throws NotFoundException {
        willThrow(NotFoundException.class).given(learnerResultDAO).delete(user, PROJECT_ID, RESULT_ID, RESULT_ID + 1);

        Response response = target("/projects/" + PROJECT_ID + "/results/" + RESULT_ID + "," +  (RESULT_ID + 1))
                            .request().delete();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldReturnAnErrorIfYouTryToDeleteAnActiveTestNo() throws NotFoundException {
        willThrow(ValidationException.class).given(learnerResultDAO).delete(user, PROJECT_ID, RESULT_ID, RESULT_ID + 1);

        Response response = target("/projects/" + PROJECT_ID + "/results/" + RESULT_ID + "," +  (RESULT_ID + 1))
                            .request().delete();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void ensureThatNoTestNumberToDeleteIsHandledProperly() throws NotFoundException {
        willThrow(NotFoundException.class).given(learnerResultDAO).delete(user, PROJECT_ID, RESULT_ID, RESULT_ID + 1);

        Response response = target("/projects/" + PROJECT_ID + "/results/,,,,")
                            .request().delete();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void ensureThatANotValidTestNumberStringOnDeletionIsHandledProperly() throws NotFoundException {
        willThrow(NotFoundException.class).given(learnerResultDAO).delete(user, PROJECT_ID, RESULT_ID, RESULT_ID + 1);

        Response response = target("/projects/" + PROJECT_ID + "/results/foobar")
                            .request().delete();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
