# GWT Eco System

Today, the GWT eco system is not only that, what comes with the framework. Usually, projects use a lot of third party
dependencies to improve there development experience, add features or prefer a different approach to do things. 

But, before we start a little history of GWT, the current state and a foresight:

## History

First and foremost, GWT is a Java to JavaScript transpiler. The basic idea is to use a type-safe and object oriented
language to create applications which can ran natively in the browser. During development, developers benefit from the 
existing Java tool chain, powerful IDEs and awesome refactoring abilities. In production the transpiler creates highly
optimized and obfuscated JavaScript.

In 2006, as GWT emerges, things were different. JavaScript depends on the browser and Java were in an
early state of evolution. That's one of the reasons why we have permutations and generators in GWT. Also there no
third-party-dependencies. Everything had to be provided by the framework. With GWT 2,  a lot of new modules like: editor, uibinder, etc. 
were added to the SDK.

Today, things are different. Java has evolved. New language feature, annotation processors, etc. are state of the art
and the gaps of the JavaScript engines in the different browser are no longer a pain point. Third party dependencies add a lot of 
things that GWT does not cover or offer an alternative implementation of existing implementations.

Also, Google transfers the lead of the GWT project to an Open Source community.

Looking at the next generation transpiler from Google (J2CL) there are no longer permutations, generators or modules 
available. Today, it is only a transpiler. In preparation of using this new transpiler and keeping in mind the huge
amount of legacy applications the community starts to migrate the GWT modules in 2019. These migrated modules no longer 
use generators (instead they are using annotation processors) and JSNI (JavaScript Native Interface). Now, they are
using JsInterop. On the long term, using this new modules, will enable applications to switch to the new transpiler
very smoothly.

### List of Modules

<table class="columns"
       style="clear: left;">
    <thead>
    <th>Module</th>
    <th>Version (Release)</th>
    <th>Repo</th>
    </thead>
    <tbody>
    <tr>
        <td style="text-align:center">gwt-activity</td>
        <td style="text-align:center">-</td>
        <td>
            <a href="https://github.com/gwtproject/gwt-activity"
               target="_blank">
                https://github.com/gwtproject/gwt-activity
            </a>
        </td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-animation</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td>
            <a href="https://github.com/gwtproject/gwt-animation"
               target="_blank">
                https://github.com/gwtproject/gwt-animation
            </a>
        </td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-activity</td>
        <td style="text-align:center"> -</td>
        <td><a href="https://github.com/gwtproject/gwt-activity "
               target="_blank">
            https://github.com/gwtproject/gwt-activity</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-animation</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-animation "
               target="_blank">
            https://github.com/gwtproject/gwt-animation</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-aria</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-aria "
               target="_blank">
            https://github.com/gwtproject/gwt-aria</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-callback</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-callback "
               target="_blank">
            https://github.com/gwtproject/gwt-callback</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-core</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-core "
               target="_blank">
            https://github.com/gwtproject/gwt-core</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-dom</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-dom "
               target="_blank">
            https://github.com/gwtproject/gwt-dom</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-dom-style-definitions</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-dom-style-definitions"
               target="_blank">
            https://github.com/gwtproject/gwt-dom-style-definitions</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-editor</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-editor "
               target="_blank">
            https://github.com/gwtproject/gwt-editor</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-event</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-event "
               target="_blank">
            https://github.com/gwtproject/gwt-event</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-event-dom</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-event-dom "
               target="_blank">
            https://github.com/gwtproject/gwt-event-dom</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-geolocation</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-geolocation "
               target="_blank">
            https://github.com/gwtproject/gwt-geolocation</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-history</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-history "
               target="_blank">
            https://github.com/gwtproject/gwt-history</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-http</td>
        <td style="text-align:center">1.0.0-RC2</td>
        <td><a href="https://github.com/gwtproject/gwt-http "
               target="_blank">
            https://github.com/gwtproject/gwt-http</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-json</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-json "
               target="_blank">
            https://github.com/gwtproject/gwt-json</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-jsonp</td>
        <td style="text-align:center"> -</td>
        <td><a href="https://github.com/gwtproject/gwt-jsonp "
               target="_blank">
            https://github.com/gwtproject/gwt-jsonp</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-layout</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-layout "
               target="_blank">
            https://github.com/gwtproject/gwt-layout</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-places</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-places "
               target="_blank">
            https://github.com/gwtproject/gwt-places</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-regexp</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-regexp "
               target="_blank">
            https://github.com/gwtproject/gwt-regexp</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-safehtml</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-safehtml "
               target="_blank">
            https://github.com/gwtproject/gwt-safehtml</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-storage</td>
        <td style="text-align:center"> -</td>
        <td><a href="https://github.com/gwtproject/gwt-storage "
               target="_blank">
            https://github.com/gwtproject/gwt-storagel</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-timer</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-timer "
               target="_blank">
            https://github.com/gwtproject/gwt-timer</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-typedarrays</td>
        <td style="text-align:center">1.0.0-RC2</td>
        <td><a href="https://github.com/gwtproject/gwt-typedarrays "
               target="_blank">
            https://github.com/gwtproject/gwt-typedarrays</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-window</td>
        <td style="text-align:center">1.0.0-RC2</td>
        <td><a href="https://github.com/gwtproject/gwt-window "
               target="_blank">
            https://github.com/gwtproject/gwt-window</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-xhr</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-xhr "
               target="_blank">
            https://github.com/gwtproject/gwt-xhr</a></td>
    </tr>
    <tr>
        <td style="text-align:center">gwt-xml</td>
        <td style="text-align:center">1.0.0-RC1</td>
        <td><a href="https://github.com/gwtproject/gwt-xml "
               target="_blank">
            https://github.com/gwtproject/gwt-xml</a></td>
    </tr>
    </tbody>
</table>

When ever possible use these modules. There are only a few API changes, so migration might be easy by just changing the 
name of the package for those classes. For example, classes of the old editor package located in **com.google.gwt.editor**
are located inside **org.gwtproject.editor**.

## Releases

GWT 2.10.0 removes the permutation for IE8, IE9 and IE10 and was the first release that had a new **groupId**. Besides 
the old groupId **com.google.gwt**, GWT can also be loaded with the new groupId: **org.gwtproject**. Starting from 2.11.0 
GWT will only be available under the groupId of **org.gwtproject**. 

GWT 2.11.0 will be the first release that requires Java 11!

Currently we have started the work on GWT 2.12.0. This release will focus on implementing new Java Language features. See the [roadmap](/oadmap.html) for more information.

## Third Party Dependencies

Besides the GWT framework many third party dependencies occurred. These dependencies add new features to GWT or offer different
approaches to do something in GWT. It might be worth to take look: 

* [Archetype Creation](archetype.html)
* [Client-Server Communication, Validation & more](com-and-more.html)
* [Application frameworks](application.html)
* [Widget Libraries](ui.html)