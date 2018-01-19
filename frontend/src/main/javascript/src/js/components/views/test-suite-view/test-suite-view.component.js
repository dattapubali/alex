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

import {webBrowser} from '../../../constants';
import {DriverConfigService} from '../../../services/driver-config.service';
import {DateUtils} from '../../../utils/date-utils';

export const testSuiteViewComponent = {
    template: require('./test-suite-view.component.html'),
    bindings: {
        testSuite: '='
    },
    controllerAs: 'vm',

    /**
     * The controller of the view.
     */
    controller: class {

        /**
         * Constructor.
         *
         * @param {$state} $state
         * @param {SymbolGroupResource} SymbolGroupResource
         * @param {SessionService} SessionService
         * @param {LearnerResource} LearnerResource
         * @param {ToastService} ToastService
         * @param {TestResource} TestResource
         * @param {PromptService} PromptService
         * @param $uibModal
         * @param {SettingsResource} SettingsResource
         * @param {DownloadService} DownloadService
         * @param {TestService} TestService
         * @param {ClipboardService} ClipboardService
         */
        // @ngInject
        constructor($state, SymbolGroupResource, SessionService, LearnerResource, ToastService, TestResource,
                    PromptService, $uibModal, SettingsResource, DownloadService, TestService, ClipboardService) {
            this.$state = $state;
            this.LearnerResource = LearnerResource;
            this.ToastService = ToastService;
            this.TestResource = TestResource;
            this.PromptService = PromptService;
            this.$uibModal = $uibModal;
            this.DownloadService = DownloadService;
            this.TestService = TestService;
            this.ClipboardService = ClipboardService;

            /**
             * The current project.
             * @type {Project}
             */
            this.project = SessionService.getProject();

            /**
             * The test suite.
             * @type {object}
             */
            this.testSuite = null;

            /**
             * The results of the test execution.
             * @type {object}
             */
            this.results = {};

            /**
             * If tests are being executed.
             * @type {boolean}
             */
            this.isExecuting = false;

            /**
             * If the execution finished.
             * @type {boolean}
             */
            this.finished = false;

            /**
             * The driver configuration.
             * @type {object}
             */
            this.driverConfig = DriverConfigService.createFromName(webBrowser.HTML_UNIT);

            SettingsResource.getSupportedWebDrivers()
                .then(data => this.driverConfig = DriverConfigService.createFromName(data.defaultWebDriver))
                .catch(console.log);

            SymbolGroupResource.getAll(this.project.id, true)
                .then(groups => this.groups = groups)
                .catch(console.log);
        }

        createTestSuite() {
            this.PromptService.prompt('Enter a name for the test suite.')
                .then(name => {
                    const testSuite = {
                        type: 'suite',
                        name: name,
                        project: this.project.id,
                        parent: this.testSuite.id,
                        tests: []
                    };
                    this.TestResource.create(testSuite)
                        .then(data => {
                            this.ToastService.success(`The test suite "${testSuite.name}" has been created.`);
                            this.testSuite.tests.push(data);
                        })
                        .catch(err => this.ToastService.danger('The test suite could not be created. ' + err.data.message));
                });
        }

        createTestCase() {
            this.PromptService.prompt('Enter a name for the test case.')
                .then(name => {
                    const testCase = {
                        type: 'case',
                        name: name,
                        project: this.project.id,
                        parent: this.testSuite.id,
                        symbols: [],
                        variables: {},
                        shouldPass: true
                    };
                    this.TestResource.create(testCase)
                        .then(data => {
                            this.ToastService.success(`The test case "${testCase.name}" has been created.`);
                            this.testSuite.tests.push(data);
                        })
                        .catch(err => this.ToastService.danger('The test suite could not be created. ' + err.data.message));
                });
        }

        editTest(test) {
            this.PromptService.prompt(`Update the name for the test.`, test.name)
                .then(name => {
                    if (name === test.name) {
                        return;
                    }

                    const testToUpdate = JSON.parse(JSON.stringify(test));
                    testToUpdate.name = name;
                    delete testToUpdate._selected;
                    if (testToUpdate.type === 'suite') {
                        testToUpdate.testIds = test.tests.map(t => t.id);
                        delete testToUpdate.tests;
                    } else {
                        testToUpdate.symbols = test.symbols.map(s => s.id);
                    }

                    this.TestResource.update(testToUpdate)
                        .then(() => {
                            this.ToastService.success('The name has been updated.');
                            test.name = name;
                        })
                        .catch(err => this.ToastService.danger(`The test ${test.type} could not be updated. ${err.data.message}`));
                });
        }

        reset() {
            this.results = {};
            this.finished = false;
        }

        deleteTest(test) {
            this.reset();

            this.TestResource.remove(test)
                .then(() => {
                    this.ToastService.success(`The test ${test.type} has been deleted.`);
                    const i = this.testSuite.tests.findIndex(t => t.id === test.id);
                    if (i > -1) this.testSuite.tests.splice(i, 1);
                })
                .catch(err => this.ToastService.danger(`The test ${test.type} could not be deleted. ${err.data.message}`));
        }

        deleteSelected() {
            if (this.testSuite.tests.findIndex(t => t._selected) === -1) {
                this.ToastService.info('You have to select at least one test case or test suite.');
                return;
            }

            this.reset();

            const selected = this.testSuite.tests.filter(t => t._selected);
            this.TestResource.removeMany(this.project.id, selected)
                .then(() => {
                    this.ToastService.success('The tests have been deleted.');
                    selected.forEach(test => {
                        const i = this.testSuite.tests.findIndex(t => t.id === test.id);
                        if (i > -1) this.testSuite.tests.splice(i, 1);
                    });
                })
                .catch(err => this.ToastService.danger(`Deleting the tests failed. ${err.data.message}`));
        }

        stopTestExecution() {
            this.ToastService.info('The execution stops after the current test.');
            this.isExecuting = false;
            this.finished = true;
        }

        executeSelected() {
            if (this.testSuite.tests.findIndex(t => t._selected) === -1) {
                this.ToastService.info('You have to select at least one test case or test suite.');
                return;
            }

            this.reset();
            this.isExecuting = true;
            const selected = this.testSuite.tests.filter(t => t._selected);

            const next = (test) => {
                if (!this.isExecuting) {
                    this.ToastService.success('Finished executing all tests.');
                    return;
                }

                this.TestResource.execute(test, this.driverConfig)
                    .then(data => {
                        for (const k in data) {
                            this.results[k] = data[k];
                        }

                        // this.results[test.id] = data;
                        if (selected.length) {
                            next(selected.shift());
                        } else {
                            this.ToastService.success('Finished executing all tests.');
                            this.isExecuting = false;
                            this.finished = true;
                        }
                    })
                    .catch(console.log);
            };
            next(selected.shift());
        }

        openBrowserConfigModal() {
            this.$uibModal.open({
                component: 'browserConfigModal',
                resolve: {
                    modalData: () => {
                        return {
                            configuration: JSON.parse(JSON.stringify(this.driverConfig))
                        };
                    }
                }
            }).result.then(data => {
                this.ToastService.success('The settings have been updated.');
                this.driverConfig = data;
            });
        }

        /**
         * Downloads the tests as JSON file.
         */
        exportSelectedTests() {
            let tests = this.testSuite.tests.filter(t => t._selected);
            if (!tests.length) {
                this.ToastService.info('You have to select at least one test.');
            } else {
                tests = JSON.parse(JSON.stringify(tests));
                tests = this.TestService.exportTests(tests);

                const name = `tests-${this.testSuite.name}-${DateUtils.YYYYMMDD()}`;
                this.PromptService.prompt('Enter a name for the file', name).then(name => {
                    this.DownloadService.downloadObject(tests, name);
                    this.ToastService.success('The tests have been exported');
                });
            }
        }

        importTests() {
            this.TestService.openImportModal(this.testSuite)
                .then((tests) => {
                    this.ToastService.success('Tests have been imported.');
                    tests.forEach(t => {
                        t.type = t.tests ? 'suite' : 'case';
                        this.testSuite.tests.push(t);
                    });
                });
        }

        copyTests() {
            let tests = this.testSuite.tests.filter((t) => t._selected);

            if (tests.length > 0) {
                tests = JSON.parse(JSON.stringify(tests));
                tests = this.TestService.exportTests(tests);
                this.ClipboardService.copy('tests', tests);
                this.ToastService.info('Tests copied to clipboard.');
            } else {
                this.ToastService.info('You have to select at least one test');
            }
        }

        pasteTests() {
            const tests = this.ClipboardService.paste('tests');
            if (tests !== null) {
                this.TestService.importTests(this.project.id, tests, this.testSuite.id)
                    .then((importedTests) => {
                        importedTests.forEach((t) => {
                            t.type = t.tests ? 'suite' : 'case';
                            this.testSuite.tests.push(t);
                        });
                        this.ToastService.success(`Pasted tests from clipboard.`);
                    })
                    .catch((err) => {
                        this.ToastService.danger(`Could not paste tests in this suite. ${err.data.message}`);
                    });
            } else {
                this.ToastService.info('There are not tests in the clipboard.');
            }
        }
    }
};