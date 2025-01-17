module.exports = function (grunt) {

    const libraries = [
        'node_modules/ace-builds/src/ace.js',
        'node_modules/angular/angular.js',
        'node_modules/angular-ui-ace/src/ui-ace.js',
        'node_modules/n3-charts/build/LineChart.js'
    ];

    const envifyDev = ['envify', {
        API_URL: 'http://localhost:8000/rest',
        global: true
    }];

    const envifyDist = ['envify', {
        API_URL: '/rest',
        global: true
    }];

    const browserifyOptionsDist = {
        files: {
            '<%= buildLocation %>/js/alex.bundle.js': ['src/js/index.ts']
        },
        options: {
            plugin: [
                ['tsify']
            ],
            transform: [
                ['stringify', { // load component templates
                    appliesTo: { includeExtensions: ['.html']}
                }]
            ]
        }
    };

    const browserifyOptionsDev = JSON.parse(JSON.stringify(browserifyOptionsDist));
    browserifyOptionsDev.options = Object.assign({
        watch: true,
        keepAlive: true,
        watchifyOptions: {
            entries: ['src/js/index.ts'],
            ignoreWatch: ['**/node_modules/**', '**/dist/**'],
            cache: {},
            packageCache: {},
            poll: true,
            delay: 500
        }
    }, browserifyOptionsDev.options);

    browserifyOptionsDev.options.transform.push(envifyDev);
    browserifyOptionsDist.options.transform.push(envifyDist);

    grunt
        .initConfig({
            buildLocation: './dist',

            pkg: grunt.file.readJSON('package.json'),

            uglify: {
                app: {
                    files: {
                        '<%= buildLocation %>/js/alex.bundle.js': ['<%= buildLocation %>/js/alex.bundle.js']
                    }
                },
                libs: {
                    files: {
                        '<%= buildLocation %>/js/libs.bundle.js': ['<%= buildLocation %>/js/libs.bundle.js']
                    }
                }
            },

            concat: {
                options: {
                    separator: ';\n'
                },
                libs: {
                    src: libraries,
                    dest: '<%= buildLocation %>/js/libs.bundle.js'
                }
            },

            watch: {
                sass: {
                    files: ['src/scss/**/*.scss'],
                    tasks: ['bundle-css'],
                    options: {
                        spawn: false
                    }
                }
            },

            cssmin: {
                target: {
                    files: {
                        '<%= buildLocation %>/css/style.css': [
                            'node_modules/n3-charts/build/LineChart.css',
                            'node_modules/angular-dragula/dist/dragula.min.css',
                            'node_modules/angular-toastr/dist/angular-toastr.min.css',
                            '<%= buildLocation %>/css/style.css'
                        ]
                    }
                }
            },

            postcss: {
                options: {
                    map: false,
                    processors: [
                        require('autoprefixer')({
                            browsers: 'last 2 versions'
                        })
                    ]
                },
                dist: {
                    src: '<%= buildLocation %>/css/style.css'
                }
            },

            browserify: {
                dev: browserifyOptionsDev,
                dist: browserifyOptionsDist
            },

            exec: {
                'build_css': 'node-sass src/scss/style.scss -o <%= buildLocation %>/css',
                'ng_annotate': 'ng-annotate -a -o <%= buildLocation %>/js/alex.bundle.js <%= buildLocation %>/js/alex.bundle.js'
            },

            copy: {
                fonts: {
                    expand: true,
                    cwd: 'node_modules/@fortawesome/fontawesome-free/webfonts',
                    src: '*',
                    dest: '<%= buildLocation %>/fonts',
                    filter: 'isFile'
                },
                images: {
                    expand: true,
                    cwd: 'assets/images',
                    src: '*',
                    dest: '<%= buildLocation %>/images',
                    filter: 'isFile'
                },
                index: {
                    src: 'index.html',
                    dest: '<%= buildLocation %>/index.html'
                }
            },

            concurrent: {
                dev: {
                    tasks: ['build:dev', 'watch:sass'],
                    options: {
                        logConcurrentOutput: true
                    }
                }
            }
        });

    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-postcss');
    grunt.loadNpmTasks('grunt-browserify');
    grunt.loadNpmTasks('grunt-exec');
    grunt.loadNpmTasks('grunt-concurrent');

    const build = ['concat:libs', 'uglify:libs', 'copy:fonts', 'bundle-css', 'copy:images', 'copy:index'];

    grunt.registerTask('bundle-js:dist', ['browserify:dist', 'exec:ng_annotate', 'uglify:app']);
    grunt.registerTask('bundle-js:dev', ['browserify:dev']);
    grunt.registerTask('bundle-css', ['exec:build_css', 'postcss', 'cssmin']);
    grunt.registerTask('build:dev', build.concat(['bundle-js:dev']));
    grunt.registerTask('build:dist', build.concat(['bundle-js:dist']));
    grunt.registerTask('dev', 'concurrent:dev');
    grunt.registerTask('build', 'build:dist');
};
