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
 * Component for displaying a bootstrap panel with a title and a specific content.
 */
export const widgetComponent = {
  template: require('./widget.component.html'),
  transclude: true,
  bindings: {
    title: '@'
  },
  controllerAs: 'vm',
  controller: class WidgetComponent {

    /** The title to display in the widget. */
    public title: string;
  }
};
