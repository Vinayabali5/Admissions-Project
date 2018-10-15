/**
 * The SchoolTypeService module for access SchoolType data from the REST API
 */
(function() {

    angular.module('SchoolTypeService', []).factory('SchoolType', function($resource) {
        return $resource('/api/resources/schoolTypes/:id', {
            id : '@id'
        }, {
            'query' : {
                method : 'GET'
            }
        });
    })

})();
