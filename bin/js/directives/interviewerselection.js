/**
 * @ngdoc directive
 * @name cisInterviewApp.directive:interviewerSelection
 * @description # interviewerSelection
 */
angular.module('InterviewApp')
    .directive('interviewerSelection', function($http) {
        return {
            // templateUrl:
            // 'scripts/directives/interviewerselection.html',
            require : "ngModel",
            restrict : 'E',
            scope : {
                ngModelVar : '='
            },
            controller : function($scope, $element) {
                $scope.staffList = [];
                $http.get('/staff/current')
                    .success(
                        function(data) {
                            $scope.staffList = data
                                .sort(function(a, b) {
                                    var nameA = a.person.surname + ',' + a.person.firstName
                                    var nameB = b.person.surname + ',' + b.person.firstName;
                                    if (nameA < nameB)
                                        return -1;
                                    if (nameA > nameB)
                                        return 1;
                                    return 0;
                                });
                        });
            },
            controllerAs : 'interviewerSelection',
            link : function(scope, element, attrs, ctrls, ngModel) {
    
            },
            template : '<select ng-model="ngModelVar">'
				+ '<option  ng-repeat="i in staffList" ng-selected="ngModelVar == i.id" ng-value="{{ i.id }}" value="{{ i.id }}">{{ i.initials }} - {{ i.person.surname }}, {{ i.person.firstName }}</option>'
                + '</select>',
            replace : true
        };
    });
