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

/**
 * The component that handles the confirm modal dialog.
 */
export const confirmModalComponent = {
  template: require('./confirm-modal.component.html'),
  bindings: {
    dismiss: '&',
    close: '&',
    resolve: '='
  },
  controllerAs: 'vm',
  controller: class ConfirmModalComponent extends ModalComponent {

    /** The text to display. */
    public text: string = null;

    /** Constructor. */
    constructor() {
      super();
    }

    $onInit(): void {
      this.text = this.resolve.text;
    }
  },
};
