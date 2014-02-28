(function (window, angular, undefined) {
  'use strict';

  angular.module('yoaModels', ['ngResource']);

  var app = angular.module('yoaApp', [
    'ngCookies',
    'ngSanitize',
    'ngRoute',
    'xeditable',
    'yoaModels'
  ]);

  app.config(function ($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

  app.run(function (editableOptions) {
    editableOptions.theme = 'bs3';
  });
})(window, window.angular);
