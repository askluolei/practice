var path = require('path')
var utils = require('./utils')
var config = require('../config')
var vueLoaderConfig = require('./vue-loader.conf')

function resolve(dir) {
  return path.join(__dirname, '..', dir)
}

module.exports = {
  entry: {
    app: './src/main/webapp/app/main.js'
  },
  output: {
    path: config.build.assetsRoot,
    filename: '[name].js',
    publicPath: process.env.NODE_ENV === 'production' ? config.build.assetsPublicPath : config.dev.assetsPublicPath
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
      '@': resolve('src/main/webapp/app'),
      'src': path.resolve(__dirname, '../src/main/webapp/app'),
      'assets': path.resolve(__dirname, '../src/main/webapp/app/assets'),
      'components': path.resolve(__dirname, '../src/main/webapp/app/components'),
      'views': path.resolve(__dirname, '../src/main/webapp/app/views'),
      'styles': path.resolve(__dirname, '../src/main/webapp/app/styles'),
      'api': path.resolve(__dirname, '../src/main/webapp/app/api'),
      'utils': path.resolve(__dirname, '../src/main/webapp/app/utils'),
      'store': path.resolve(__dirname, '../src/main/webapp/app/store'),
      'router': path.resolve(__dirname, '../src/main/webapp/app/router'),
      'mock': path.resolve(__dirname, '../src/main/webapp/app/mock'),
      'vendor': path.resolve(__dirname, '../src/main/webapp/app/vendor'),
      'static': path.resolve(__dirname, '../src/main/webapp/content')
    }
  },
  module: {
    rules: [
      {
        test: /\.(js|vue)$/,
        loader: 'eslint-loader',
        enforce: "pre",
        include: [resolve('src/main/webapp/app'), resolve('src/test/javascript')],
        options: {
            formatter: require('eslint-friendly-formatter')
        }
      },
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: vueLoaderConfig
      },
      {
        test: /\.js$/,
        loader: 'babel-loader?cacheDirectory',
        include: [resolve('src/main/webapp/app'), resolve('src/test/javascript')]
      },
      {
        test: /\.svg$/,
        loader: 'svg-sprite-loader',
        include: [resolve('src/main/webapp/app/icons')],
        options: {
          symbolId: 'icon-[name]'
        }
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        exclude: [resolve('src/main/webapp/app/icons')],
        options: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
        }
      }
    ]
  },
  //注入全局mixin
  // sassResources: path.join(__dirname, '../src/styles/mixin.scss'),
  // sassLoader: {
  //     data:  path.join(__dirname, '../src/styles/index.scss')
  // },
}

