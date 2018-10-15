
/**
 * @ngdoc directive
 * @name cisInterviewApp.directive:offerTypeSelection
 * @description
 * # offerTypeSelection
 */
angular.module('InterviewApp')
  .directive('offerTypeSelection', function ($http) {
    return {
		//templateUrl: 'scripts/directives/offertypeselection.html',
    	require: "ngModel",
		restrict: 'E',
		scope: {
			datasets: '=',
			ngModelVar : '='
		},
		controller: function($scope, $element) {
			$scope.offers = [];
			$http.get('/offerTypes').success(function(data) {
				$scope.offers = data
		 	});
	     },
      	controllerAs: 'offerSelection',
		link: function(scope, element, attrs, ctrls, ngModel) {
			
     	},
    	template:'<select ng-model="ngModelVar">' +
    			'<option  ng-repeat="o in offers" ng-selected="ngModelVar == o.id" ng-value="{{ o.id }}" value="{{ o.id }}">{{ o.description }}</option>' +
    			'</select>',
     	replace: true
    };
  });
