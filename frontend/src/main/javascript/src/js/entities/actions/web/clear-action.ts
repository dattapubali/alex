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
 * Remove all inputs from an element.
 */
export class ClearWebAction extends Action {

  /** The selector of the element. */
  public node: any;

  /**
   * Constructor.
   *
   * @param obj The object to create the action from.
   */
  constructor(obj: any = {}) {
    super(actionType.WEB_CLEAR, obj);

    this.node = obj.node || {selector: '', type: 'CSS'};
  }

  /**
   * A string representation of the action.
   *
   * @returns {string}
   */
  toString(): string {
    return `Clear input "${this.node.selector}"`;
  }
}
