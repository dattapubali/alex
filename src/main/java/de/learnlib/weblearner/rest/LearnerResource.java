package de.learnlib.weblearner.rest;

import de.learnlib.weblearner.dao.ProjectDAO;
import de.learnlib.weblearner.dao.SymbolDAO;
import de.learnlib.weblearner.entities.LearnerConfiguration;
import de.learnlib.weblearner.entities.LearnerResult;
import de.learnlib.weblearner.entities.LearnerResumeConfiguration;
import de.learnlib.weblearner.entities.LearnerStatus;
import de.learnlib.weblearner.entities.Project;
import de.learnlib.weblearner.entities.Symbol;
import de.learnlib.weblearner.learner.Learner;
import de.learnlib.weblearner.utils.ResourceErrorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

/**
 * REST API to manage the learning.
 * @resourcePath learner
 * @resourceDescription Operations about the learning
 */
@Path("/learner/")
public class LearnerResource {

    /** Use the logger for the server part. */
    private static final Logger LOGGER = LogManager.getLogger("server");

    /** The {@link ProjectDAO} to use. */
    @Inject
    private ProjectDAO projectDAO;

    /** The {@link SymbolDAO} to use. */
    @Inject
    private SymbolDAO symbolDAO;

    /** The {@link Learner learner} to use. */
    @Inject
    private Learner learner;

    /**
     * Start the learning.
     *
     * @param projectId
     *         The project to learn.
     * @param configuration
     *         The configuration to customize the learning.
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.weblearner.entities.LearnerStatus
     */
    @POST
    @Path("/start/{project_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response start(@PathParam("project_id") long projectId, LearnerConfiguration configuration) {
        LearnerStatus status = new LearnerStatus(learner);
        try {
            List<Symbol<?>> symbols = symbolDAO.getAll(projectId, configuration.getSymbols());
            Project project = projectDAO.getByID(projectId, "all");

            learner.start(project, configuration, symbols.toArray(new Symbol[symbols.size()]));
            return Response.ok(status).build();
        } catch (IllegalStateException e) {
            LOGGER.info("tried to start the learning again.");
            return Response.status(Status.NOT_MODIFIED).entity(status).build();
        } catch (IllegalArgumentException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.start", Status.BAD_REQUEST, e);
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
     * @responseType de.learnlib.weblearner.entities.LearnerStatus
     */
    @POST
    @Path("/resume/{project_id}/{test_run}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resume(@PathParam("project_id") long projectId, @PathParam("test_run") long testRunNo,
                           LearnerResumeConfiguration configuration) {
        LearnerStatus status = new LearnerStatus(learner);
        try {
            LearnerResult lastResult = learner.getResult();
            if (lastResult.getProjectId() != projectId || lastResult.getTestNo() != testRunNo) {
                LOGGER.info("could not resume the learner of another project or with an wrong test run.");
                return Response.status(Status.NOT_MODIFIED).entity(status).build();
            }

            learner.resume(configuration);
            return Response.ok(status).build();
        } catch (IllegalStateException e) {
            LOGGER.info("tried to restart the learning while the learner is running.");
            return Response.status(Status.NOT_MODIFIED).entity(status).build();
        } catch (IllegalArgumentException e) {
            return ResourceErrorHandler.createRESTErrorMessage("LearnerResource.resume", Status.BAD_REQUEST, e);
        }
    }

    /**
     * Stop the learning.
     * 
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.weblearner.entities.LearnerStatus
     */
    @GET
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop() {
        LearnerStatus status = new LearnerStatus(learner);
        if (learner.isActive()) {
            learner.stop(); // Hammer Time
            return Response.ok(status).build();
        } else {
            LOGGER.info("tried to stop the learning again.");
            return Response.status(Status.NOT_MODIFIED).entity(status).build();
        }
    }

    /**
     * Is the learner active?
     *
     * @return The status of the current learn process.
     * @successResponse 200 OK
     * @responseType de.learnlib.weblearner.entities.LearnerStatus
     */
    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isActive() {
        LearnerStatus status = new LearnerStatus(learner);
        return Response.ok(status).build();
    }

    /**
     * Get the parameters & (temporary) results of the learning.
     *
     * @return The information of the learning
     * @successResponse 200 OK
     * @responseType de.learnlib.weblearner.entities.LearnerResult
     */
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResult() {
        return Response.ok(learner.getResult()).build();
    }

}
