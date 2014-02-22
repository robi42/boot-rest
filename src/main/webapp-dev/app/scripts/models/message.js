(function (window, angular, undefined) {
  'use strict';

  angular.module('yoAngularModels')
    .factory('Message', function ($resource) {
      return $resource('/api/messages/:messageId',
        {messageId: '@id'}, {update: {method: 'PUT'}});
    });
})(window, window.angular);
