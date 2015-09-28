module.exports = function (grunt) {

    var scripts = [
        'app/modules/init.js',

        // core module
        'app/modules/core/init.js',
        'app/modules/core/constants.js',
        'app/modules/core/routes.js',
        'app/modules/core/controllers/*.js',
        'app/modules/core/directives/*.js',
        'app/modules/core/filters/*.js',
        'app/modules/core/models/*.js',
        'app/modules/core/resources/*.js',
        'app/modules/core/services/*.js',

        // actions module
        'app/modules/actions/init.js',
        'app/modules/actions/constants.js',
        'app/modules/actions/directives/**/*.js',
        'app/modules/actions/services/**/*.js',
        'app/modules/actions/models/**/*.js',

        // modals module
        'app/modules/modals/init.js',
        'app/modules/modals/controllers/*.js',
        'app/modules/modals/directives/*.js',
        'app/modules/modals/services/*.js'
    ];

    grunt
        .initConfig({
            pkg: grunt.file.readJSON('bower.json'),

            uglify: {
                options: {
                    banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
                },
                app: {
                    files: {
                        './app/app.min.js': ['./app/app.js']
                    }
                }
            },

            concat: {
                options: {
                    separator: ';\n'
                },
                app: {
                    src: ['app/templates.js', scripts],
                    dest: './app/app.js'
                },
                libs: {
                    src: [
                        'bower_components/angular/angular.min.js',
                        'bower_components/angular-ui-router/release/angular-ui-router.min.js',
                        'bower_components/angular-animate/angular-animate.min.js',
                        'bower_components/lodash/lodash.min.js',
                        'bower_components/angular-selection-model/dist/selection-model.min.js',
                        'bower_components/angular-ui-ace/ui-ace.min.js',
                        'bower_components/ace-builds/src-min/ace.js',
                        'bower_components/ace-builds/src-min/theme-eclipse.js',
                        'bower_components/ace-builds/src-min/mode-json.js',
                        'bower_components/ngtoast/dist/ngToast.min.js',
                        'bower_components/angular-sanitize/angular-sanitize.min.js',
                        'bower_components/angular-bootstrap/ui-bootstrap.min.js',
                        'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
                        'bower_components/d3/d3.min.js',
                        'bower_components/graphlib/dist/graphlib.core.min.js',
                        'bower_components/dagre/dist/dagre.core.min.js',
                        'bower_components/dagre-d3/dist/dagre-d3.core.min.js',
                        'bower_components/n3-line-chart/build/line-chart.min.js',
                        'bower_components/Sortable/Sortable.min.js',
                        'bower_components/Sortable/ng-sortable.js',
                        'bower_components/ng-file-upload/ng-file-upload.min.js',
                        'bower_components/angular-jwt/dist/angular-jwt.min.js'
                    ],
                    dest: 'app/libs.min.js'
                }
            },

            html2js: {
                options: {
                    useStrict: true,
                    base: '../webapp'
                },
                all: {
                    src: [
                        'app/modules/core/views/**/*.html',
                        'app/modules/actions/views/*.html',
                        'app/modules/modals/views/*.html'],
                    dest: 'app/templates.js'
                }
            },

            watch: {
                scripts: {
                    files: ['app/modules/**/*.js'],
                    tasks: ['build-js'],
                    options: {
                        spawn: false
                    }
                },
                sass: {
                    files: ['app/stylesheets/**/*.scss'],
                    tasks: ['build-css'],
                    options: {
                        spawn: false
                    }
                },
                html: {
                    files: ['app/modules/**/*.html'],
                    tasks: ['build-js'],
                    options: {
                        spawn: false
                    }
                }
            },

            sass: {
                options: {
                    sourceMap: false
                },
                dist: {
                    files: {
                        'app/stylesheets/style.css': 'app/stylesheets/style.scss'
                    }
                }
            },

            karma: {
                unit: {
                    configFile: 'tests/unit/karma.conf.js'
                }
            },

            cssmin: {
                target: {
                    files: {
                        'app/style.min.css': [
                            'bower_components/ngtoast/dist/ngToast.min.css',
                            'bower_components/codemirror/lib/codemirror.css',
                            'app/stylesheets/style.css'
                        ]
                    }
                }
            },

            clean: {
                js: ["app/templates.js"]
            },

            bower: {
                install: {
                    options: {
                        targetDir: './bower_components/',
                        verbose: true,
                        copy: false
                    }
                }
            },

            copy: {
                fonts: {
                    files: [
                        {
                            expand: true,
                            flatten: true,
                            src: ['bower_components/font-awesome/fonts/**'],
                            dest: 'app/fonts',
                            filter: 'isFile'
                        }
                    ]
                }
            },

            postcss: {
                options:  {
                    map: false,

                    processors : [
                        require('autoprefixer-core')({
                            browsers: 'last 2 versions'
                        })
                    ]
                },
                dist: {
                    src: 'app/stylesheets/style.css'
                }
            }
        });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-html2js');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-postcss');

    grunt.registerTask('build-js', ['html2js', 'concat', 'uglify', 'clean']);
    grunt.registerTask('build-css', ['sass', 'postcss', 'cssmin', 'copy:fonts']);
    grunt.registerTask('default', ['build-js', 'build-css']);
    grunt.registerTask('test-unit', ['karma']);
};
