'use strict';

import angular from 'angular';

import routing from './routing';
import Message from './models/message';
import HomeCtrl from './controllers/home';
import tooltip from './directives/tooltip';

let appModule = angular.module('brApp', [
  'ngRoute',
  'ngResource',
  'xeditable'
]).config(routing)
  .run(editableOptions => editableOptions.theme = 'bs3')
  .factory('Message', Message)
  .controller(HomeCtrl.name, HomeCtrl)
  .directive('bsTooltip', tooltip);

export default () =>
  angular.element(document).ready(() =>
    angular.bootstrap(document.body, [appModule.name]));
