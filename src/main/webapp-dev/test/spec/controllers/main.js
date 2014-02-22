'use strict';

describe('Controller: MainCtrl', function () {

  // Load the controller's module
  beforeEach(module('yoaApp'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    // TODO: write some real tests
    expect(true).toBeTruthy();
//    expect(scope.awesomeThings.length).toBe(3);
  });
});
