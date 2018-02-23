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
 * Form groups for a project.
 * @type {{templateUrl: string, bindings: {project: string, form: string}, controllerAs: string, controller: projectFormGroups.controller}}
 */
export const projectFormGroupsComponent = {
    template: require('./project-form-groups.component.html'),
    bindings: {
        project: '=',
        form: '='
    },
    controllerAs: 'vm',
    controller: class {

        /** Constructor. */
        constructor() {
            this.mirrorUrl = 'http://';
        }

        /** Add a mirror URL. */
        addMirrorUrl() {
            if (this.mirrorUrl == null
                || this.mirrorUrl.trim() === ''
                || this.mirrorUrl === 'http://'
                || this.mirrorUrl === 'https://') {
                return;
            }

            this.project.mirrorUrls.push(this.mirrorUrl);
            this.mirrorUrl = 'http://';
        }

        /**
         * Removes a mirror URL.
         * @param {number} index The index of the url to remove.
         */
        removeMirrorUrl(index) {
            this.project.mirrorUrls.splice(index, 1);
        }
    }
};
