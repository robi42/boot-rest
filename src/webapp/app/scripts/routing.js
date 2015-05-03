'use strict';

export default ($locationProvider, $routeProvider) => {
  'ngInject';

  $locationProvider.hashPrefix('!');

  $routeProvider
    .when('/', {
      templateUrl: 'views/home.html',
      controller: 'HomeCtrl as ctrl'
    })
    .otherwise({
      redirectTo: '/'
    });
};
