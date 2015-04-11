(function (window, angular, undefined) {
  'use strict';

  angular.module('brModels')
    .factory('Message', function ($resource) {
      return $resource('/api/messages/:messageId',
        {messageId: '@id'}, {update: {method: 'PUT'}});
    });
})(window, window.angular);
