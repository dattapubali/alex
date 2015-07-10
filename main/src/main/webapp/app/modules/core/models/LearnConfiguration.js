(function () {
    'use strict';

    angular
        .module('ALEX.core')
        .factory('LearnConfiguration', LearnConfigurationFactory);

    LearnConfigurationFactory.$inject = ['learnAlgorithms', 'EqOracle'];

    /**
     * The factory for creating LearnConfiguration objects that are used to configure a learning process
     *
     * @param learnAlgorithms - The dictionary for learning algorithms
     * @param EqOracle - The factory for EQ-oracles
     * @returns {LearnConfiguration}
     * @constructor
     */
    function LearnConfigurationFactory(learnAlgorithms, EqOracle) {

        /**
         * The model for a learning configuration
         * @constructor
         */
        function LearnConfiguration() {
            this.symbols = [];
            this.maxAmountOfStepsToLearn = 0;
            this.eqOracle = new EqOracle.Random(1, 10, 20);
            this.algorithm = learnAlgorithms.TTT;
            this.resetSymbol = null;
            this.comment = null;
        }

        /**
         * Remove properties from a learning configuration that aren't needed for resuming a learning process
         *
         * @returns {LearnConfiguration} - The reduced learning configuration
         */
        LearnConfiguration.prototype.toLearnResumeConfiguration = function () {
            delete this.symbols;
            delete this.algorithm;
            delete this.resetSymbol;
            delete this.comment;
            return this;
        };

        /**
         * Sets the reset symbols for the configuration
         *
         * @param {Symbol} symbol
         */
        LearnConfiguration.prototype.setResetSymbol = function (symbol) {
            this.resetSymbol = {
                id: symbol.id,
                revision: symbol.revision
            };
        };

        /**
         * Adds a symbol to the configuration that should be used to learn an application
         *
         * @param {Symbol} symbol
         */
        LearnConfiguration.prototype.addSymbol = function (symbol) {
            this.symbols.push({
                id: symbol.id,
                revision: symbol.revision
            });
        };

        /**
         * Creates an instance of LearnConfiguration from an object
         *
         * @param {Object} data - The object that is used to create a LearnConfiguration from
         * @returns {LearnConfiguration}
         */
        LearnConfiguration.build = function (data) {
            return angular.extend(new LearnConfiguration(), {
                comment: data.comment,
                symbols: data.symbols,
                maxAmountOfStepsToLearn: data.maxAmountOfStepsToLearn,
                algorithm: data.algorithm,
                eqOracle: EqOracle.build(data.eqOracle),
                resetSymbol: data.resetSymbol
            });
        };

        return LearnConfiguration;
    }
}());