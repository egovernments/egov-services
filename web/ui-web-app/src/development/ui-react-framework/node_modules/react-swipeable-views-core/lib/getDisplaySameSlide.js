'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
//  weak

var getDisplaySameSlide = function getDisplaySameSlide(props, nextProps) {
  var displaySameSlide = false;

  if (props.children.length && nextProps.children.length) {
    var oldChildren = props.children[props.index];
    var oldKey = oldChildren ? oldChildren.key : 'empty';

    if (oldKey !== null) {
      var newChildren = nextProps.children[nextProps.index];
      var newKey = newChildren ? newChildren.key : 'empty';

      if (oldKey === newKey) {
        displaySameSlide = true;
      }
    }
  }

  return displaySameSlide;
};

exports.default = getDisplaySameSlide;