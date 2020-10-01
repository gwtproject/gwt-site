# GWT Documentation

* GWT documentation is published under : http://www.gwtproject.org/doc/latest/DevGuide.html

## Reference

* Markdown processor: https://github.com/sirthias/pegdown

## Adding content

* See: http://www.gwtproject.org/makinggwtbetter.html#webpage

## Building

If you have Grunt installed :
* build the assets using Grunt: `grunt`
* then run: `mvn clean install`
* after that you will find the generated documentation in `target/generated-site/`.

If you don't have Grunt installer :
* build the assets using Maven and Grunt plugin: `mvn clean install -Pgrunt`
* after that you will find the generated documentation in `target/generated-site/`.

### Running locally
Run the site locally for easy visual testing

Run without server:
* Change to the `target/generated-site` folder.
* Open the `index.html` file in your browser.

Run with Maven:
* Run: `mvn jetty:run`
* Open URL `http://localhost:9999` in your browser.

Happy Hacking :)
