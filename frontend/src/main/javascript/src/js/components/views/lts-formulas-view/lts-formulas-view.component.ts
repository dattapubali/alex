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

import {Selectable} from '../../../utils/selectable';
import {ToastService} from '../../../services/toast.service';
import {ProjectService} from '../../../services/project.service';
import {LtsFormulaService} from '../../../services/lts-formula.service';
import {Project} from '../../../entities/project';

export const ltsFormulasViewComponent = {
  template: require('./lts-formulas-view.component.html'),
  controllerAs: 'vm',
  controller: class LtsFormulasViewComponent {

    public selectedFormulas: Selectable<any>;

    /**
     * Constructor.
     *
     * @param toastService
     * @param projectService
     * @param ltsFormulaService
     */
    /* @ngInject */
    constructor(private toastService: ToastService,
                private projectService: ProjectService,
                private ltsFormulaService: LtsFormulaService) {

      this.selectedFormulas = new Selectable(null, 'id');
    }

    $onInit(): void {
      this.ltsFormulaService.load(this.project.id)
        .then(() => this.selectedFormulas = new Selectable(this.formulas, 'id'));
    }

    deleteSelectedFormulas(): void {
      const selectedFormulas = this.selectedFormulas.getSelected();
      if (selectedFormulas.length === 0) {
        this.toastService.info('You have to select at least one formula.');
      } else {
        this.ltsFormulaService.deleteMany(selectedFormulas)
          .then(() => {
            this.toastService.success('The formulas have been deleted.');
            this.selectedFormulas.unselectMany(selectedFormulas);
          })
          .catch(err => this.toastService.danger(`The formulas could not be deleted. ${err.data.message}`));
      }
    }

    editSelectedFormula(): void {
      const selectedFormulas = this.selectedFormulas.getSelected();
      if (selectedFormulas.length === 1) {
        this.ltsFormulaService.openEditModal(selectedFormulas[0]);
      }
    }

    get project(): Project {
      return this.projectService.store.currentProject;
    }

    get formulas(): any[] {
      return this.ltsFormulaService.store.ltsFormulas;
    }
  }
};
