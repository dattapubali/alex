(function () {
    'use strict';

    angular
        .module('ALEX.actions')
        .factory('ClickLinkByTextWebAction', ClickLinkByTextWebActionFactory);

    ClickLinkByTextWebActionFactory.$inject = ['AbstractAction', 'actionGroupTypes', 'actionTypes'];

    /**
     * The factory for ClickLinkByTextWebAction
     *
     * @param AbstractAction
     * @param actionGroupTypes
     * @param actionTypes
     * @returns {ClickLinkByTextWebAction}
     * @constructor
     */
    function ClickLinkByTextWebActionFactory(AbstractAction, actionGroupTypes, actionTypes) {

        /**
         * Clicks on a link with a specific text value
         *
         * @param {string} value - The text of the link
         * @constructor
         */
        function ClickLinkByTextWebAction(value) {
            AbstractAction.call(this, actionTypes[actionGroupTypes.WEB].CLICK_LINK_BY_TEXT);
            this.value = value || null;
        }

        ClickLinkByTextWebAction.prototype = Object.create(AbstractAction.prototype);

        /**
         * @returns {string}
         */
        ClickLinkByTextWebAction.prototype.toString = function () {
            return 'Click on link with text "' + this.value + '"';
        };

        return ClickLinkByTextWebAction;
    }
}());