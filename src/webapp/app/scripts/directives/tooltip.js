'use strict';

export default () => (scope, element) =>
  element.find('[data-toggle=tooltip]').tooltip();
