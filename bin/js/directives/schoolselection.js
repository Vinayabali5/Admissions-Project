
/**
 * @ngdoc directive
 * @name cisInterviewApp.directive:schoolSelection
 * @description
 * # schoolSelection
 */
angular.module('InterviewApp')
  .directive('schoolSelection', ['$http', function ($http) {
    return {
		//templateUrl: 'scripts/directives/schoolselection.html',
		restrict: 'E',
		scope: {
			datasets: '=',
			ngmodelvar : '='
		},
		controller: function($scope, $element) {
			$scope.schools = [];
			$http.get('http://localhost:10101/schools').success(function(data) {
				$scope.schools = data;
		 	});
	     },
      	controllerAs: 'schoolSelection',
		link: function(scope, element, attrs, ctrls, ngModel) {
     	},
    	template: '<select ng-model="ngmodelvar">' +
		'<option>-- Select Option --</option>' +
 		'<option  ng-repeat="c in schools" value="{{ c.id }}">{{ c.name }}</option>' +
		'</select>',
     	replace: true
    };
  }]);
