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

import {TestCaseStep} from '../../../../entities/test-case-step';
import {SymbolGroup} from '../../../../entities/symbol-group';
import {AlphabetSymbol} from '../../../../entities/alphabet-symbol';

/**
 * The component for selecting a symbol that is executed before and after a test.
 */
export const prePostTestCaseStepComponent = {
  template: require('./pre-post-test-case-step.component.html'),
  bindings: {
    steps: '=',
    groups: '=',
    text: '@'
  },
  controllerAs: 'vm',
  controller: class PrePostTestCaseSymbol {

    public steps: any[];
    public groups: SymbolGroup[];
    public text: string;

    /**
     * Select a symbol from the symbol group tree.
     *
     * @param symbol The symbol to use for the step.
     */
    selectSymbol(symbol: AlphabetSymbol): void {
      this.steps = [TestCaseStep.fromSymbol(symbol)];
    }

    /** Removes the pre or post step. */
    removeStep(): void {
      this.steps = [];
    }
  }
};
