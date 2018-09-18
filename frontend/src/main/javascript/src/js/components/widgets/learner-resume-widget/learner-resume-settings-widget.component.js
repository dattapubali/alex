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

import {eqOracleType} from '../../../constants';
import {ParametrizedSymbol} from '../../../entities/parametrized-symbol';

/**
 * The directive for the widget of the sidebar where learn resume configurations can be edited. Should be included
 * into a <div widget></div> directive for visual appeal.
 *
 * Expects an attribute 'learnConfiguration' attached to the element whose value should be a LearnConfiguration
 * object.
 */
class LearnerResumeSettingsWidgetComponent {

    /** Constructor. */
    constructor() {
        this.eqOracleTypes = eqOracleType;

        /**
         * The selected symbol to add.
         * @type {?AlphabetSymbol}
         */
        this.selectedSymbol = null;
    }

    addSelectedSymbol() {
        this.configuration.symbolsToAdd.push(ParametrizedSymbol.fromSymbol(this.selectedSymbol));
        this.selectedSymbol = null;
    }

    /**
     * Creates a new eq oracle object from the selected type and assigns it to the configuration.
     */
    setEqOracle(eqOracle) {
        this.configuration.eqOracle = eqOracle;
    }
}

export const learnerResumeSettingsWidgetComponent = {
    template: require('./learner-resume-settings-widget.component.html'),
    bindings: {
        symbols: '=',
        configuration: '=',
        project: '<',
        result: '<'
    },
    controller: LearnerResumeSettingsWidgetComponent,
    controllerAs: 'vm'
};
