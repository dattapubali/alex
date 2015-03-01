(function () {
    'use strict';

    angular
        .module('weblearner.directives')
        .directive('symbolCreateModalHandle', symbolCreateModalHandle);

    symbolCreateModalHandle.$inject = ['$modal', 'paths'];

    /**
     * The directive that handles the modal window for the creation of a new symbol. It attaches an click event to the
     * attached element that opens the modal dialog.
     *
     * Use it as an Attribute like 'symbol-create-modal-handle' and add an attribute 'project-id' with the id of the
     * project and an attribute 'on-created' which expects a callback function from the directives parent controller.
     * The callback function should have one parameter that will be the newly created symbol.
     *
     * @param $modal - The $modal service
     * @param paths - The constants for application paths
     * @returns {{restrict: string, scope: {projectId: string, onCreated: string}, link: link}}
     */
    function symbolCreateModalHandle($modal, paths) {

        var directive = {
            restrict: 'A',
            scope: {
                projectId: '@',
                onCreated: '&'
            },
            link: link
        };
        return directive;

        /**
         * @param scope
         * @param el
         * @param attrs
         */
        function link(scope, el, attrs) {

            // attach the click event on the target element that opens the modal dialog
            el.on('click', handleModal);

            function handleModal() {
                var modal = $modal.open({
                    templateUrl: paths.views.MODALS + '/symbol-create-modal.html',
                    controller: 'SymbolCreateModalController',
                    resolve: {
                        modalData: function () {
                            return {
                                projectId: scope.projectId
                            }
                        }
                    }
                });

                // call the callback with the created symbol on success
                modal.result.then(function (symbol) {
                    scope.onCreated()(symbol);
                })
            }
        }
    }
}());