/*global -$ */
'use strict';
// generated on 2015-04-25 using generator-gulp-webapp 0.3.0
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var browserSync = require('browser-sync');
var reload = browserSync.reload;

var distDir = '../main/resources/public';

$.help(gulp);

gulp.task('scripts', ['jshint'], function (cb) {
  var Builder = require('systemjs-builder');
  var builder = new Builder();

  builder.loadConfig('./config.js')
    .then(function () {
      builder.buildSFX('app/scripts/main', '.tmp/scripts/build.min.js', {minify: true, mangle: false})
        .then(function () {
          return cb();
        }).catch(function (err) {
          cb(new Error(err));
        });
    });
});

gulp.task('jshint', function () {
  return gulp.src('app/scripts/**/*.js')
    .pipe(reload({stream: true, once: true}))
    .pipe($.jshint())
    .pipe($.jshint.reporter('jshint-stylish'))
    .pipe($.if(!browserSync.active, $.jshint.reporter('fail')));
});

gulp.task('styles', function () {
  return gulp.src('app/styles/main.less')
    .pipe($.less())
    .pipe($.postcss([
      require('autoprefixer-core')({browsers: ['last 1 version']})
    ]))
    .pipe(gulp.dest('.tmp/styles'))
    .pipe(reload({stream: true}));
});

gulp.task('html', ['scripts', 'styles'], function () {
  var assets = $.useref.assets({searchPath: ['.tmp', 'app', '.']});

  return gulp.src('app/*.html')
    .pipe(assets)
    .pipe($.if('*.js', $.ngAnnotate()))
    .pipe($.if('*.css', $.csso()))
    .pipe($.rev())
    .pipe(assets.restore())
    .pipe($.useref())
    .pipe($.revReplace())
    .pipe($.if('*.html', $.minifyHtml({conditionals: true, loose: true, empty: true})))
    .pipe(gulp.dest(distDir));
});

gulp.task('views', function () {
  return gulp.src('app/views/**/*')
    .pipe($.minifyHtml({conditionals: true, loose: true, empty: true}))
    .pipe(gulp.dest(distDir + '/views'));
});

gulp.task('images', function () {
  return gulp.src('app/images/**/*')
    .pipe($.cache($.imagemin({
      progressive: true,
      interlaced: true,
      // don't remove IDs from SVGs, they are often used
      // as hooks for embedding and styling
      svgoPlugins: [{cleanupIDs: false}]
    })))
    .pipe(gulp.dest(distDir + '/images'));
});

gulp.task('fonts', function () {
  return gulp.src(['app/fonts/**/*.{eot,svg,ttf,woff,woff2}', 'jspm_packages/**/*.{eot,svg,ttf,woff,woff2}'])
    .pipe($.flatten())
    .pipe(gulp.dest('.tmp/fonts'))
    .pipe(gulp.dest(distDir + '/fonts'))
    .pipe($.size({title: 'fonts'}));
});

gulp.task('extras', function () {
  return gulp.src([
    'app/*.*',
    '!app/*.html'
  ], {
    dot: true
  }).pipe(gulp.dest(distDir));
});

gulp.task('apidocs', function () {
  return gulp.src('api-docs/**/*')
    .pipe(gulp.dest(distDir + '/api-docs'));
});

gulp.task('clean', require('del').bind(null, ['.tmp', distDir], {force: true}));

gulp.task('serve', ['styles', 'fonts'], function () {
  var proxy = require('proxy-middleware');
  var proxyOptions = require('url').parse('http://localhost:8888/api');
  proxyOptions.route = '/api';

  browserSync({
    notify: false,
    port: 9000,
    server: {
      baseDir: ['.tmp', 'app', '.'],
      routes: {
        '/jspm_packages': 'jspm_packages'
      },
      middleware: [proxy(proxyOptions)]
    }
  });

  // watch for changes
  gulp.watch([
    'app/*.html',
    'app/scripts/**/*.js',
    'app/images/**/*',
    '.tmp/fonts/**/*'
  ]).on('change', reload);

  gulp.watch('app/styles/**/*.less', ['styles']);
  gulp.watch('app/fonts/**/*', ['fonts']);
});

gulp.task('build', ['html', 'views', 'images', 'fonts', 'extras', 'apidocs'], function () {
  return gulp.src(distDir + '/**/*').pipe($.size({title: 'build', gzip: true}));
});

gulp.task('default', ['clean'], function () {
  gulp.start('build');
});
