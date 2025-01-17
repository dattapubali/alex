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

import {ModalComponent} from '../modal.component';
import {ProjectService} from '../../../services/project.service';
import {SymbolResource} from '../../../services/resources/symbol-resource.service';
import {TestService} from '../../../services/test.service';
import {Project} from '../../../entities/project';

export const testsImportModalComponent = {
  template: require('./tests-import-modal.component.html'),
  bindings: {
    close: '&',
    dismiss: '&',
    resolve: '='
  },
  controllerAs: 'vm',
  controller: class TestsImportModalComponent extends ModalComponent {

    /** The error message to display in case something fails. */
    public errorMessage: string = null;

    /** The tests to import. */
    public importData: any = null;

    /**
     * Constructor.
     *
     * @param projectService
     * @param symbolResource
     * @param testService
     */
    /* @ngInject */
    constructor(private projectService: ProjectService,
                private symbolResource: SymbolResource,
                private testService: TestService) {
      super();
    }

    /**
     * Callback from the file drop zone.
     *
     * @param data The contents of the imported file.
     */
    fileLoaded(data: string): void {
      this.errorMessage = null;
      try {
        const importData = JSON.parse(data);
        if (importData.type !== 'tests' || importData.tests == null || importData.tests.length === 0) {
          throw 'The file does not seem to contain any tests';
        }
        this.importData = importData;
      } catch (exception) {
        this.errorMessage = '' + exception;
      }
    }

    /** Import all test cases. */
    importTests(): void {
      this.errorMessage = null;

      if (this.importData.tests.length) {
        const parentId = this.resolve.parent.id;

        this.testService.importTests(this.project.id, this.importData.tests, parentId)
          .then(tests => this.close({$value: tests}))
          .catch(err => this.errorMessage = err.data.message);
      } else {
        this.errorMessage = 'There aren\'t any tests to import';
      }
    }

    get project(): Project {
      return this.projectService.store.currentProject;
    }
  }
};
