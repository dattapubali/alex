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

/**
 * Utilities for symbol groups.
 */
export class SymbolGroupUtils {

    /**
     * Get all symbols of multiple symbol groups.
     *
     * @param groups The groups to get the symbols from.
     * @return {AlphabetSymbol[]} All symbols in the groups.
     */
    static getSymbols(groups) {
        function getSymbols(group, symbols) {
            group.symbols.forEach(s => symbols.push(s));
            group.groups.forEach(g => getSymbols(g, symbols));
        }

        const symbols = [];
        groups.forEach(g => getSymbols(g, symbols));
        return symbols;
    }

    /**
     * Find the symbol group of a symbol.
     *
     * @param groups The groups to search in.
     * @param groupId The id of the group.
     * @return {SymbolGroup} The group that contains the symbol
     */
    static findGroupById(groups, groupId) {
        const root = {groups};

        function find(group) {
            if (group.id === groupId) {
                return group;
            }

            for (let i = 0; i < group.groups.length; i++) {
                const g = find(group.groups[i]);
                if (g != null) {
                    return g;
                }
            }
        }

        const res = find(root);
        return res != null ? res : null;
    }
}
