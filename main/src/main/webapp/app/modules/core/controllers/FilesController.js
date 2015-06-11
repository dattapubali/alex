(function () {
    'use strict';

    angular
        .module('ALEX.core')
        .controller('FilesController', FilesController);

    FilesController.$inject = ['$scope', 'Upload', 'paths', 'ToastService', 'SessionService', 'FileResource', '_'];

    /**
     * The controller that manages files of a project handles file uploads
     *
     * Template: 'views/files.html'
     *
     * @param $scope - angular $scope
     * @param Upload - ngFileUpload Upload service
     * @param paths - The applications paths constant
     * @param Toast - The ToastService
     * @param Session - The SessionService
     * @param FileResource - The Resource that handles API requests for files
     * @param _ - Lodash
     * @constructor
     */
    function FilesController($scope, Upload, paths, Toast, Session, FileResource, _) {

        var project = Session.project.get();

        /**
         * All project related files
         * @type {{name: string, project: number}[]}
         */
        $scope.files = [];

        /**
         * The selected files
         * @type {{name: string, project: number}[]}
         */
        $scope.selectedFiles = [];

        /**
         * The progress in percent of the current uploading file
         * @type {number}
         */
        $scope.progress = 0;

        /**
         * The list of files to upload
         * @type {null|File[]}
         */
        $scope.filesToUpload = null;

        (function init() {
            FileResource.getAll(project.id)
                .then(function (files) {
                    $scope.files = files;
                }).catch(function () {

                });
        }());

        /**
         * Remove a single file from the server and the list
         *
         * @param {string} file - The name of the file to delete
         */
        $scope.deleteFile = function (file) {
            FileResource.delete(project.id, file)
                .then(function () {
                    Toast.success('File "' + file.name + '" has been deleted');
                    _.remove($scope.files, function (f) {
                        return f.name === file.name;
                    });
                })
        };

        /**
         * Upload all chosen files piece by piece and add successfully deleted files to the list
         */
        $scope.upload = function () {
            var error = false;
            var countFiles = $scope.files.length;

            function next() {
                $scope.progress = 0;
                if ($scope.filesToUpload.length > 0) {
                    var file = $scope.filesToUpload[0];
                    Upload.upload({
                        url: paths.api.URL + '/projects/' + project.id + '/files',
                        file: file
                    }).progress(function (evt) {
                        $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                    }).success(function (data) {
                        $scope.filesToUpload.shift();
                        $scope.files.push(data);
                        next();
                    }).error(function () {
                        error = true;
                        $scope.filesToUpload.shift();
                        next();
                    })
                } else {
                    if ($scope.files.length === countFiles) {
                        Toast.danger('<strong>Upload failed</strong><p>No file could be uploaded</p>');
                    } else {
                        if (error) {
                            Toast.info('Some files could not be uploaded');
                        } else {
                            Toast.success('All files uploaded successfully');
                        }
                    }
                }
            }

            next();
        };

        /**
         * Batch delete selected files
         * TODO: call batch resource function as soon as there is an endpoint for that
         */
        $scope.deleteSelectedFiles = function () {
            _.forEach($scope.selectedFiles, $scope.deleteFile);
        }
    }
}());