const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
var webpack = require("webpack");
const { VueLoaderPlugin } = require('vue-loader');

module.exports = {
  mode: 'development',
  entry: {
    app: './js/app.js',
    vue: './components/app.js',
    bewerben: './js/pages/bewerben.js',
    bewerbungsportal: './js/pages/bewerbungsportal.js',
    change_pw: './js/pages/changePw.js',
    cms: './js/pages/cms.js',
    index: './js/pages/index.js',
    nutzer: './js/pages/nutzer.js',
    task_overview_sgl: './js/pages/task_overview_sgl.js',
    task_overview: './js/pages/task_overview.js',
    task_detail_sgl: './js/pages/task-detail_sgl.js',
    task_detail: './js/pages/task-detail.js',
    admin_process_modeler: './js/admin/admin-process-modeler.js',
    admin_process: './js/admin/admin-process.js',
    auslandsamt_tabelle: './js/admin/auslandsamt_tabelle.js',
    student_tabelle: './js/admin/student_tabelle.js',
    studiengangsleitung_tabelle: './js/admin/studiengangsleitung_tabelle.js',
  },
  devServer: {
    static: './dist',
    port: 1234,
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'dist'),
    clean: true,
  },
  plugins: [
    new HtmlWebpackPlugin({
      filename: 'admin-process-modeler.html',
      template: 'admin-process-modeler.html',
      chunks: ['admin_process_modeler', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'admin-process.html',
      template: 'admin-process.html',
      chunks: ['admin_process', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'bewerben.html',
      template: 'bewerben.html',
      chunks: ['bewerben', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'bewerbungsportal.html',
      template: 'bewerbungsportal.html',
      chunks: ['bewerbungsportal', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'changePw.html',
      template: 'changePw.html',
      chunks: ['change_pw', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'cms.html',
      template: 'cms.html',
      chunks: ['cms', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'faq.html',
      template: 'faq.html',
      chunks: ['app', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'forgot_pw.html',
      template: 'forgot_pw.html',
      chunks: ['vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'impressum.html',
      template: 'impressum.html',
      chunks: ['app', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: 'index.html',
      chunks: ['index', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'prozess.html',
      template: 'prozess.html',
      chunks: ['app', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'task_detail_sgl.html',
      template: 'task_detail_sgl.html',
      chunks: ['task_detail_sgl', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'task_detail.html',
      template: 'task_detail.html',
      chunks: ['task_detail', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'task_overview.html',
      template: 'task_overview.html',
      chunks: ['task_overview', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'task_overview_sgl.html',
      template: 'task_overview_sgl.html',
      chunks: ['task_overview_sgl', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'verwaltung_auslandsamt.html',
      template: 'verwaltung_auslandsamt.html',
      chunks: ['nutzer', 'verwaltung_student', 'auslandsamt_tabelle', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'verwaltung_student.html',
      template: 'verwaltung_student.html',
      chunks: ['nutzer', 'verwaltung_student', 'vue'],
    }),
    new HtmlWebpackPlugin({
      filename: 'verwaltung_studiengangsleitung.html',
      template: 'verwaltung_studiengangsleitung.html',
      chunks: ['nutzer', 'studiengangsleitung_tabelle', 'vue'],
    }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: 'css/**/*',
        },
        {
          from: 'images/**/*',
        },
        {
          from: 'modals/**/*',
        },
        {
          from: 'videos/**/*',
        },
        {
          from: 'docs/**/*',
        },
        { from: 'node_modules/bootstrap/**/*' },
        { from: 'node_modules/jquery-ui-dist/**/*' },
        { from: 'node_modules/sweetalert2/**/*' },
        { from: 'node_modules/cookieconsent/**/*' },
        { from: 'node_modules/dropzone/**/*' },
        { from: 'node_modules/datatables.net-bs4/**/*' },
        { from: 'node_modules/jquery/**/*' },
        { from: 'node_modules/ckeditor4/**/*' },
        { from: 'node_modules/@fortawesome/**/*' },
        { from: 'node_modules/@fortawesome/fontawesome-free/webfonts/*', to: './webfonts/[name][ext]' },
      ],
    }),
    new VueLoaderPlugin(),
  ],
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        test: /\.html$/,
        use: [
          'html-loader',
          {
            loader: 'posthtml-loader',
            options: {
              plugins: [
                require('posthtml-modules')({}),
              ],
            },
          },
        ],
      },
      {
        test: require.resolve("jquery"),
        loader: "expose-loader",
        options: {
          exposes: {
            globalName: "$",
            override: true,
          },
        },
      },
      {
        test: /\.jpg/,
        type: 'asset/resource'
      },
    ],
  },
  resolve: {
    alias: {
      'vue': 'vue/dist/vue.esm-bundler.js',
      'jquery': "jquery/src/jquery"
    }
  }
};
