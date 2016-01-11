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

import de.learnlib.alex.core.dao.LearnerResultDAO;
import de.learnlib.alex.core.dao.ProjectDAO;
import de.learnlib.alex.core.dao.SymbolDAO;
import de.learnlib.alex.core.entities.IdRevisionPair;
import de.learnlib.alex.core.entities.LearnerConfiguration;
import de.learnlib.alex.core.entities.LearnerResult;
import de.learnlib.alex.core.entities.LearnerResumeConfiguration;
import de.learnlib.alex.core.entities.LearnerStatus;
import de.learnlib.alex.core.entities.Project;
import de.learnlib.alex.core.entities.Symbol;
import de.learnlib.alex.core.entities.SymbolSet;
import de.learnlib.alex.core.entities.User;
import de.learnlib.alex.core.learner.Learner;
import de.learnlib.alex.exceptions.LearnerException;
import de.learnlib.alex.exceptions.NotFoundException;
import de.learnlib.alex.security.UserPrincipal;
import de.learnlib.alex.utils.ResourceErrorHandler;
import de.learnlib.alex.utils.ResponseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * REST API to manage the learning.
 * @resourcePath learner
 * @resourceDescription Operations about the learning
 */
@Path("/learner/")
@RolesAllowed({"REGISTERED"})
public class LearnerResource {

    /** Use the logger for the server part. */
    private static final Logger LOGGER = LogManager.getLogger("server");

    /** The {@link ProjectDAO} to use. */
    @Inject
    private ProjectDAO projectDAO;

    /** The {@link SymbolDAO} to use. */
    @Inject
    private SymbolDAO symbolDAO;

    /** The {@link LearnerResultDAO} to use. */
    @Inject
    private LearnerResultDAO learnerResultDAO;

    /** The {@link Learner learner} to use. */
    @Inject
    private Learner learner;

    /** The security context containing the user of the request. */
    @Context
    private SecurityContext securityContext;

    /**
     * Start the learning.
     *
     * @param projectId
     *         The project to learn.
     * @param configuration
     *         The configuration to customize the learning.
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.alex.core.entities.LearnerStatus
     * @errorResponse   400 bad request `de.learnlib.alex.utils.ResourceErrorHandler.RESTError
     * @errorResponse   404 not found   `de.learnlib.alex.utils.ResourceErrorHandler.RESTError
     */
    @POST
    @Path("/start/{project_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response start(@PathParam("project_id") long projectId, LearnerConfiguration configuration) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.trace("LearnerResource.start(" + projectId + ", " + configuration +  ") for user " + user + ".");

        try {
            Project project = projectDAO.getByID(user.getId(), projectId, ProjectDAO.EmbeddableFields.ALL);

            try {
                Symbol resetSymbol = symbolDAO.get(user, projectId, configuration.getResetSymbolAsIdRevisionPair());
                configuration.setResetSymbol(resetSymbol);
            } catch (NotFoundException e) { // Extra exception to emphasize that this is the reset symbol.
                throw new NotFoundException("Could not find the reset symbol!", e);
            }

            Set<Symbol> symbols = new HashSet<>(symbolDAO.getAll(user, projectId, new LinkedList<>(configuration.getSymbolsAsIdRevisionPairs()))); // TODO: remove new HashMap -> getAll should return a Set
            configuration.setSymbols(symbols);

            learner.start(user, project, configuration);
            LearnerStatus status = learner.getStatus(user);
            return Response.ok(status).build();
        } catch (IllegalStateException e) {
            LOGGER.info("tried to start the learning again.");
            LearnerStatus status = learner.getStatus(user);
            return Response.status(Status.NOT_MODIFIED).entity(status).build();
        } catch (IllegalArgumentException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.start", Status.BAD_REQUEST, e);
        } catch (NotFoundException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.start", Status.NOT_FOUND, e);
        }
    }

    /**
     * Resume the learning.
     * The project id and the test no must be the same as the very last started learn process.
     * The server must not be restarted
     *
     * @param projectId
     *         The project to learn.
     * @param testRunNo
     *         The number of the test run which should be resumed.
     * @param configuration
     *         The configuration to specify the settings for the next learning steps.
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.alex.core.entities.LearnerStatus
     */
    @POST
    @Path("/resume/{project_id}/{test_run}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resume(@PathParam("project_id") long projectId, @PathParam("test_run") long testRunNo,
                           LearnerResumeConfiguration configuration) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.trace("LearnerResource.resume(" + projectId + ", " + testRunNo + ", " + configuration +  ") "
                     + "for user " + user + ".");

