(function (window, angular, _, undefined) {
  'use strict';

  var _focusNewMessageInput = function () {
    angular.element('#new-message-input').focus();
  };

  var _showInputValidationModal = function () {
    angular.element('#input-validation-modal').modal({show: true})
      .on('hidden.bs.modal', function () {
        _focusNewMessageInput();
      });
  };

  angular.module('yoaControllers')
    .controller('MainCtrl', function ($scope, $log, Message) {
      $scope.messages = [];
      $scope.newMessage = {body: ''};
      _focusNewMessageInput();

      $scope.loadMessages = function () {
        $scope.messages = Message.query(function (messages) {
          $log.debug('Number of messages to display:', messages.length);
        });
      };
      $scope.loadMessages();

      // Handle update of a message body on change via in-place edit
      $scope.$watch('messages', function (newValues, oldValues) {
        var hasUpdatedMessage = newValues && oldValues && (newValues.length === oldValues.length) &&
          (newValues !== oldValues);

        if (hasUpdatedMessage) {
          _(newValues).each(function (newValue) {
            var oldValue = _(oldValues).find({id: newValue.id});
            var hasUpdatedMessageBody = oldValue && (oldValue.body !== newValue.body);

            if (hasUpdatedMessageBody) {
              newValue.$update(function (updatedMessage) {
                var indexOfUpdatedMessage = _($scope.messages).findIndex({id: updatedMessage.id});
                $scope.messages[indexOfUpdatedMessage] = updatedMessage;
                _focusNewMessageInput();
              });
            }
          });
        }
      }, true);

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
          _focusNewMessageInput();
        });
      };
    });
})(window, window.angular, window._);
