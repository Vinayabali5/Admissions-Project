/**
 * The SchoolEditor module for displaying and editing the list of Schools
 */
(function() {

    angular.module('SchoolEditor', [
            'ngResource', 
            'ui.bootstrap', 
            'SchoolService', 
            'SchoolTypeService',
            'SchoolPriorityService',
            'SchoolEditorDialog'
    ])
    .controller('SchoolListController', function($scope, $http, $uibModal, School) {
        console.log('ControlerLoaded')
        $scope.searchTerm = '';
        $scope.schoolList = [];

        $scope.paginationInfo = {
            pageNumber : 1,
            pageSize : 10,
            sort : 'name',
            order : 'ASC'
        };

        $scope.getPage = function() {
            console.log('Loading page: ' + $scope.paginationInfo.pageNumber);
            School.query({
                page : $scope.paginationInfo.pageNumber - 1,
                size : $scope.paginationInfo.pageSize,
                sort : $scope.paginationInfo.sort + ',' + $scope.paginationInfo.order
            }, function(data, header) {
                $scope.paginationInfo.pageNumber = data.page.number + 1;
                $scope.paginationInfo.pageSize = data.page.size;
                $scope.paginationInfo.totalItems = data.page.totalElements;
                $scope.paginationInfo.totalPages = data.page.totalPages;
                $scope.schoolList = data._embedded.schools;
            });
        };

        $scope.pageChanged = function() {
            console.log('Page changed to: ' + $scope.paginationInfo.pageNumber);
            $scope.getPage();
        };

        $scope.loadSchools = function() {
            School.query(function(data) {
                $scope.schoolList = data._embedded.schools;
            });
        };

        /* Event to Search */
        $scope.search = function() {
            var search = $scope.searchTerm;
            var url = this.url;
            if (search.size > 3) {
                var appList = this.applicationList
                $http.get(url).success(function(response) {
                    $scope.schoolList = response;
                });
            }
        };

        /* Edit School */
        $scope.editSchool = function(index, id) {
            console.log('Edit School with id: ' + id);

            School.get({
                id : id
            }, function(school) {
                console.log(school);
                $scope.currentSchool = school;
            });

            var schoolEditor = $uibModal.open({
                templateUrl : '/js/modules/entities/school/schoolEditorDialog.html',
                controller : 'SchoolDialogController',
                resolve : {
                    currentSchool : function() {
                        return School.get({
                            id : id
                        }).$promise;
                    }
                }
            });

            schoolEditor.result.then(function(updatedSchool) {
                console.log("Updating school list");
                $scope.schoolList[index] = updatedSchool;
                $scope.getPage();
            }, function() {
                console.log("ERR");
            });

        };
        /* Add School */
        $scope.addSchool = function() {
            console.log('Adding New School');

            var schoolEditor = $uibModal.open({
                templateUrl : '/views/school-form.html',
                controller : 'SchoolDialogController',
                resolve : {
                    currentSchool : function() {
                        return {
                            name : '',
                            type : null,
                            priority : null
                        }
                    }
                }
            });

        }

        $scope.getPage();

    });

})();
