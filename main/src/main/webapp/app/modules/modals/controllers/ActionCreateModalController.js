(function () {
    'use strict';

    angular
        .module('ALEX.modals')
        .controller('ActionCreateModalController', ActionCreateModalController);

    ActionCreateModalController.$inject = [
        '$scope', '$modalInstance', 'modalData', 'actionTypes', 'actionGroupTypes', 'ActionBuilder', 'SymbolResource', 'SessionService'
    ];

    /**
     * The controller for the modal dialog that handles the creation of a new action.
     *
     * The template can be found at 'views/modals/action-create-modal.html'.
     *
     * @param $scope
     * @param $modalInstance
     * @param modalData
     * @param actionTypes
     * @param actionGroupTypes
     * @param ActionBuilder
     * @param SymbolResource
     * @param Session
     * @constructor
     */
    function ActionCreateModalController($scope, $modalInstance, modalData, actionTypes, actionGroupTypes, ActionBuilder, SymbolResource, Session) {

        var project = Session.project.get();

        /**
         * The constant for action group types
         * @type {Object}
         */
        $scope.actionGroupTypes = actionGroupTypes;

        /**
         * The constant for action type names
         * @type {Object}
         */
        $scope.actionTypes = actionTypes;

        /**
         * The model for the new action
         * @type {null|Object}
         */
        $scope.action = null;

        /**
         * All symbols of the project
         * @type {Array}
         */
        $scope.symbols = [];

        (function init() {
            SymbolResource.getAll(project.id)
                .then(function (symbols) {
                    $scope.symbols = symbols;
                })
        }());

        /**
         * Creates a new instance of an Action by a type that was clicked in the modal dialog.
         *
         * @param {string} type - The type of the action that should be created
         */
        $scope.selectNewActionType = function (type) {
            $scope.action = ActionBuilder.createFromType(type);
        };

        /**
         * Closes the modal dialog an passes the created action back to the handle that called the modal
         */
        $scope.createAction = function () {
            $modalInstance.close($scope.action);
        };

        /**
         * Creates a new action in the background without closing the dialog
         */
        $scope.createActionAndContinue = function () {
            modalData.addAction($scope.action);
            $scope.action = null;
        };

        /**
         * Closes the modal dialog without passing any data
         */
        $scope.closeModal = function () {
            $modalInstance.dismiss();
        };
    }
}());