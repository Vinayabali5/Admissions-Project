/**
 * The Search Module for finding applicants 
 */
(function(){

	var app = angular.module('search', []);
	
	app.directive('searchBox', function(){
		return {
			restrict: 'E',
			scope: {},
			templateUrl: 'js/directives/search.html',
			controller: function($scope, $http) {
				$scope.searchTerm = '';
				$scope.applicationList = [];
				
				/* Event to Search */
				$scope.search = function() {
					var search = this.searchTerm;
					var url = "/applications/search/" + search; 
					var appList = this.applicationList 
					$http
					.get(url)
					.success(function(response) {
						$scope.applicationList = response;
					});
				};
			},
			controllerAs: 's'
		};
	});
	
})();