        try {
            projectDAO.getByID(user.getId(), projectId); // check if project exists

            LearnerResult lastResult = learner.getResult(user);
            if (lastResult == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            if (lastResult.getProjectId() != projectId || lastResult.getTestNo() != testRunNo) {
                LOGGER.info("could not resume the learner of another project or with an wrong test run.");
                LearnerStatus status = learner.getStatus(user);
                return Response.status(Status.NOT_MODIFIED).entity(status).build();
            }

            learner.resume(user, configuration);
            LearnerStatus status = learner.getStatus(user);
            return Response.ok(status).build();
        } catch (IllegalStateException e) {
            LOGGER.info("tried to restart the learning while the learner is running.");
            LearnerStatus status = learner.getStatus(user);
            return Response.status(Status.NOT_MODIFIED).entity(status).build();
        } catch (IllegalArgumentException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.resume", Status.BAD_REQUEST, e);
        } catch (NotFoundException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.start", Status.NOT_FOUND, e);
        }
    }

    /**
     * Stop the learning after the current step.
     * This does not stop the learning immediately!
     * This will always return OK, even if there is nothing to stop.
     * To see if there is currently a learning process, the status like '/active' will be returned.
     *
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.alex.core.entities.LearnerStatus
     */
    @POST
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop() {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.trace("LearnerResource.stop() for user " + user + ".");

        if (learner.isActive(user)) {
            learner.stop(user); // Hammer Time
        } else {
            LOGGER.info("tried to stop the learning again.");
        }
        LearnerStatus status = learner.getStatus(user);
        return Response.ok(status).build();
    }

    /**
     * Is the learner active?
     *
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.alex.core.entities.LearnerStatus
     */
    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isActive() {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.trace("LearnerResource.isActive() for user " + user + ".");

        LearnerStatus status = learner.getStatus(user);
        return Response.ok(status).build();
    }

    /**
     * Get the parameters & (temporary) results of the learning.
     *
     * @return The information of the learning
     * @successResponse 200 OK
     * @responseType    de.learnlib.alex.core.entities.LearnerResult
     * @errorResponse   404 not found `de.learnlib.alex.utils.ResourceErrorHandler.RESTError
     */
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResult() {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.trace("LearnerResource.getResult() for user " + user + ".");

        LearnerResult resultInThread = learner.getResult(user);
        if (resultInThread == null) {
            IllegalStateException e = new IllegalStateException("No result was learned in this instance of ALEX.");
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.status", Status.NOT_FOUND, e);
        }

        try {
            learnerResultDAO.get(resultInThread.getUserId(), resultInThread.getProjectId(), resultInThread.getTestNo());
        } catch (NotFoundException nfe) {
            IllegalArgumentException e = new IllegalArgumentException("The last learned result was deleted.");
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.status", Status.NOT_FOUND, e);
        }

        return Response.ok(resultInThread).build();
    }

    /**
     * Get the output of a (possible) counter example.
     * This output is generated by executing the symbols on the SUL.
     *
     * @param projectId
     *         The project id the counter example takes place in.
     * @param symbolSet
     *         The symbol/ input set which will be executed.
     * @return The observed output of the given input set.
     * @successResponse 200 OK
     * @responseType    java.util.List<String>
     * @errorResponse   400 bad request `de.learnlib.alex.utils.ResourceErrorHandler.RESTError
     * @errorResponse   404 not found   `de.learnlib.alex.utils.ResourceErrorHandler.RESTError
     */
    @POST
    @Path("/outputs/{project_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readOutput(@PathParam("project_id") Long projectId, SymbolSet symbolSet) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.trace("LearnerResource.readOutput(" + projectId + ", " + symbolSet + ") for user " + user + ".");

        try {
            Project project = projectDAO.getByID(user.getId(), projectId);

            IdRevisionPair resetSymbolAsIdRevisionPair = symbolSet.getResetSymbolAsIdRevisionPair();
            if (resetSymbolAsIdRevisionPair == null) {
                throw new NotFoundException("No reset symbol specified!");
            }
            Symbol resetSymbol = symbolDAO.get(user, projectId, resetSymbolAsIdRevisionPair);
            symbolSet.setResetSymbol(resetSymbol);

            List<Symbol> symbols = loadSymbols(user, projectId, symbolSet.getSymbolsAsIdRevisionPairs());
            symbolSet.setSymbols(symbols);

            List<String> results = learner.readOutputs(user, project, resetSymbol, symbols);

            return ResponseHelper.renderList(results, Status.OK);
        } catch (NotFoundException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.readOutput", Status.NOT_FOUND, e);
        } catch (LearnerException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.readOutput", Status.BAD_REQUEST, e);
        }
    }

    // load all from SymbolDAO always orders the Symbols by ID
    private List<Symbol> loadSymbols(User user, Long projectId, List<IdRevisionPair> idRevisionPairs)
            throws NotFoundException {
        List<Symbol> symbols = new LinkedList<>();

        for (IdRevisionPair pair : idRevisionPairs) {
            Symbol symbol = symbolDAO.get(user, projectId, pair);
            symbols.add(symbol);
        }

        return symbols;
    }

}
