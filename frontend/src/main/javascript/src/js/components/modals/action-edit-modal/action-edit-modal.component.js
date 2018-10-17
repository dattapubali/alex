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
 * The controller for the modal dialog that handles the editing of an action.
 */
export class ActionEditModalComponent {

    /**
     * Constructor.
     *
     * @param actionService
     * @param symbolResource
     * @param projectService
     */
    // @ngInject
    constructor(actionService, symbolResource, projectService) {
        this.actionService = actionService;

        /**
         * The copy of the action that should be edited.
         * @type {Object}
         */
        this.action = null;

        /**
         * The list of symbols.
         * @type {Symbol[]}
         */
        this.symbols = [];

        // fetch all symbols so that symbols have access to it
        const project = projectService.store.currentProject;
        symbolResource.getAll(project.id).then(symbols => {
            this.symbols = symbols;
        });
    }

    $onInit() {
        this.action = this.resolve.modalData.action;
    }

    /**
     * Close the modal dialog and pass the updated action to the handle that called it.
     */
    updateAction() {
        this.close({$value: this.action});
    }
}

export const actionEditModalComponent = {
    template: require('./action-edit-modal.component.html'),
    bindings: {
        dismiss: '&',
        close: '&',
        resolve: '='
    },
    controller: ActionEditModalComponent,
    controllerAs: 'vm',
};
