const path = require('path');

module.exports = {
  
  entry: './src/egov-telemetry.js',
  output: {
    filename: 'egov-telemetry.js',
    path: path.resolve(__dirname, 'build')
  }
};