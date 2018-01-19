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

import uniqueId from 'lodash/uniqueId';
import {AlphabetSymbol} from '../../../entities/alphabet-symbol';

/**
 * The component for the symbols import modal window.
 */
export class SymbolsImportModalComponent {

    /**
     * Constructor.
     *
     * @param {SymbolResource} SymbolResource
     * @param {SessionService} SessionService
     * @param {ToastService} ToastService
     */
    // @ngInject
    constructor(SymbolResource, SessionService, ToastService) {
        this.SymbolResource = SymbolResource;
        this.ToastService = ToastService;

        /**
         * The current project.
         * @type {Project}
         */
        this.project = SessionService.getProject();

        /**
         * The list of symbols to import.
         * @type {AlphabetSymbol[]}
         */
        this.symbols = [];

        this.symbolToEdit = null;
    }

    /**
     * Load the symbols from a JSON file.
     *
     * @param {string} data - The serialized symbols.
     */
    fileLoaded(data) {
        this.symbols = JSON.parse(data).map(s => {
            const symbol = new AlphabetSymbol(s);
            symbol.id = uniqueId();
            return symbol;
        });
    }

    /**
     * Import the symbols and close the modal window on success.
     */
    importSymbols() {
        const symbolsToImport = JSON.parse(JSON.stringify(this.symbols));
        symbolsToImport.forEach(s => delete s.id);

        this.SymbolResource.createMany(this.project.id, symbolsToImport)
            .then(symbols => {
                this.ToastService.success('Symbols imported');
                this.close({$value: symbols});
            })
            .catch(() => {
                this.ToastService.danger('Import failed. It seems at least on symbol already exists');
            });
    }

    /**
     * Updates the name of the symbol to edit.
     *
     * @param {AlphabetSymbol} updatedSymbol - The updated symbol.
     */
    updateSymbol(updatedSymbol) {

        // check if the name already exists
        let symbol = this.symbols.find(s => s.name === updatedSymbol.name);
        if (symbol && symbol.id !== updatedSymbol.id) {
            this.ToastService.danger(`The symbol with the name "${updatedSymbol.name}" already exists.`);
            return;
        }

        // update name
        symbol = this.symbols.find(s => s.id === updatedSymbol.id);
        symbol.name = updatedSymbol.name;
        this.symbolToEdit = null;
    }
}

export const symbolsImportModalComponent = {
    template: require('./symbols-import-modal.component.html'),
    bindings: {
        dismiss: '&',
        close: '&'
    },
    controller: SymbolsImportModalComponent,
    controllerAs: 'vm'
};