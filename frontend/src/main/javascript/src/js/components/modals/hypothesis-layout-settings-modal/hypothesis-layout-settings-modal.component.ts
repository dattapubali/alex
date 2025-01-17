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

interface LayoutProperties {
  nodesep: number,
  edgesep: number,
  ranksep: number
}

/**
 * The controller that handles the modal dialog for changing the layout settings of a hypothesis.
 */
export const hypothesisLayoutSettingsModalComponent = {
  template: require('./hypothesis-layout-settings-modal.component.html'),
  bindings: {
    dismiss: '&',
    close: '&',
    resolve: '='
  },
  controllerAs: 'vm',
  controller: class HypothesisLayoutSettingsModalComponent extends ModalComponent {

    /** The default layout settings for a hypothesis. */
    private defaultLayoutProperties: LayoutProperties = {
      nodesep: 50,
      edgesep: 25,
      ranksep: 50
    };

    /** The layout properties to apply on the model. */
    public layoutSettings: LayoutProperties;

    /** Constructor.*/
    constructor() {
      super();
    }

    $onInit(): void {
      const settings: LayoutProperties = this.resolve.layoutSettings;
      this.layoutSettings = settings != null ? settings : this.defaultLayoutProperties;
    }

    /**
     * Closes the modal window and passes the updated layout settings.
     */
    update(): void {
      this.close({$value: this.layoutSettings});
    }

    /**
     * Sets the layout settings to its default values.
     */
    defaultLayoutSettings(): void {
      this.layoutSettings = this.defaultLayoutProperties;
    }
  },
};
