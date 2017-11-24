'use strict';

exports.__esModule = true;
var getDisplayName = function getDisplayName(Component) {
  return Component.displayName || Component.name || (typeof Component === 'string' ? Component : 'Component');
};

exports.default = getDisplayName;