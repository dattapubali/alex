/**
 * The directive that handles the opening of the modal dialog for manipulating a learn configuration. Can only be
 * used as an attribute and attaches a click event to the source element that opens the modal.
 *
 * Attribute 'learnConfiguration' should be the model with a LearnConfiguration object instance.
 * Attribute 'onOk' should be a callback function with one parameter where the modified config is passed.
 *
 * @param $modal - The ui.boostrap $modal service
 * @returns {{restrict: string, scope: {learnConfiguration: string, onOk: string}, link: link}}
 */
// @ngInject
function learnSetupSettingsModalHandle($modal) {
    return {
        restrict: 'A',
        scope: {
            learnConfiguration: '=',
            onOk: '&'
        },
        link: link
    };

    function link(scope, el) {
        el.on('click', () => {
            var modal = $modal.open({
                templateUrl: 'views/modals/learn-setup-settings-modal.html',
                controller: 'LearnSetupSettingsModalController',
                resolve: {
                    modalData: function () {
                        return {
                            learnConfiguration: scope.learnConfiguration
                        };
                    }
                }
            });
            modal.result.then(learnConfiguration => {
                if (angular.isDefined(scope.onOk)) {
                    scope.onOk()(learnConfiguration);
                }
            });
        });
    }
}

export default learnSetupSettingsModalHandle;