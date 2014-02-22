(function (window, angular, undefined) {
  'use strict';

  angular.module('yoAngularApp')
    .controller('MainCtrl', function ($scope, $log, Message) {
      $scope.loadMessages = function () {
        $scope.messages = Message.query(function (messages) {
          $log.debug('Number of messages to display:', messages.length);
        });
      };
      $scope.loadMessages();

      $scope.saveNewMessage = function () {
        Message.save($scope.newMessage, function () {
          $scope.newMessage.body = '';
          $scope.loadMessages();
        });
      };

      $scope.deleteMessage = function (message) {
        message.$delete(function () {
          $scope.loadMessages();
        });
      };
    });
})(window, window.angular);
