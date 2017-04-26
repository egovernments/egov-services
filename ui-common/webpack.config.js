var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = [{
  watch: true,
  context: path.resolve(__dirname, 'app'),
  resolve: {
    extensions: ['.js', '.jsx']
  },

  entry: {
    index : "./index.js",
    create_usagetype : './components/usage_master/create_usagetype.js',
    create_category : './components/category_master/create_category.js',
    create_pipesize : './components/pipesize_master/create_pipesize.js'
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
        // exclude: /node_modules/,
        use: ExtractTextPlugin.extract({
          fallback: "style-loader",
          use: { loader: "css-loader" }
        })
      },

      {
            test: /\.less$/,
            use: [{
                loader: "style-loader" // creates style nodes from JS strings
            }, {
                loader: "css-loader" // translates CSS into CommonJS
            }, {
                loader: "less-loader" // compiles Less to CSS
            }]
      },

      {
        test: /\.(eot|svg|ttf|woff|woff2)$/,
        use: ['file-loader']
      },

      {
        test: /\.(jpe?g|png|gif|svg)$/i,
        loaders: ['file-loader?context=src/images&name=images/[path][name].[ext]', {
          loader: 'image-webpack-loader',
          query: {
            mozjpeg: {
              progressive: true,
            },
            gifsicle: {
              interlaced: false,
            },
            optipng: {
              optimizationLevel: 4,
            },
            pngquant: {
              quality: '75-90',
              speed: 3,
            },
          },
        }],
        exclude: /node_modules/,
        include: __dirname,
      }

    ]
  },

  plugins: [
    new HtmlWebpackPlugin({
        filename: 'index.html',
        template: path.resolve(__dirname, "app", "index.html")
    }),

    new HtmlWebpackPlugin({
        filename: 'create_usagetype.html',
        template: path.resolve(__dirname, "app", "./containers/usage_master/create_usagetype.html"),
        chunks : ['create_usagetype']
    }),

    new HtmlWebpackPlugin({
        filename: 'create_category.html',
        template: path.resolve(__dirname, "app", "./containers/category_master/create_category.html"),
        chunks : ['create_category']
    }),

    new HtmlWebpackPlugin({
        filename: 'create_pipesize.html',
        template: path.resolve(__dirname, "app", "./containers/pipesize_master/create_pipesize.html"),
        chunks : ['create_pipesize']
    }),

    new ExtractTextPlugin('css/[name].[hash].css')
  ],

  devServer: {
    contentBase: path.join(__dirname, 'dist'),
    port: 3000
  }
}];
