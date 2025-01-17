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

/** The model for user api results. */
export class User {

  /** The id of the user. */
  public id;

  /** The role of the user. */
  public role;

  /** The email of the user. */
  public email;

  /**
   * Constructor.
   *
   * @param obj The object to create a user from.
   */
  constructor(obj: any = {}) {
    this.id = obj.id;
    this.role = obj.role;
    this.email = obj.email;
  }
}
