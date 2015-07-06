# Building a modern GWT app using Polymer Elements:

## Introduction

In this tutorial, you'll write a **TodoList** application using [Web Components](https://en.wikipedia.org/wiki/Web_Components) which defines collection of standards which allow us to bundle markup and styles into custom HTML elements.

In our case we want to fulfill the [Material Design](http://www.google.es/design/spec/material-design/introduction.html) specification, thus we will use Polymer [Iron and Paper](https://elements.polymer-project.org/) element collections through the Vaadin [gwt-polymer-elements](https://github.com/vaadin/gwt-polymer-elements) library, which is a `JsInterop` wrapper for Iron and Paper elements and has been produced using the Vaadin [gwt-api-generator](https://github.com/vaadin/gwt-api-generator).

Before starting the tutorial you might try the live [TodoList][1] application to check how the application should look like when you finish.

[<img class='polymer-tutorial-mobile' src='images/todo-list-01.png'>
 <img class='polymer-tutorial-mobile' src='images/todo-list-02.png'>][1]

[1]: http://manolo.github.io/gwt-polymer-todo-list/demo/TodoListWidgets.html

## The Learning process

In the process of building the **TodoList** app, you'll learn:

* To create a new maven project.
* To run your project in SuperDevMode
* How to add external libraries to your application.
* To configure the project to use the experimental `JsInterop` mode.

Then you have to select a way to continue with your development: using classic **Widgets** or  `JsInterop` **Elements** which is the new tendency.

Either option you select will teach you:

* To create new widgets using `UiBinder`
* To import and use Polymer web components.
* How to deal with responsive layouts.
* Style elements in `UiBinder`
* Add events handlers to UiBinder components
* How to use a Basic Data Model.

## What's next

[Step 1: Create and prepare a new Project](create.html)
