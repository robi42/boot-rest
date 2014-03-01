(function (window, angular, undefined) {
  'use strict';

  angular.module('yoaDirectives')
    .directive('bsTooltip', function () {
      return function (scope, element) {
        element.find('[data-toggle=tooltip]').tooltip();
      };
    });
})(window, window.angular);
