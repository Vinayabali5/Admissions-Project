/**
 * The SchoolService module for access School data from the REST API
 */
(function() {

    angular.module('SchoolService', []).factory('School', function($resource) {
        return $resource('/api/resources/schools/:id', {
            id : '@id'
        }, {
            'get' : {
                method : 'GET'
            },
            'query' : {
                method : 'GET',
                params : {
                    sort : 'name'
                }
            },
            'update' : {
                method : 'PUT'
            },
            'create' : {
                method : 'POST'
            }
        });
    });

})();
