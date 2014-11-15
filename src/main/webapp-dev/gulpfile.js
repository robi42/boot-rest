'use strict';
// generated on 2014-06-07 using generator-gulp-webapp 0.1.0

var gulp = require('gulp');
var gutil = require('gulp-util');

var distDir = '../webapp';

// Load plugins
var $ = require('gulp-load-plugins')();

gulp.task('styles', function () {
  return gulp.src('app/styles/main.less')
    .pipe($.less())
    .pipe($.autoprefixer('last 1 version'))
    .pipe(gulp.dest('.tmp/styles'));
});

gulp.task('scripts', function () {
  return gulp.src('app/scripts/**/*.js')
    .pipe($.jshint())
    .pipe($.jshint.reporter(require('jshint-stylish')))
    .pipe($.jshint.reporter('fail'))
    .pipe($.ngAnnotate());
});

gulp.task('html', ['styles', 'scripts'], function () {
  var assets = $.useref.assets({searchPath: '{.tmp,app}'});

  return gulp.src('app/*.html')
    .pipe(assets)
    .pipe($.if('*.js', $.uglify({mangle: false})))
    .pipe($.if('*.css', $.csso()))
    .pipe($.rev())
    .pipe(assets.restore())
    .pipe($.useref())
    .pipe($.revReplace())
    .pipe($.if('*.html', $.minifyHtml({conditionals: true, loose: true, empty: true})))
    .pipe(gulp.dest(distDir))
    .pipe($.size());
});

gulp.task('views', function () {
  return gulp.src('app/views/**/*')
    .pipe($.minifyHtml({conditionals: true, loose: true, empty: true}))
    .pipe(gulp.dest(distDir + '/views'));
});

gulp.task('images', function () {
  var imageDest = distDir + '/images';

    return gulp.src('app/images/**/*')
    .pipe($.newer(imageDest))
    .pipe($.imagemin({
      optimizationLevel: 3,
      progressive: true,
      interlaced: true
    }))
    .pipe(gulp.dest(imageDest));
});

gulp.task('fonts', function () {
  return gulp.src(require('main-bower-files')().concat('app/fonts/**/*'))
    .pipe($.filter('**/*.{eot,svg,ttf,woff}'))
    .pipe($.flatten())
    .pipe(gulp.dest(distDir + '/fonts'));
});

gulp.task('fontawesome', function () {
  return gulp.src('app/bower_components/components-font-awesome/fonts/**/*')
    .pipe(gulp.dest(distDir + '/fonts'));
});

gulp.task('extras', function () {
  return gulp.src(['app/*.*', '!app/*.html'], {dot: true})
    .pipe(gulp.dest(distDir));
});

gulp.task('apidocs', function () {
  return gulp.src('api-docs/**/*')
    .pipe(gulp.dest(distDir + '/api-docs'));
});

gulp.task('test', function () {
  require('karma').server.start({configFile: __dirname + '/karma.conf.js', singleRun: true}, function (exitCode) {
    gutil.log('Karma has exited with ' + exitCode);
    process.exit(exitCode);
  });
});

gulp.task('clean', require('del').bind(null, ['.tmp', distDir], {force: true}));

gulp.task('build', ['test', 'html', 'views', 'images', 'fonts', 'fontawesome', 'extras', 'apidocs']);

gulp.task('default', ['clean'], function () {
  gulp.start('build');
});

gulp.task('connect', function () {
  var proxy = require('proxy-middleware');
  var url = require('url');
  var proxyOptions = url.parse('http://localhost:8888/api');
  proxyOptions.route = '/api';

  var serveStatic = require('serve-static');
  var serveIndex = require('serve-index');
  var app = require('connect')()
    .use(require('connect-livereload')({port: 35729}))
    .use(proxy(proxyOptions))
    .use(serveStatic('app'))
    .use(serveStatic('.tmp'))
    // paths to bower_components should be relative to the current file
    // e.g. in app/index.html you should use ../bower_components
    .use('/bower_components', serveStatic('bower_components'))
    .use(serveIndex('app'));

  require('http').createServer(app)
    .listen(9000)
    .on('listening', function () {
      console.log('Started connect web server on http://localhost:9000');
    });
});

gulp.task('serve', ['connect', 'styles'], function () {
  require('opn')('http://localhost:9000');
});

// Inject Bower components
gulp.task('wiredep', function () {
  var wiredep = require('wiredep').stream;

  gulp.src('app/styles/*.less')
    .pipe(wiredep({
      directory: 'app/bower_components'
    }))
    .pipe(gulp.dest('app/styles'));

  gulp.src('app/*.html')
    .pipe(wiredep({
      directory: 'app/bower_components'
    }))
    .pipe(gulp.dest('app'));
});

gulp.task('watch', ['connect', 'serve'], function () {
  var server = $.livereload();

  // Watch for changes
  gulp.watch([
    'app/*.html',
    'app/views/**/*.html',
    '.tmp/styles/**/*.css',
    'app/scripts/**/*.js',
    'app/images/**/*'
  ]).on('change', function (file) {
    server.changed(file.path);
  });

  gulp.watch('app/styles/**/*.less', ['styles']);
  gulp.watch('app/scripts/**/*.js', ['scripts']);
  gulp.watch('app/images/**/*', ['images']);
  gulp.watch('bower.json', ['wiredep']);
});
