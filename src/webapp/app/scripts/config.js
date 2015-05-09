'use strict';

import routing from './routing';
import newMessage from './models/message';
import HomeCtrl from './controllers/home';
import tooltip from './directives/tooltip';

export default angular.module('brApp', [
  'ngTouch',
  'ngAnimate',
  'ngSanitize',
  'ngCookies',
  'ngRoute',
  'ngResource',

  'xeditable'
]).config(routing)
  .run(editableOptions => editableOptions.theme = 'bs3')
  .factory('Message', newMessage)
  .controller(HomeCtrl.name, HomeCtrl)
  .directive('bsTooltip', tooltip);

