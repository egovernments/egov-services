'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = require('react');

var _warning = require('warning');

var _warning2 = _interopRequireDefault(_warning);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/* eslint-disable flowtype/require-valid-file-annotation */

var checkIndexBounds = function checkIndexBounds(props) {
  var index = props.index,
      children = props.children;


  var childrenCount = _react.Children.count(children);

  process.env.NODE_ENV !== "production" ? (0, _warning2.default)(index >= 0 && index <= childrenCount, 'react-swipeable-view: the new index: ' + index + ' is out of bounds: [0-' + childrenCount + '].') : void 0;
};

exports.default = checkIndexBounds;