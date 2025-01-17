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

import {UiService} from '../../services/ui.service';

/**
 * The main component that is the start point of the application.
 * Should be used only once in the main index.html page.
 */
export const alexComponent = {
  template: require('./alex.component.html'),
  controllerAs: 'vm',
  controller: class AlexComponent {

    /**
     * Constructor.
     *
     * @param uiService
     */
    /* @ngInject */
    constructor(private uiService: UiService) {
    }

    get collapsed(): boolean {
      return this.uiService.store.sidebar.collapsed;
    }
  }
};
