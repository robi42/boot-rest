'use strict';

export default $resource => {
  'ngInject';

  return $resource('/api/messages/:id',
    {id: '@id'}, {update: {method: 'PUT'}});
};
