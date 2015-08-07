# Creating the *TodoList* Project

In this section, we'll create the **TodoList** project from scratch using GWT's [webAppCreator](http://www.gwtproject.org/doc/latest/RefCommandLineTools.html#webAppCreator), a command-line utility.

Before we start, make sure to download the most recent [GWT distribution](../../../download.html) and install [Maven](https://maven.apache.org/).

## Using webAppCreator

The `webAppCreator` is a command-line tool included in the GWT SDK. It generates the project structure necessary to get started. It also creates a starter application, which you can run to ensure that the components are created and linked successfully.

As you develop your software, you'll replace the code in the starter application with yours.

For the **TodoList** project, we'll need to run `webAppCreator` with the following parameters:


| Parameter  | Definition                                     | Example      |
| ---------- | -----------------------------------------------| ------------ |
| -templates | Comma separated templates to use               | maven,sample |
| -out       | The directory to place the generated files.    | TodoList     |
| moduleName | The name of the GWT module you want to create. | org.gwtproject.tutorial.TodoList |


## Setting up a new project.

1. Create the **TodoList** application.

      GWT webAppCreator will generate the project structure and the build script (maven pom.xml).

        $ /full_path_to_gwt_sdk/webAppCreator \
            -templates maven,sample \
            -out TodoListApp \
            org.gwtproject.tutorial.TodoList

      _**Tip**: If you include the GWT SDK folder in your PATH environment variable, you won't have to specify the full path._

      Before you can run the application you have to modify the `pom.xml`. Add `<type>pom</type>` to the gwt dependency, otherwise you will encounter an error. finally, include the `maven-war-plugin` and `maven-resources-plugin` and configure them so the `TodoList.html` will be copied to the right place. your pom.xml should look like this:

        <?xml version="1.0" encoding="UTF-8"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

          <!-- POM file generated with GWT webAppCreator -->
          <modelVersion>4.0.0</modelVersion>
          <groupId>org.gwtproject.tutorial</groupId>
          <artifactId>TodoList</artifactId>
          <packaging>war</packaging>
          <version>1.0-SNAPSHOT</version>
          <name>org.gwtproject.tutorial.TodoList</name>
          
          <properties>
            <!-- Convenience property to set the GWT version -->
            <gwtVersion>2.7.0</gwtVersion>
            <!-- Note: GWT needs at least java 1.6 -->
            <maven.compiler.source>1.7</maven.compiler.source>
            <maven.compiler.target>1.7</maven.compiler.target>
          </properties>

          <dependencyManagement>
            <dependencies>
              <!-- ensure all GWT deps use the same version (unless overridden) -->
              <dependency>
                <groupId>com.google.gwt</groupId>
                <artifactId>gwt</artifactId>
                <version>${gwtVersion}</version>
                <scope>import</scope>
                <type>pom</type>
              </dependency>
            </dependencies>
          </dependencyManagement>

          <dependencies>
            <dependency>
              <groupId>com.google.gwt</groupId>
              <artifactId>gwt-servlet</artifactId>
              <scope>runtime</scope>
            </dependency>
            <dependency>
              <groupId>com.google.gwt</groupId>
              <artifactId>gwt-user</artifactId>
              <scope>provided</scope>
            </dependency>
            <dependency>
              <groupId>com.google.gwt</groupId>
              <artifactId>gwt-dev</artifactId>
              <scope>provided</scope>
            </dependency>
            <dependency>
              <groupId>com.google.gwt</groupId>
              <artifactId>gwt-codeserver</artifactId>
              <scope>provided</scope>
            </dependency>
            <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.11</version>
              <scope>test</scope>
            </dependency>
          </dependencies>

          <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-war-plugin</artifactId>
                    <version>2.1</version>
                    <configuration>
                        <warSourceDirectory>src/main/webapp</warSourceDirectory>
                        <webXml>src/main/webapp/WEB-INF/web.xml</webXml>
                    </configuration>
                </plugin>
                <plugin>
                  <artifactId>maven-resources-plugin</artifactId>
                  <executions>
                    <execution>
                      <id>copy-resources</id>
                      <phase>generate-sources</phase>
                      <goals>
                        <goal>copy-resources</goal>
                      </goals>
                      <configuration>
                        <outputDirectory>${project.build.directory}/${project.build.finalName}</outputDirectory>
                        <resources>
                          <resource>
                           <directory>src/main/webapp</directory>
                           <filtering>true</filtering>
                          </resource>
                        </resources>
                      </configuration>
                    </execution>
                  </executions>
                </plugin>
              <!-- GWT Maven Plugin-->
              <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>gwt-maven-plugin</artifactId>
                <version>${gwtVersion}</version>
                <executions>
                  <execution>
                    <goals>
                      <goal>compile</goal>
                    </goals>
                  </execution>
                  <execution>
                    <!-- Run tests through gwt:test, this allows reusing the plugin configuration for GWTTestCases -->
                    <id>gwt-tests</id>
                    <phase>test</phase>
                    <goals>
                      <goal>test</goal>
                    </goals>
                  </execution>
                </executions>
                <!-- Plugin configuration. There are many available options, 
                     see gwt-maven-plugin documentation at codehaus.org --> 
                <configuration>
                  <modules>
                    <module>org.gwtproject.tutorial.TodoList</module>
                  </modules>
                  <!-- URL that should be opened by DevMode (gwt:run). -->
                  <runTarget>TodoList.html</runTarget>
                  <!-- Ask GWT to create the Story of Your Compile (SOYC) (gwt:compile) -->
                  <compileReport>true</compileReport>
                  <!-- Run tests using HtmlUnit -->
                  <mode>htmlunit</mode>
                  <!-- Tests patterns -->
                  <includes>**/Test*.java,**/*Test.java,**/*TestCase.java</includes>
                </configuration>
              </plugin>

              <!-- Skip normal test execution, we use gwt:test instead -->
              <plugin>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.17</version>
                <configuration>
                  <skip>true</skip>
                </configuration>
              </plugin>
            </plugins>
          </build>
        </project>

2. Run the application in [SuperDevMode](articles/superdevmode.html).

     To check that the project was created correctly start the new app in SuperDevMode.

        $ cd TodoListApp
        $ mvn gwt:run

      _**Tip**: Since the created project is built with Maven, you can import it in Eclipse, IDEA, etc._

3. Launch your Browser.

    In the GWT developer window, press "Launch Default Browser" to launch the application. Alternatively, you can click "Copy to Clipboard" and paste the URL into any browser.

    If you change something in the code, you can recompile the application by simply reloading the web page. If you change configuration files, e.g. pom.xml or static content in webapp, you might have to restart SuperDevMode. `Ctrl+C` and `mvn gwt:run` stops and starts the execution, respectively.

## Customizing your project

With the base project set up, we'll now add the necessary external dependencies. At the same time, we'll also remove some of the files and dependencies that are set up and generated by default when the starter application was built.


1. Add the vaadin `gwt-polymer-elements` dependency to your project by editing the `pom.xml` file.

        <dependency>
         <groupId>com.vaadin.polymer</groupId>
         <artifactId>vaadin-gwt-polymer-elements</artifactId>
         <version>${gwtPolymerVersion}</version>
         <scope>provided</scope>
        </dependency>

     _**Note**: Replace the `${gwtPolymerVersion}` placeholder with the current version (as of this writing 1.0.2.0-alpha3) or add the corresponding property in your pom.xml_

2. Update the gwt-maven-plugin configuration to support the experimental `JsInterop` feature.

        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>gwt-maven-plugin</artifactId>
          ...
          <configuration>
            <jsInteropMode>JS</jsInteropMode>
            ...
          </configuration>
        </plugin>

      _**Note**: JsInterop is an experimental flag in GWT-2.7.0 and you need to enable it explicitly. In future versions of GWT it will be enabled by default._

3. Update `TodoList.gwt.xml` module file so that we can use the new gwt library.

        <module rename-to="todolist">
          ...
          <inherits name="com.vaadin.polymer.Elements"/>
          ...
        </module>

4. Update `TodoList.html`
    * Configure the `<meta>` viewport to handle mobile layouting.
    * Import the polyfill `<script>` for non web-component capable browsers.
    * Remove the content inside the <body> tag.

            <!doctype html>
            <html>
            <head>
             <meta name="viewport"
               content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1" />
             <script src="todo/bower_components/webcomponentsjs/webcomponents.js"></script>
             <script type="text/javascript" src="todo/todo.nocache.js"></script>
            </head>
            <body>
            </body>
            </html>

5. Remove `greetServlet` and its mapping in `WEB-INF/web-xml`

        <web-app>
        </web-app>

6. Remove all unnecessary files.

    * Remove the folder and shared folders located in `src/main/java/org/gwtproject/tutorial`.
    * Remove `GreetingService.java` and `GreetingServiceAsync.java` from the `client` package.
    * Remove the example tests in `src/main/test`.

7. Update the **EntryPoint**.

    Replace the content of `TodoList.java` with

        package org.gwtproject.tutorial.client;

        import com.google.gwt.core.client.EntryPoint;
        import com.google.gwt.user.client.ui.RootPanel;
        import com.vaadin.polymer.paper.widget.PaperButton;

        public class TodoList implements EntryPoint {
          public void onModuleLoad() {
            // Use Widget API to Create a <paper-button>
            PaperButton button = new PaperButton("Press me!");
            button.setRaised(true);
            RootPanel.get().add(button);
          }
        }

    _**Note**: The example above shows how to add a  `PaperButton` element using the Widgets API._

8. Run the application again.

    You should see a web page containing a Material Design button.

## What's next

In this lesson we learned how to:

- Create a new GWT maven project from scratch.
- Run and debug our application in SuperDevMode
- Add external dependencies to our project.
- Configure our project to use the experimental `JsInterop` mode.
- Replace the starter application code with our own.

We're now prepared to design the UI of the **TodoList** application. There are two ways we can go about it: using GWT widgets (classic) or the Elements API (modern).

[Step 2a: Building the User Interface using Widgets](widgets-buildui.html)

[Step 2b: Building the User Interface using Elements](elements-buildui.html)

[**DISCLAIMER**](introduction.html#pol-disclaimer)
