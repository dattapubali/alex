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

import {eqOracleType, learnAlgorithm, webBrowser} from './constants';

/**
 * The filter to format a EQ type constant to something more readable.
 */
export function formatEqOracle(): (string) => string {
  return type => {
    switch (type) {
      case eqOracleType.RANDOM:
        return 'Random Word';
      case  eqOracleType.COMPLETE:
        return 'Complete';
      case  eqOracleType.SAMPLE:
        return 'Sample';
      case  eqOracleType.WMETHOD:
        return 'W-Method';
      case eqOracleType.WP_METHOD:
        return 'Wp-Method';
      case  eqOracleType.HYPOTHESIS:
        return 'Hypothesis';
      case eqOracleType.TEST_SUITE:
        return 'Test suite';
      default:
        return type;
    }
  };
}

/**
 * Sort tests. First all test suites, then all test cases. Both alphabetically.
 */
export function sortTests() {
  return tests => {
    let testSuites = tests.filter(t => t.type === 'suite');
    let testCases = tests.filter(t => t.type === 'case');

    const compare = (a, b) => a.name > b.name ? 1 : b.name > a.name ? -1 : 0;

    testSuites.sort(compare);
    testCases.sort(compare);

    return testSuites.concat(testCases);
  };
}

/**
 * Formats the web browser dictionary.
 */
export function formatWebBrowser(): (string) => string {
  return browser => {
    switch (browser) {
      case webBrowser.HTML_UNIT:
        return 'HTML Unit';
      case webBrowser.CHROME:
        return 'Chrome';
      case webBrowser.FIREFOX:
        return 'Firefox';
      case webBrowser.EDGE:
        return 'Edge';
      case webBrowser.SAFARI:
        return 'Safari';
      case webBrowser.REMOTE:
        return 'Remote driver';
      case webBrowser.IE:
        return 'Internet Explorer';
      default:
        return browser;
    }
  };
}

/**
 * The filter to format a learn algorithm name to something more readable.
 */
export function formatAlgorithm(): (string) => string {
  return name => {
    switch (name) {
      case learnAlgorithm.LSTAR:
        return 'L*';
      case learnAlgorithm.DHC:
        return 'DHC';
      case learnAlgorithm.TTT:
        return 'TTT';
      case learnAlgorithm.DT:
        return 'Discrimination Tree';
      case learnAlgorithm.KEARNS_VAZIRANI:
        return 'Kearns Vazirani';
      default:
        return name;
    }
  };
}

/**
 * Transform an upper case string to a normal one.
 * Example: RANDOM_WORD -> Random word.
 */
export function normalizeUpperCase(): (string) => string {
  return str => {
    let res = str.split('_').map(part => part.toLowerCase()).join(' ');
    return res.charAt(0).toUpperCase() + res.slice(1);
  };
}

/**
 * The filter takes a number representing milliseconds and formats it to [h] [min] s.
 */
export function formatMilliseconds(): (number) => string {
  return ms => {
    let hours, minutes, seconds;

    if (ms >= 3600000) {
      hours = Math.floor(ms / 3600000);
      ms = ms % 3600000;
      minutes = Math.floor(ms / 60000);
      seconds = Math.floor((ms % 60000) / 1000);
      return hours + 'h ' + minutes + 'min ' + seconds + 's';
    } else if (ms >= 60000) {
      minutes = Math.floor(ms / 60000);
      return minutes + 'min ' + Math.floor((ms % 60000) / 1000) + 's';
    } else {
      seconds = Math.floor(ms / 1000);
      return seconds + 's ' + (ms % 1000) + 'ms';
    }
  };
}
