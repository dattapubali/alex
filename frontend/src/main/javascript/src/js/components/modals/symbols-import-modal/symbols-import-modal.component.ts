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

import {SymbolGroupUtils} from '../../../utils/symbol-group-utils';
import {ModalComponent} from '../modal.component';
import {SymbolResource} from '../../../services/resources/symbol-resource.service';
import {ProjectService} from '../../../services/project.service';
import {ToastService} from '../../../services/toast.service';
import {SymbolGroupResource} from '../../../services/resources/symbol-group-resource.service';
import {SymbolGroup} from '../../../entities/symbol-group';
import {Project} from '../../../entities/project';

/**
 * The component for the symbols import modal window.
 */
export const symbolsImportModalComponent = {
  template: require('./symbols-import-modal.component.html'),
  bindings: {
    dismiss: '&',
    close: '&',
    resolve: '='
  },
  controllerAs: 'vm',
  controller: class SymbolsImportModalComponent extends ModalComponent {

    /** The error message. */
    public errorMessage: string = null;

    /** The groups in the project. */
    public groups: SymbolGroup[] = [];

    /** The selected group where the symbols are imported to. */
    public selectedGroup: SymbolGroup = null;

    /** The data to import */
    public importData: any = null;

    /**
     * Constructor.
     *
     * @param symbolResource
     * @param projectService
     * @param toastService
     * @param symbolGroupResource
     */
    /* @ngInject */
    constructor(private symbolResource: SymbolResource,
                private projectService: ProjectService,
                private toastService: ToastService,
                private symbolGroupResource: SymbolGroupResource) {
      super();
    }

    $onInit(): void {
      this.groups = this.resolve.groups;
    }

    /**
     * Load the symbols from a JSON file.
     *
     * @param data The serialized symbols.
     */
    fileLoaded(data: string): void {
      const importData = JSON.parse(data);
      if (importData.type == null || ['symbolGroups', 'symbols'].indexOf(importData.type) === -1) {
        this.errorMessage = 'The file does not seem to contain symbols.';
      } else {
        this.importData = importData;
      }
    }

    /**
     * Import the symbols and close the modal window on success.
     */
    importSymbols(): void {
      this.errorMessage = null;

      if (this.importData.type === 'symbols') {
        const defaultGroup = SymbolGroupUtils.findDefaultGroup(this.groups);
        this.importData.symbols.forEach(symbol => {
          symbol.group = this.selectedGroup == null ? defaultGroup.id : this.selectedGroup.id;
        });

        this.symbolResource.createMany(this.project.id, this.importData.symbols)
          .then(createdSymbols => {
            this.toastService.success('The symbols have been imported');
            this.close({$value: {type: 'symbols', symbols: createdSymbols}});
          })
          .catch(err => {
            this.errorMessage = `The symbols could not be imported. ${err.data.message}`;
          });
      } else {
        this.symbolGroupResource.createMany(this.project.id, this.importData.symbolGroups)
          .then(createdGroups => {
            this.toastService.success('The symbols have been imported');
            this.close({$value: {type: 'symbolGroups', groups: createdGroups}});
          })
          .catch(err => {
            this.errorMessage = `The symbols could not be imported. ${err.data.message}`;
          });
      }
    }

    selectGroup(group: SymbolGroup): void {
      this.selectedGroup = group === this.selectedGroup ? null : group;
    }

    get project(): Project {
      return this.projectService.store.currentProject;
    }
  }
};
