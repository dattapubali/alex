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

package de.learnlib.alex.data.rest;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.auth.security.UserPrincipal;
import de.learnlib.alex.common.utils.ResourceErrorHandler;
import de.learnlib.alex.data.dao.SymbolGroupDAO;
import de.learnlib.alex.data.entities.SymbolGroup;
import de.learnlib.alex.data.events.SymbolGroupEvent;
import de.learnlib.alex.webhooks.services.WebhookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * REST API to manage groups.
 */
@Path("/projects/{project_id}/groups")
@RolesAllowed("REGISTERED")
public class SymbolGroupResource {

    private static final Logger LOGGER = LogManager.getLogger();

    /** Context information about the URI. */
    @Context
    private UriInfo uri;

    /** The SymbolGroupDAO to use. */
    @Inject
    private SymbolGroupDAO symbolGroupDAO;

    /** The security context containing the user of the request. */
    @Context
    private SecurityContext securityContext;

    /** The {@link WebhookService} to use. */
    @Inject
    private WebhookService webhookService;

    /**
     * Create a new group.
     *
     * @param projectId
     *         The ID of the project.
     * @param group
     *         The group to create.
     * @return On success the added group (enhanced with information from the DB); an error message on failure.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(@PathParam("project_id") long projectId, SymbolGroup group) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("createGroup({}, {}) for user {}.", projectId, group, user);

        group.setProjectId(projectId);
        symbolGroupDAO.create(user, group);

        LOGGER.traceExit(group);

        webhookService.fireEvent(user, new SymbolGroupEvent.Created(group));
        return Response.status(Response.Status.CREATED).entity(group).build();
    }

    /**
     * Create multiple symbol groups including symbols at once.
     *
     * @param projectId
     *         The ID of the project.
     * @param groups
     *         The groups to create.
     * @return The created groups.
     */
    @POST
    @Path("/batch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroups(@PathParam("project_id") Long projectId, List<SymbolGroup> groups) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("createGroups({}, {}) for user {}.", projectId, groups, user);

        final List<SymbolGroup> createdGroups = symbolGroupDAO.create(user, projectId, groups);
        webhookService.fireEvent(user, new SymbolGroupEvent.CreatedMany(createdGroups));
        LOGGER.traceExit(createdGroups);
        return Response.status(Response.Status.CREATED).entity(createdGroups).build();
    }

    /**
     * Get a list of all groups within on projects.
     *
     * @param projectId
     *         The ID of the project.
     * @param embed
     *         The properties to embed in the response.
     * @return All groups in a list. If the project contains no groups the list will be empty.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("project_id") long projectId, @QueryParam("embed") String embed) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("getAll({}, {}) for user {}.", projectId, embed, user);

        try {
            final SymbolGroupDAO.EmbeddableFields[] embeddableFields = parseEmbeddableFields(embed);
            final List<SymbolGroup> groups = symbolGroupDAO.getAll(user, projectId, embeddableFields);

            LOGGER.traceExit(groups);
            return Response.ok(groups).build();
        } catch (IllegalArgumentException e) {
            LOGGER.traceExit(e);
            return ResourceErrorHandler.createRESTErrorMessage("SymbolGroupResource.getAll", Response.Status.BAD_REQUEST, e);
        }
    }

    /**
     * Get a one group.
     *
     * @param projectId
     *         The ID of the project.
     * @param id
     *         The ID of the group within the project.
     * @param embed
     *         The properties to embed in the response.
     * @return The requested group.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("project_id") long projectId,
                        @PathParam("id") Long id,
                        @QueryParam("embed") String embed) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("get({}, {}, {}) for user {}.", projectId, id, embed, user);

        try {
            final SymbolGroupDAO.EmbeddableFields[] embeddableFields = parseEmbeddableFields(embed);
            final SymbolGroup group = symbolGroupDAO.get(user, projectId, id, embeddableFields);

            LOGGER.traceExit(group);
            return Response.ok(group).build();
        } catch (IllegalArgumentException e) {
            LOGGER.traceExit(e);
            return ResourceErrorHandler.createRESTErrorMessage("SymbolGroupResource.get", Response.Status.BAD_REQUEST, e);
        }
    }

    /**
     * Update a group.
     *
     * @param projectId
     *         The ID of the project.
     * @param id
     *         The ID of the group within the project.
     * @param group
     *         The new values
     * @return On success the updated group (enhanced with information from the DB).
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("project_id") long projectId, @PathParam("id") Long id, SymbolGroup group) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("update({}, {}, {}) for user {}.", projectId, id, group, user);

        symbolGroupDAO.update(user, group);

        LOGGER.traceExit(group);
        webhookService.fireEvent(user, new SymbolGroupEvent.Updated(group));
        return Response.ok(group).build();
    }

    /**
     * Moves a group to another group.
     *
     * @param projectId
     *         The id of the project.
     * @param groupId
     *         The id of the group to move.
     * @param group
     *         The group to move with the updated {@link SymbolGroup#parent} property. The parent property may be null
     *         to indicate that the group is moved to the upmost level.
     * @return 200 with the updated group.
     */
    @PUT
    @Path("/{groupId}/move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response move(@PathParam("project_id") Long projectId, @PathParam("groupId") Long groupId, SymbolGroup group) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("move({}, {}, {}) for user {}.", projectId, groupId, group, user);

        final SymbolGroup movedGroup = symbolGroupDAO.move(user, group);

        LOGGER.traceExit(movedGroup);
        webhookService.fireEvent(user, new SymbolGroupEvent.Moved(movedGroup));
        return Response.ok(movedGroup).build();
    }

    /**
     * Delete a group.
     *
     * @param projectId
     *         The ID of the project.
     * @param id
     *         The ID of the group within the project.
     * @return On success no content will be returned.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("project_id") long projectId, @PathParam("id") Long id) {
        User user = ((UserPrincipal) securityContext.getUserPrincipal()).getUser();
        LOGGER.traceEntry("delete({}, {}) for user {}.", projectId, id, user);

        try {
            symbolGroupDAO.delete(user, projectId, id);
            LOGGER.traceExit("Group {} deleted.", id);
            webhookService.fireEvent(user, new SymbolGroupEvent.Deleted(id));
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            LOGGER.traceExit(e);
            return ResourceErrorHandler.createRESTErrorMessage("SymbolGroupResource.update", Response.Status.BAD_REQUEST, e);
        }
    }

    private SymbolGroupDAO.EmbeddableFields[] parseEmbeddableFields(String embed) throws IllegalArgumentException {
        if (embed == null) {
            return new SymbolGroupDAO.EmbeddableFields[0];
        }

        String[] fields = embed.split(",");

        SymbolGroupDAO.EmbeddableFields[] embedFields = new SymbolGroupDAO.EmbeddableFields[fields.length];
        for (int i = 0; i < fields.length; i++) {
            embedFields[i] = SymbolGroupDAO.EmbeddableFields.fromString(fields[i]);
        }

        return embedFields;
    }
}
