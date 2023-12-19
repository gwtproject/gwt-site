# Creating GWT applications

The GWT framework comes with an application creator known as **webAppCreator**. This **webAppCreator** creates 
a simple Maven project with some test code. It is ready use, will launch and while running, do a RPC server call. All 
classes are located in one Maven project.

Besides the **webAppCreator** there are more application creator. Depending on your needs, this archetype creator might be 
a better choice for your needs.

## Application Creators

Here are a list of application creator. As far as we known, all of this projects are under active development.

### gwt-maven-archetypes<a id="create-third-party-tb"></a>

This archetype creator will generate a Maven based GWT project with separate modules for client-, shared- and server-code.
The server module is based on Jetty. In modern GWT development the separation of client-, shared- and server-code is the 
way to go. This archetype generator is a great place to start. 

For more information visit the archetype at [gwt-maven-archetypes at GitHub](https://github.com/tbroyer/gwt-maven-archetypes)
and follow the instructions. 

### gwt-maven-springboot-archetype<a id="create-third-party-nk"></a>

This archetype creator will generate a Maven based GWT project with separate modules for client-, shared- and server-code
similar to the one above, but in opposite to the first one, uses Spring Boot on the server side. In case
you are planing to use Spring Boot on the server side, this is a prefect archetype to start

For more information visit the archetype at [gwt-maven-springboot-archetype at GitHub](https://github.com/NaluKit/gwt-maven-springboot-archetype) 
and follow the instructions.

### domino-cli<a id="create-third-partydocli"></a>

The domino-cli provides a tool for generating application templates using the DominoKit tool stack.

For more information visit the archetype at [domino-cli at GitHub](https://github.com/DominoKit/domino-cli) 
and follow the instructions.

## Support

The GWT project team does not support this projects. In case you have questions or would like to open an issue, please
contact the contributor of the project!

## Missing Something?

If you are missing a library or a archetype creator from the list, please follow the instructions [here](add-lib.html) to add it.