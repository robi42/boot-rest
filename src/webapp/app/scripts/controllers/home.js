'use strict';

import _ from 'lodash';

let _focusNewMessageInput = () => angular.element('#new-message-input').focus();

let _showInputValidationModal = () =>
  angular.element('#input-validation-modal').modal({show: true})
    .on('hidden.bs.modal', () => _focusNewMessageInput());

export default class HomeCtrl {
  constructor($log, $scope, Message) {
    'ngInject';

    this.$scope = $scope;
    this.Message = Message;

    _focusNewMessageInput();

    $scope.messages = Message.query(messages =>
      $log.debug('Number of messages to display:', messages.length));
    $scope.newMessage = {body: ''};
  }

  saveMessage() {
    if (!this.$scope.messageForm.body.$valid) {
      _showInputValidationModal();
      return;
    }
    this.Message.save(this.$scope.newMessage, message => {
      this.$scope.messages.push(message);
      this.resetForm();
    });
  }

  resetForm() {
    this.$scope.newMessage.body = '';
  }

  validate(text) {
    if (!text) {
      _showInputValidationModal();
      return false;
    }
    return true;
  }

  update(message) {
    message.$update(updatedMessage => {
      let messageIndex = _(this.$scope.messages).findIndex({id: updatedMessage.id});
      this.$scope.messages[messageIndex] = updatedMessage;
      _focusNewMessageInput();
    });
  }

  remove(message) {
    message.$delete(() => {
      _.remove(this.$scope.messages, {id: message.id});
      _focusNewMessageInput();
    });
  }
}
