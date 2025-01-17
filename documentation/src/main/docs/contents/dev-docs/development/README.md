# Develop

On this page, we present some information for developers and maintainers of ALEX.


## Requirements

Make sure you have the following software installed on your development machine:

1. Java JDK 8 or greater
2. Node.js v10 and NPM v6
3. Git
4. Maven 3.3.*


## Command line arguments

Make sure that you `mvn install`ed ALEX once before executing any of the following commands.

| Command                                 | Description                                                          |
|-----------------------------------------|----------------------------------------------------------------------|
| `mvn test`                              | Execute all backend tests.                                           |
| `mvn checkstyle:check -Pcode-analysis`  | Check if the code style is according to the specifications.          |
| `mvn spring-boot:run`                   | Start the REST API of ALEX.                                          |

In addition, the following parameters can be used for the build file or the `mvn spring-boot:run` command:

| Argument          | Description                                                                                           |
|-------------------|-------------------------------------------------------------------------------------------------------|
| `server.port`     | The path where ALEX should run `mvn spring-boot:run "-Dserver.port=8000"`                             |
| `alex.dbpath`     | The path where the HSQLDB is stored. `mvn spring-boot:run "-Dalex.dbpath=mem:testdb"`                 |
| `chromeDriver`    | The absolute path to the Chrome driver executable on your system                                      |
| `edgeDriver`      | The absolute path to the Edge driver executable on your system                                        |
| `firefoxDriver`   | The absolute path to the Gecko driver executable on your system                                       |
| `ieDriver`        | The absolute path to the Internet Explorer executable                                                 |
| `remoteDriver`    | The URI to the remote Selenium server                                                                 |
| `ltsmin.path`     | The path to the compiles binaries of the LTSmin library                                               |


## Frontend development

The frontend is developed with [AngularJS][angular] and written in Typescript.
Styling in ALEX is done with [Bootstrap v4][bootstrap] and SASS stylesheets.
All frontend files can be found in *frontend/src/main/javascript*.

### NPM commands

There are several NPM commands to automate the development workflow.
In the *frontend/src/main/javascript* directory, use the following commands:

| Command          | Description                                                                         |
|------------------|-------------------------------------------------------------------------------------|
| `npm run build`  | Build all the application files for production use                                  |
| `npm run dev`    | Compile sass, html and js files every time their content changes                    |
| `npm run serve`  | Serves the frontend at *http://localhost:8080*. For a custom port, add `-- -p XXXX` |


## Performing a release

### GitHub release

In the developer branch, perform the following steps:

1. Update the version, in the *pom.xml* files of all Maven modules and in the following files 
    * *src/main/javascript/package.json*
    * *src/main/javascript/environments.json*
2. Execute `mvn clean install package` in the root directory of the repository.
   This will generate the executable war file in the *build/target* directory.
3. Commit and push the changes to the developer branch.
4. Merge the developer branch in the master branch.
5. In the master branch, create a new tag with the new version and perform a GitHub release.
   Here, append the binary file generated from step 4.
6. In the developer branch, increment the version in all files from 1. and append the *-SNAPSHOT* suffix.
  
  

[angular]: https://angularjs.org/
[bootstrap]: https://getbootstrap.com/docs/4.3/
[docker]: https://www.docker.com
