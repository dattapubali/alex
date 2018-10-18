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

import {HtmlElementPickerService} from '../../../../../services/html-element-picker.service';
import {ClickLinkByTextWebAction} from '../../../../../entities/actions/web/click-link-by-text-action';

export const clickLinkByTextActionFormComponent = {
  template: require('./click-link-by-text-action-form.component.html'),
  bindings: {
    action: '='
  },
  controllerAs: 'vm',
  controller: class ClickLinkByTextActionFormComponent {

    public action: ClickLinkByTextWebAction;

    /**
     * Constructor.
     *
     * @param htmlElementPickerService
     */
    // @ngInject
    constructor(private htmlElementPickerService: HtmlElementPickerService) {
    }

    /** Opens the element picker. */
    openPicker(): void {
      this.htmlElementPickerService.open()
        .then(data => {
          this.action.value = data.textContent;
        });
    }
  }
};
