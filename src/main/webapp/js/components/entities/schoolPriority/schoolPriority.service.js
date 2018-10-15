/**
 * The SchoolPriorityService module for access SchoolPriority data from the REST API
 */
(function() {

    angular.module('SchoolPriorityService', [])    .factory('SchoolPriority', function($resource) {
        return $resource('/api/resources/schoolPriorities/:id', {
            id : '@id'
        }, {
            'query' : {
                method : 'GET'
            }
        });
    })

})();
