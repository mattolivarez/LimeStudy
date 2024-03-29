module.exports = {
  devServer: {
    port: 8000,
    proxy: {
      '/auth': {
          target: 'http://127.0.0.1:8085',
          ws: true,
          changeOrigin: true,
        },
        '/api': {
          target: 'http://localhost:8080',
          ws: true,
          changeOrigin: true
        },
    }
  },
  chainWebpack: (config) => {
    const svgRule = config.module.rule("svg");

    svgRule.uses.clear();

    svgRule
      .use("babel-loader")
      .loader("babel-loader")
      .end()
      .use("vue-svg-loader")
      .loader("vue-svg-loader");
  },
};
