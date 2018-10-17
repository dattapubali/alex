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

export const testSuiteGenerationWidgetComponent = {
    template: require('./test-suite-generation-widget.component.html'),
    bindings: {
        result: '=',
        stepNo: '='
    },
    controllerAs: 'vm',
    controller: class TestSuiteGenerationWidgetComponent {

        /**
         * Constructor.
         *
         * @param learnResultResource
         * @param toastService
         */
        // @ngInject
        constructor(learnResultResource, toastService) {
            this.learnResultResource = learnResultResource;
            this.toastService = toastService;

            /**
             * The current learner result.
             * @type {?LearnResult}
             */
            this.result = null;

            /** The config to use for the generation. */
            this.config = {
                stepNo: 0,
                includeParameterValues: true,
                name: '',
                method: 'DT'
            };
        }

        $onInit() {
            this.config.stepNo = this.stepNo + 1;
            this.config.name = `TestNo ${this.result.testNo} (Generated)`;
        }

        generateTestSuite() {
            this.errorMessage = null;

            if (this.config.name.trim() === '') {
                this.toastService.danger(`The name may not be empty`);
                return;
            }

            this.learnResultResource.generateTestSuite(this.result.project, this.result.testNo, this.config)
                .then(() => {
                    this.toastService.success('The test suite has been generated.');
                })
                .catch(err => {
                    this.toastService.danger(`The test suite could not ne generated. ${err.data.message}`);
                });
        }
    }
};
