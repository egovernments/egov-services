"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
/* eslint-disable flowtype/require-valid-file-annotation */

function mod(n, m) {
  var q = n % m;
  return q < 0 ? q + m : q;
}

exports.default = mod;