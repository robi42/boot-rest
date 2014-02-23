(function (window, angular, $, _, undefined) {
  'use strict';

  var _focusMessageInput = function () {
    $('#message-input').focus();
  };

  var _showInputValidationModal = function () {
    $('#input-validation-modal').modal({show: true})
      .on('hidden.bs.modal', function () {
        _focusMessageInput();
      });
  };

  angular.module('yoaApp')
    .controller('MainCtrl', function ($scope, $log, Message) {
      $scope.messages = [];
      $scope.newMessage = {body: ''};
      _focusMessageInput();

      $scope.loadMessages = function () {
        $scope.messages = Message.query(function (messages) {
          $log.debug('Number of messages to display:', messages.length);
        });
      };
      $scope.loadMessages();

      $scope.saveNewMessage = function () {
        if (!$scope.messageForm.body.$valid) {
          _showInputValidationModal();
          return;
        }
        Message.save($scope.newMessage, function (savedMessage) {
          $scope.newMessage.body = '';
          $scope.messages.push(savedMessage);
        });
      };

      $scope.deleteMessage = function (messageToDelete) {
        messageToDelete.$delete(function () {
          _($scope.messages).remove(function (message) {
            return message.id === messageToDelete.id;
          });
          _focusMessageInput();
        });
      };
    });
})(window, window.angular, window.jQuery, window._);
