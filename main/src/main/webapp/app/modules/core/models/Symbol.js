(function () {
    'use strict';

    angular
        .module('ALEX.core')
        .factory('Symbol', SymbolModel);

    SymbolModel.$inject = ['_', 'ActionBuilder'];

    /**
     * The factory for the symbol model.
     *
     * @param _ - Lodash
     * @param ActionBuilder - The factory that creates Actions
     * @returns {Symbol} - The symbol model
     * @constructor
     */
    function SymbolModel(_, ActionBuilder) {

        /**
         * The symbol model
         *
         * @param {string} name - The name of the symbol
         * @param {string} abbreviation - The abbreviation of the symbol
         * @constructor
         */
        function Symbol(name, abbreviation) {
            this.name = name || null;
            this.abbreviation = abbreviation || null;
            this.actions = [];
        }

        /**
         * Builds a symbol instance from an object
         *
         * @param {Object} data - The data the symbol instance should be build from
         * @returns {Symbol} - The symbol instance
         */
        Symbol.build = function (data) {
            return angular.extend(new Symbol(
                data.name,
                data.abbreviation
            ), {
                actions: data.actions ? ActionBuilder.createFromObjects(data.actions) : [],
                id: data.id,
                revision: data.revision,
                project: data.project,
                hidden: data.hidden,
                group: data.group
            });
        };

        /**
         * Creates [an] instance[s] of Symbol from a HTTP response
         *
         * @param {Object} response - The response object from the API
         * @returns {Symbol|Symbol[]} - The Symbol[s]
         */
        Symbol.transformApiResponse = function (response) {
            if (angular.isArray(response.data)) {
                if (response.data.length > 0) {
                    return _.map(response.data, Symbol.build);
                } else {
                    return [];
                }
            } else {
                return Symbol.build(response.data);
            }
        };

        /**
         * Counts the number of actions that are not skipped by the learner
         *
         * @returns {number} - The amount of enabled actions
         */
        Symbol.prototype.countEnabledActions = function () {
            for (var i = 0, c = 0; i < this.actions.length; i++) {
                c += this.actions[i].disabled ? 0 : 1;
            }
            return c;
        };

        /**
         * Get the id and revision of the symbol as a pair
         *
         * @returns {{id: number, revision: number}}
         */
        Symbol.prototype.getIdRevisionPair = function () {
            return {
                id: this.id,
                revision: this.revision
            }
        };

        return Symbol;
    }
}());