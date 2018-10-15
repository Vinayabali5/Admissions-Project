var app = angular.module('applicationControllers', []);

app.controller('ApplicationListCtrl', [ '$scope', '$http',
		function($scope, $http) {
			$scope.applications = {};
			
			$http.get('applications.json').success(function(data) {
				$scope.applications = data;
			});
		} ]);
