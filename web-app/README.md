# gwt-site-webapp
This project includes the client code of the GWT site. It does not include the
site content which is in the gwt-site project nor the utilities to create and
deploy with samples and plugin releases.

## Requirements
[Apache Maven](http://maven.apache.org) 3.8 or greater, and JDK 11+ in order to run.

To build the project, invoke the following command:

    $ mvn verify

This only tests and builds the JS to enhance the site content. To build the contents
first, you need to build the gwt-site project.

1. Check out the [gwt-site](https://github.com/gwtproject/gwt-site) project.
2. Build the gwt-site project:

        $ cd [...]/gwt-site
        $ mvn install

3. Build the gwt-site-webapp project, with the -Pfull-site profile:

        $ cd [...]/gwt-site-webapp
        $ mvn verify -Pfull-site

Now you can point your browser to the `target/www` folder, or you can serve
its content with any web-server (i.e [serve](https://www.npmjs.com/package/serve) or
[http.server](https://docs.python.org/3/library/http.server.html)).
Notice that an http server is needed if you want to test site navigation via Ajax.

Building will run the **tests**, but to explicitly run them you can use the test target

        $ mvn test

# Develop and Deploy

Note that as configured, sourcemaps will not work locally - they must be deployed to gwtproject.org
because the generated JS will attempt to load them from an absolute URL.

  * The `target/gwt-site-webapp-<version>.war` file (and the `target/gwt-site-webapp-<version>`
  directory) contains the generated JavaScript and sourcemaps, and can be deployed along with the
  generated site content.
  * The `target/www` directory contains the generated JavaScript, sourcemaps (and Java sources),
  and the generated HTML from gwt-site itself, and is suitable for deployment directly to a
  server.
