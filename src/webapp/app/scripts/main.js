'use strict';

import 'jquery';
import 'bootstrap';
import angular from 'angular';
import 'angular-touch';
import 'angular-animate';
import 'angular-sanitize';
import 'angular-cookies';
import 'angular-route';
import 'angular-resource';
import 'angular-xeditable';

import appModule from './config';

$(document).ready(() =>
  angular.bootstrap(document.body, [appModule.name]));
