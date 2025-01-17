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

import {actionType} from '../../../constants';
import {Action} from '../action';

/**
 * Selects an entry from a select box.
 */
export class SelectWebAction extends Action {

  /** The CSS selector of an element. */
  public node: any;

  /** The value of the select input that should be selected. */
  public value: string;

  /** The type the option is selected by {'TEXT', 'VALUE', 'INDEX'}. */
  public selectBy: string;

  /**
   * Constructor.
   *
   * @param obj The object to create the action from.
   */
  constructor(obj: any = {}) {
    super(actionType.WEB_SELECT, obj);

    this.node = obj.node || {selector: '', type: 'CSS'};
    this.value = obj.value || '';
    this.selectBy = obj.selectBy || 'TEXT';
  }

  toString(): string {
    return `Select value "${this.value}" from select input "${this.node.selector}"`;
  }
}
