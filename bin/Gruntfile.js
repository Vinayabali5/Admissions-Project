module.exports = function(grunt) {

    grunt.initConfig({
        jshint : {
            options : {
                globals : {
                    jQuery : true
                },
                ignores : ['js/all.js'],
            }, 
            files : [
                    'Gruntfile.js', 'js/**/*.js', 'js/**/*.js'
            ],
        },
        concat : {
            options : {
                separator : ';\n',
            },
            js : {
                src : [
                        'bower_components/jquery/dist/jquery.min.js', 
                        'bower_components/jquery-ui/jquery-ui.min.js', 
                        'bower_components/bootstrap/dist/js/bootstrap.min.js',
                        'bower_components/angular/angular.min.js', 
                        'bower_components/angular-route/angular-route.min.js', 
                ],
                dest : 'js/all.js',
            },
            css : {
                src : [
                        'bower_components/bootstrap/dist/css/bootstrap.min.css', 
                        'bower_components/bootstrap/dist/css/bootstrap-theme.min.css',
                ],
                dest : 'css/all.css',
            }
        },
        watch : {
            jshint : {
                files : [
                    '<%= jshint.files %>'
                ],
                tasks : [
                    'jshint'
                ]
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.registerTask('default', [
        'jshint'
    ]);

};