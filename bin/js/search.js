/**
 * The Search Module for finding applicants
 */
var app = angular.module('ApplicationSearch', []);

app.controller('ApplicationSearchController', function($log, $scope, $http) {
	
	var vm = this;
	
    this.searchTerm = '';
    this.noResults = true;
    $scope.noResults = true;
    
    this.loading = false;
	this.message = "";
	
	this.displayMessage = function(message) {
		$log.debug('II SearchController :: displayMessage called');
		vm.message = message;
	};

	
    /* Event to Search */
    this.search = function() {
    	$log.debug('II StudentSearchController :: search called');
		vm.loading = true;
		vm.displayMessage("Loading please wait!");
        var search = this.searchTerm;
        var url = "/applications/search/" + search;
        $http.get(url).success(function(response) {
            if (response.length !== 0) {
                $scope.applicationList = response;
                this.noResults = false;
                $scope.noResults = false;
                vm.displayMessage("");
            } else {
                $scope.applicationList = {};
                this.noResults = true;
                $scope.noResults = true;
                vm.displayMessage(response.message);
            }
        });

    };

});

$( document ).ready(function() {
    $("#search").trigger("focus");
});
