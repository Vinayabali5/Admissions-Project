
/**
 * @ngdoc directive
 * @name cisInterviewApp.directive:studentTypeSelection
 * @description
 * # studentTypeSelection
 */
angular.module('InterviewApp')
  .directive('studentTypeSelection', ['$http', function ($http) {
    return {
		//templateUrl: 'scripts/directives/studenttypeselection.html',
    	require: "ngModel",
		restrict: 'E',
		scope: {
			datasets: '=',
			ngModelVar : '='
		},
		controller: function($scope, $element) {
			$scope.students = [];
			$http.get('/studentTypes').success(function(data) {
				$scope.students = data;
		 	});
	     },
      	controllerAs: 'studentSelection',
		link: function(scope, element, attrs, ctrls, ngModel) {
     	},
    	template: '<select ng-model="ngModelVar">' +
				'<option  ng-repeat="s in students" ng-selected="ngModelVar == s.id" ng-value="{{ s.id }}" value="{{ s.id }}">{{ s.description }}</option>' +
				'</select>',
     	replace: true
    };
  }]);
