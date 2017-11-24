'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = computeIndex;

var _react = require('react');

var _constant = require('./constant');

var _constant2 = _interopRequireDefault(_constant);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

//  weak

function computeIndex(params) {
  var children = params.children,
      startIndex = params.startIndex,
      startX = params.startX,
      pageX = params.pageX,
      viewLength = params.viewLength,
      resistance = params.resistance;


  var indexMax = _react.Children.count(children) - 1;
  var index = startIndex + (startX - pageX) / viewLength;
  var newStartX = void 0;

  if (!resistance) {
    // Reset the starting point
    if (index < 0) {
      index = 0;
      newStartX = (index - startIndex) * viewLength + pageX;
    } else if (index > indexMax) {
      index = indexMax;
      newStartX = (index - startIndex) * viewLength + pageX;
    }
  } else if (index < 0) {
    index = Math.exp(index * _constant2.default.RESISTANCE_COEF) - 1;
  } else if (index > indexMax) {
    index = indexMax + 1 - Math.exp((indexMax - index) * _constant2.default.RESISTANCE_COEF);
  }

  return {
    index: index,
    startX: newStartX
  };
}