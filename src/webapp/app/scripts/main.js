'use strict';

import 'jquery';
import 'bootstrap';
import angular from 'angular';
import 'angular-route';
import 'angular-resource';
import 'angular-xeditable';

import appModule from './config';

angular.element(document).ready(() =>
  angular.bootstrap(document.body, [appModule.name]));
