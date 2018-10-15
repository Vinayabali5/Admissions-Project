var app = angular.module('InterviewApp', ['ngRoute', 'applicationControllers']);

app.config([
        '$routeProvider', function($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: '/views/application.html',
                controller: 'ApplicationListCtrl'
            })
            .when('/interview/:id', {
                templateUrl: '/view/interview.html',
                controller: 'InterviewCtrl'
            });
        }
]);
