// see http://vuejs-templates.github.io/webpack for documentation.
var path = require("path");
var devEnv = require("./dev.env");
module.exports = {
  build: {
    sitEnv: require("./sit.env"),
    prodEnv: require("./prod.env"),
    index: path.resolve(__dirname, "../src/main/resources/public/index.html"),
    assetsRoot: path.resolve(__dirname, "../src/main/resources/public"),
    assetsSubDirectory: "static",
    assetsPublicPath: "./", //请根据自己路径配置更改
    productionSourceMap: false,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ["js", "css"],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: devEnv,
    port: 9577,
    autoOpenBrowser: true,
    assetsSubDirectory: "public",
    assetsPublicPath: "/",
    proxyTable: {
      backend: {
        filter: [
          /* jhipster-needle-add-entity-to-webpack - JHipster will add entity api paths here */
          "/api",
          "/management",
          "/swagger-resources",
          "/v2/api-docs",
          "/h2-console",
          "/auth"
        ],
        target: "http://127.0.0.1:8080",
        secure: false
      },
      websocket: {
        filter: ["/websocket"],
        target: "ws://127.0.0.1:8080",
        ws: true
      }
    },
    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false
  }
};
