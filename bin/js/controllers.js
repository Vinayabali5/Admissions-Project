var app = angular.module('applicationControllers', []);

app.controller('ApplicationListCtrl', [
        '$scope', '$http', function($scope, $http) {

            $http.get('applications.json').success(function(data) {
                $scope.students = data;
            });
        }
]);

app.controller('InterviewCtrl', [
        '$scope', '$routeParms', function($scope, $routeParams) {
            $scope.studentId = $routeParams.id;
        }
]);
