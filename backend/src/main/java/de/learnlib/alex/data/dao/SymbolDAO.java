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

package de.learnlib.alex.data.dao;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.common.exceptions.NotFoundException;
import de.learnlib.alex.data.entities.Project;
import de.learnlib.alex.data.entities.Symbol;
import de.learnlib.alex.data.entities.SymbolVisibilityLevel;
import org.apache.shiro.authz.UnauthorizedException;

import javax.validation.ValidationException;
import java.util.List;

/**
 * Interface to describe how Symbols are handled.
 */
public interface SymbolDAO {

    /**
     * Save the given symbol.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The ID of the project.
     * @param symbol
     *         The symbol to save.
     * @return The created symbol.
     * @throws ValidationException
     *         When the symbol was not valid.
     * @throws NotFoundException
     *         If a symbol could not be found.
     */
    Symbol create(User user, Long projectId, Symbol symbol) throws ValidationException, NotFoundException;

    /**
     * Save the given symbols.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The ID of the project.
     * @param symbols
     *         The symbols to save.
     * @return The created symbols.
     * @throws ValidationException
     *         When one the symbols was not valid. In this case all symbols are reverted and not saved.
     * @throws NotFoundException
     *         If a symbol could not be found.
     */
    List<Symbol> create(User user, Long projectId, List<Symbol> symbols) throws ValidationException, NotFoundException;

    /**
     * Get a list of specific symbols of a project.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The project the symbols should belong to.
     * @param ids
     *         A list of IDs to specify the expected symbols.
     * @return A list of symbols matching the project and list of IDs.
     * @throws NotFoundException
     *         If the project or one of the symbols could not be found.
     */
    List<Symbol> getByIds(User user, Long projectId, List<Long> ids) throws NotFoundException;

    /**
     * Get a list of symbols by their ids.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The project the symbols should belong to.
     * @param visibilityLevel
     *         The visibility level that returned symbols should have.
     * @param ids
     *         The ids of the symbols you want to get.
     * @return A list of symbols. Can be empty.
     * @throws NotFoundException
     *         If no Symbol was found.
     */
    List<Symbol> getByIds(User user, Long projectId, SymbolVisibilityLevel visibilityLevel,
            List<Long> ids)
            throws NotFoundException;

    /**
     * Get all symbols of a Project.
     *
     * @param user
     *         The user performing the action.
     * @param projectID
     *         The project the Symbols should belong to.
     * @param visibilityLevel
     *         Include symbols that are currently marked as hidden?
     * @return A list of symbols belonging to the Project. Can be empty.
     * @throws NotFoundException
     *         If the User or Project could not be found.
     */
    List<Symbol> getAll(User user, Long projectID, SymbolVisibilityLevel visibilityLevel)
            throws NotFoundException;

    /**
     * Get a List of Symbols that are within a specific Group within a Project.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The Project of the Symbols.
     * @param groupId
     *         The Group of the Symbols.
     * @return A List of Symbols belonging to the Group. Can be empty.
     * @throws NotFoundException
     *         If the User, Project or Group could not be found.
     */
    List<Symbol> getAll(User user, Long projectId, Long groupId)
            throws NotFoundException;

    /**
     * Get a List of Symbols that are withing a specific Group within a Project and have a specific visibility level.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The Project the Symbols.
     * @param groupId
     *         The Group of the Symbols.
     * @param visibilityLevel
     *         Only look for Symbols with the given visibility level.
     * @return A List of Symbols belonging to the Group with the given VisibilityLevel. Can be empty.
     * @throws NotFoundException
     *         If the User, Project or Group could not be found.
     */
    List<Symbol> getAll(User user, Long projectId, Long groupId, SymbolVisibilityLevel visibilityLevel)
            throws NotFoundException;

