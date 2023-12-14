# GWT Eco System

## History

First and foremost, GWT is a Java to JavaScript transpiler. The basic idea is to use a type-safe and object oriented
Language to create applications which can ran natively in the browser. During development, developers use a
bullet-proofed
tooling. In production the transpiler creates highly optimized JavaScript.

In 2006, as GWT emerges, things were different than today. JavaScript really depends on the browser and Java were in an
early state
of evolution. That's one of the reasons why we have permutations and generators in GWT. Also there no third-party-libs.
So everything
had to be deployed with the framework. GWT 2 add a lot of new modules: editor, uibinder, etc. we all use today.

Today, things are different. Java has evolved. New language feature, annotation processors, etc. are state of the art
and
Browsers no longer have such gaps in there JavaScript engines. Third party libraries cover a lot of the things the
modules does
and they add new features to GWT. Also, Google transfers the lead the GWT project to an Open Source community.

Looking at the next generation transpiler from Google (J2CL) there are no longer permutations, generators or modules. It
is only a transpiler. In 2018 the community starts to migrate the GWT modules. These modules no longer use generators
(instead they use annotation processors) and instead of JSNI (JavaScript Native Interface) JsInterop. On the long term,
that will enable application to switch to the new transpiler.

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

When ever possible use these modules. There are only a few API changes, so migration might be easy by changing the package.
For example the old editor package is **com.google.gwt.editor** and the new one from the editor module will be
**org.gwtproject.editor**.

## Releases

GWT 2.10.0 removes the permutation for IE8, IE9 and IE10 and was the first release that had a new **groupId**. Besides 
the old groupId **com.google.gwt** it could also be loaded with the new one: **org.gwtproject**. Starting from 2.11.0 
GWT will only be available under **org.gwtproject**. 

GWT 2.11.0 will be the first release that requires Java 11!

Currently we have started the work on GWT 2.12.0. This release will focus on implementing new Java 11 - 17 Language features.

## Third Party Libraries

Besides the GWT framework many third party libraries occurred. These libraries add new features to GWT or offer different
approaches to do things in GWT. It might be worth to take look: 

* [Archetype Creation](tpl/archetype.html)
* [Client-Server Communication](tpl/communication.html)
* [Application frameworks](tpl/application.html)
* [Widget Libraries](tpl/widget.html)