// const path = require('path');

// module.exports = {
  
//   entry: './src/egov-telemetry.js',
//   output: {
//     filename: 'egov-telemetry-1540795215.js',
//     path: path.resolve(__dirname, 'build')
//   },
//   devtool: 'source-map',
//   module: {
//     rules: [
//       {
//         test: /\.js?/,
//         include: '/src',
//         use: {
//           loader: 'babel-loader',
//           options: {
//             presets: ['es2015', 'react', 'stage-0'],
//             plugins: [
//               [
//                 "transform-runtime",
//                 {
//                   "polyfill": false,
//                   "regenerator": true
//                 }
//               ]
//             ]
//           }
//         }
//       }
//     ]
//   }
// };


const path = require("path");
const webpack = require("webpack");
const webpack_rules = [];
const webpackOption = {
    entry: "./src/egov-telemetry.js",
    output: {
        path: path.resolve(__dirname, "build"),
        filename: "egov-telemetry-1540795215.js",
    },
    module: {
        rules: webpack_rules
    }
};
let babelLoader = {
    test: /\.js$/,
    exclude: /(node_modules|bower_components)/,
    use: {
        loader: "babel-loader",
        options: {
            presets: ["@babel/preset-env"]
        }
    }
};
webpack_rules.push(babelLoader);
module.exports = webpackOption;