    /**
     * Get a Symbol by the user, project and a Pair of an ID.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The ID of the project the symbol belongs to.
     * @param id
     *         The ID of the Symbol in the project.
     * @return The Symbol.
     * @throws NotFoundException
     *         If the Symbol could not be found.
     */
    Symbol get(User user, Long projectId, Long id) throws NotFoundException;

    /**
     * Update a symbol.
     *
     * @param user
     *         The user performing the action.
     * @param symbol
     *         The symbol to update.
     * @param projectId
     *         The ID of the project.
     * @return The updated symbol.
     * @throws NotFoundException
     *         When the Symbol was not found.
     * @throws ValidationException
     *         When the Symbol was not valid.
     */
    Symbol update(User user, Long projectId, Symbol symbol) throws NotFoundException, ValidationException;

    /**
     * Update a list of Symbols.
     *
     * @param user
     *         The user performing the action.
     * @param symbols
     *         The symbol sto update.
     * @param projectId
     *         The ID of the project.
     * @return The list of updated symbols.
     * @throws NotFoundException
     *         When one of the Symbol was not found.
     * @throws ValidationException
     *         When one of the Symbol was not valid.
     */
    List<Symbol> update(User user, Long projectId, List<Symbol> symbols) throws NotFoundException, ValidationException;

    /**
     * Move a Symbol to a new Group.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The id of the project.
     * @param symbolId
     *         The id of the symbol to move.
     * @param newGroupId
     *         The new Group.
     * @return The group with the updated parent group.
     * @throws NotFoundException
     *         If the Symbol or the Group could not be found.
     */
    Symbol move(User user, Long projectId, Long symbolId, Long newGroupId) throws NotFoundException;

    /**
     * Moves a List of Symbols ot a new Group. If one Symbol failed to be move, no Symbol will be moved.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The id of the project.
     * @param symbolIds
     *         The ids of the symbols to move.
     * @param newGroupId
     *         The new Group.
     * @return The groups with the updated parent group.
     * @throws NotFoundException
     *         If at least one of the Symbols or if the Group could not be found.
     */
    List<Symbol> move(User user, Long projectId, List<Long> symbolIds, Long newGroupId) throws NotFoundException;

    /**
     * Mark a symbol as hidden.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The ID of the project the symbol belongs to.
     * @param ids
     *         The IDs of the symbols to hide.
     * @return The list of hidden symbols.
     * @throws NotFoundException
     *         When the Symbol was not found.
     */
    List<Symbol> hide(User user, Long projectId, List<Long> ids) throws NotFoundException;

    /**
     * Revive a symbol from the hidden state.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The ID of the project the symbol belongs to.
     * @param ids
     *         The ID of the symbols to show.
     * @throws NotFoundException
     *         When the Symbol was not found.
     */
    void show(User user, Long projectId, List<Long> ids) throws NotFoundException;

    /**
     * Delete a symbol permanently but only if there are no more references to it.
     *
     * @param user
     *         The user.
     * @param projectId
     *         The ID of the project.
     * @param symbolId
     *         The ID of the symbol to delete.
     * @throws NotFoundException
     *         If the project or symbol could not be found.
     */
    void delete(User user, Long projectId, Long symbolId) throws NotFoundException;

    /**
     * Permanently delete multiple symbols at once.
     *
     * @param user
     *          The user.
     * @param projectId
     *          The ID of the project.
     * @param symbolIds
     *          The IDs of the symbols to delete.
     * @throws NotFoundException
     *          If the project or a symbol could not be found.
     */
    void delete(User user, Long projectId, List<Long> symbolIds) throws NotFoundException;

    /**
     * Check if the user can access or modify a symbol.
     *
     * @param user
     *         The user.
     * @param project
     *         the project.
     * @param symbol
     *         The symbol.
     * @throws NotFoundException
     *         If one of the resources could not be found.
     * @throws UnauthorizedException
     *         If the user has no access to one of the resources.
     */
    void checkAccess(User user, Project project, Symbol symbol) throws NotFoundException, UnauthorizedException;
}
