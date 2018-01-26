var utils = require('./utils')
var path = require('path')
var webpack = require('webpack')
var config = require('../config')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var CopyWebpackPlugin = require('copy-webpack-plugin')
var FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin')

// add hot-reload related code to entry chunks
Object.keys(baseWebpackConfig.entry).forEach(function (name) {
  baseWebpackConfig.entry[name] = ['./webpack/dev-client'].concat(baseWebpackConfig.entry[name])
})

function resolveApp(relativePath) {
  return path.resolve(relativePath);
}

module.exports = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({ sourceMap: config.dev.cssSourceMap, usePostCSS: true })
  },
  // cheap-source-map is faster for development
  devtool: '#cheap-source-map',
  cache: true,
  plugins: [
    new webpack.DefinePlugin({
      'process.env': config.dev.env
    }),
    // https://github.com/glenjamin/webpack-hot-middleware#installation--usage
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NoEmitOnErrorsPlugin(),
    // https://github.com/ampedandwired/html-webpack-plugin
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: 'index.html',
      favicon: resolveApp('favicon.ico'),
      inject: true,
      path: config.dev.assetsPublicPath + config.dev.assetsSubDirectory
    }),
    new FriendlyErrorsPlugin(),
    // copy custom static assets
    new CopyWebpackPlugin([
      // { from: path.resolve(__dirname, './src/main/webapp/static'), to: config.build.assetsSubDirectory, ignore: ['.*']},
      { from: './node_modules/swagger-ui/dist/css', to: 'swagger-ui/dist/css' },
      { from: './node_modules/swagger-ui/dist/lib', to: 'swagger-ui/dist/lib' },
      { from: './node_modules/swagger-ui/dist/swagger-ui.min.js', to: 'swagger-ui/dist/swagger-ui.min.js' },
      { from: './src/main/webapp/swagger-ui/', to: 'swagger-ui' },
      { from: './src/main/webapp/manifest.webapp', to: 'manifest.webapp' },
      // { from: './src/main/webapp/sw.js', to: 'sw.js' },
      // jhipster-needle-add-assets-to-webpack - JHipster will add/remove third-party resources in this array
      { from: './src/main/webapp/robots.txt', to: 'robots.txt' }
    ])
  ]
})

