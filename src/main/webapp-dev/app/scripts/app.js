(function (window, angular, undefined) {
  'use strict';

  angular.module('brModels', ['ngResource']);
  angular.module('brControllers', []);
  angular.module('brDirectives', []);

  var app = angular.module('brApp', [
    'ngAnimate',
    'ngCookies',
    'ngSanitize',
    'ngRoute',
    'xeditable',
    'brModels',
    'brControllers',
    'brDirectives'
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
