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

/**
 * Component that displays a form for the browser action.
 * @type {{template: *, bindings: {action: string}, controllerAs: string, controller: browserActionFormComponent.controller}}
 */
export const browserActionFormComponent = {
  template: require('./browser-action-form.component.html'),
  bindings: {
    action: '=',
  },
  controllerAs: 'vm',
  controller: class BrowserActionFormComponent {

    public actions: string[] = ['REFRESH', 'RESTART'];
  }
};
