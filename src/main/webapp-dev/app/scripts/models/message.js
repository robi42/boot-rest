(function (window, angular, undefined) {
  'use strict';

  var models = angular.module('yoAngularModels');

  models.factory('Message', function ($resource) {
    return $resource('/api/messages/:messageId',
      {messageId: '@id'},
      {update: {method: 'PUT'}});
  });
})(window, window.angular);
