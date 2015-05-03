'use strict';

export default $resource => {
  'ngInject';

  return $resource('/api/messages/:messageId',
    {messageId: '@id'}, {update: {method: 'PUT'}});
};
