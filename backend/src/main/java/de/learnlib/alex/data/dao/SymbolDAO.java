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

package de.learnlib.alex.data.dao;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.common.exceptions.NotFoundException;
import de.learnlib.alex.data.entities.Symbol;
import de.learnlib.alex.data.entities.SymbolVisibilityLevel;

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
     * @param symbol
     *         The symbol to save.
     * @throws ValidationException
     *         When the symbol was not valid.
     */
    void create(User user, Symbol symbol) throws ValidationException, NotFoundException;

    /**
     * Save the given symbols.
     *
     * @param user
     *         The user performing the action.
     * @param symbols
     *         The symbols to save.
     * @throws ValidationException
     *         When one the symbols was not valid.
     *         In this case all symbols are reverted and not saved.
     */
    void create(User user, List<Symbol> symbols) throws ValidationException, NotFoundException;

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
     * @return The updated symbol.
     * @throws IllegalArgumentException
     *         If an old revision is used.
     * @throws NotFoundException
     *         When the Symbol was not found.
     * @throws ValidationException
     *         When the Symbol was not valid.
     */
    Symbol update(User user, Symbol symbol) throws IllegalArgumentException, NotFoundException, ValidationException;

    /**
     * Update a list of Symbols.
     *
     * @param user
     *         The user performing the action.
     * @param symbols
     *         The symbol sto update.
     * @return The list of updated symbols.
     * @throws IllegalArgumentException
     *         If an old revision is used.
     * @throws NotFoundException
     *         When one of the Symbol was not found.
     * @throws ValidationException
     *         When one of the Symbol was not valid.
     */
    List<Symbol> update(User user, List<Symbol> symbols)
            throws IllegalArgumentException, NotFoundException, ValidationException;

    /**
     * Move a Symbol to a new Group.
     *
     * @param user
     *         The user performing the action.
     * @param symbol
     *         The Symbol to move.
     * @param newGroupId
     *         The new Group.
     * @throws NotFoundException
     *         If the Symbol or the Group could not be found.
     */
    void move(User user, Symbol symbol, Long newGroupId) throws NotFoundException;

    /**
     * Moves a List of Symbols ot a new Group.
     * If one Symbol failed to be move, no Symbol will be moved.
     *
     * @param user
     *         The user performing the action.
     * @param symbols
     *         The Symbol to move.
     * @param newGroupId
     *         The new Group.
     * @throws NotFoundException
     *         If at least one of the Symbols or if the Group could not be found.
     */
    void move(User user, List<Symbol> symbols, Long newGroupId) throws NotFoundException;

    /**
     * Mark a symbol as hidden.
     *
     * @param user
     *         The user performing the action.
     * @param projectId
     *         The ID of the project the symbol belongs to.
     * @param ids
     *         The IDs of the symbols to hide.
     * @throws NotFoundException
     *         When the Symbol was not found.
     */
    void hide(User user, Long projectId, List<Long> ids) throws NotFoundException;

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

}
