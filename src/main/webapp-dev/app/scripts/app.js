(function (window, angular, undefined) {
  'use strict';

  angular.module('yoAngularModels', ['ngResource']);

  var app = angular.module('yoAngularApp', [
    'ngCookies',
    'ngSanitize',
    'ngRoute',
    'yoAngularModels'
  ]);

  app.config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
})(window, window.angular);
