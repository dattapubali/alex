# Developer docs

Some documents that are useful for developers.


## Setup ALEX for development

Make sure your machine has the following software installed:

1. Java JDK 8 or greater
2. Node.js v6.9.* or higher
3. Git
4. Maven 3.3.*

Clone the repository and install the frontend dependencies.

```bash
git clone https://github.com/LearnLib/alex.git
cd alex/main/src/main/webapp
npm install
```

Go back to the root folder and execute the following to install all java related dependencies:

```bash
mvn install [-DskipTests]
```

You are done! Add the optional parameter *-DskipTests* in order to skip the execution of frontend tests.


## Test ALEX

| Command                   | Description                                                 |
|---------------------------|-------------------------------------------------------------|
| `mvn test`                | Execute all backend unit tests                              |
| `mvn checkstyle:check`    | Check if the code style is according to the specifications  |


## Building release

To build a packaged version of ALEX from the source, navigate to the root folder of the repository and enter the following commands in the terminal:

```bash
mvn clean package [-DskipTests]
```

Afterwards the *ALEX-xxx.war* archive is created in the `main/target` directory.


## Command Line Arguments

alex.dbpath
:   The path where the HSQL DB is stored.
    E.g.: alex.dbpath=mem:testdb


## Deploy with Docker

Another way to test and / or release ALEX involves the tool [Docker][docker].
Please make sure that Docker is installed on your machine and that you have the right permissions to use it.
If this is the case just run:

    # must only be executed once!
    mvn install

    cd main
    mvn docker:build

Afterwards a new Docker image is created and you use it like any other Docker image, e.g.

    # lets take a look at all images
    docker images

    # run ALEX at http://localhost:8000
    docker run -d -p 8000:8080 alex:1.0-SNAPSHOT


[docker]: https://www.docker.com


## Frontend development

The frontend is developed with angular.js and uses the latest es6 features using the babel transpiler.
Styling is done with bootstrap and custom sass stylesheets.
All frontend files are in the webapp directory in _alex/main/src/main/webapp_.

### Requirements

* node.js v6.9.* or higher

### NPM commands

There are several npm commands to automate the development workflow.
In the _webapp_ folder, use the following commands.

| Command          | Description                                                 |
|------------------|-------------------------------------------------------------|
| `npm run build`  | Build all the application files                             |
| `npm run dev`    | Compile sass, html and js files every time they are changed |
| `npm test`       | Execute all unit tests                                      |


## Authentication with the REST API

At the moment, the REST interface supports authentication only via JSON Web Tokens (JWT).
Read more about this method and how to use them [here](http://jwt.io/).

1. Make a `POST` request to `/rest/users/login` with a HTTP body that contains a serialized user object which looks like `{"email":"<yourEmail>", "password":"<yourPassword>"}`.
2. You should receive a base64 encoded JWT. Save it somewhere and send it with each HTTP request to the API in the HTTP-Authorization header as follows: `Authorization: Bearer <theEncodedToken>`

The token provides information about the user as a base64 encoded JSON object as its payload.
It looks like `{"email": "<yourEmail>", "role": "<ROLE>"}` where the role is either _ADMIN_ or _REGISTERED_.

That is it.
Currently, the password is transferred in plain text since there is no HTTPS connection available for now, but we are working on it.
So be careful in which environment you use the application.