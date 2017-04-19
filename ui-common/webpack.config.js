var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = [{
  watch: true,
  context: path.resolve(__dirname, 'app'),
  resolve: {
    extensions: ['.js', '.jsx', '.json']
  },

  entry: {
    home: "./index.js"
  },

  output: {
    path: path.resolve(__dirname, 'public', ''),
    filename: "js/[name].[chunkhash].bundle.js"
  },

  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      },

      {
        test: /\.css$/,
        exclude: /node_modules/,
        use: ExtractTextPlugin.extract({
          fallback: "style-loader",
          use: { loader: "css-loader" }
        })
      }
    ]
  },

  plugins: [
    new HtmlWebpackPlugin({
        filename: 'index.html',
      template: path.resolve(__dirname, "app", "index.html"),
    }),

    new ExtractTextPlugin('css/[name].[hash].css')
  ],

  devServer: {
    contentBase: path.join(__dirname, 'dist'),
    port: 3000
  }
}];
