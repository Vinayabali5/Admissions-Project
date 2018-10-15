/**
 * The SchoolEditorDialog module for editing an individual School 
 */
(function() {

    angular.module('SchoolEditorDialog', [
            'ngResource', 
            'ui.bootstrap', 
            'SchoolService',
            'SchoolTypeService', 
            'SchoolPriorityService',
            
          
    ]).controller('SchoolDialogController', function($scope, $uibModalInstance, SchoolType, SchoolPriority, School, currentSchool) {
        $scope.msg = '';
        $scope.currentSchool = currentSchool;
        $scope.schoolTypes = [];
        $scope.schoolPriorities = [];
       
        
       
        SchoolType.query().$promise.then(function(data) {
            $scope.schoolTypes = data._embedded.schoolTypes;
            console.log("Retrieve School Types")
        }, function(error) {
            $scope.schoolTypes = [];
        });

        SchoolPriority.query().$promise.then(function(data) {
            $scope.schoolPriorities = data._embedded.schoolPriorities;
            console.log("Retrieve School Priorities")
        }, function(error) {
            $scope.schoolPriorities = [];
        });

        $scope.test = function() {
            console.log($scope);
        }

        $scope.save = function() {
        	window.location.reload(false); 
            var currentSchool = $scope.currentSchool;
            console.log($scope.currentSchool);
            if (currentSchool.id !== null && currentSchool.id !== undefined) {
                // Update Existing School
                School.update({
                    id : currentSchool.id
                }, currentSchool).$promise.then(function() {
                    $scope.msg = 'School Updated';
                });
            } else {
                // Create New School
                if (currentSchool.name !== null && currentSchool.type !== null && currentSchool.priority !== null) {
                    School.create(currentSchool).$promise.then(function() {
                        $scope.msg = 'School Created: ' + currentSchool.name;
                    });
                } else {
                    $scope.msg = 'Not all fields have been completed.';
                }
            }
            $uibModalInstance.close(currentSchool);
        }

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        }
        
       
    })

})();
