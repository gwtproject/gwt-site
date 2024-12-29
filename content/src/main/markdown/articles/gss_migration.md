# GSS Migration Guide

## Summary

With the GWT 2.8 Css in CssResource is being deprecated in favor of GSS and
applications are strongly encouraged to migrate to GSS.

To help adoption of GWT 2.8 the default for CssResource is still CSS. Once an
application has been migrated to 2.8 it can transition over to GSS in multiple
steps.

The [GSS open source](https://code.google.com/p/closure-stylesheets/) project has an overview of GSS features and here is a [list of difference between CSS and GSS](https://www.gwtproject.org/doc/latest/DevGuideGssVsCss.html).

This document outlines the steps one needs to take in order to move from CSS to
GSS.

Small applications want to use a one time migration, while big applications may
want to use the Gradual migration path.

## Configuration Properties

### Enable GSS

Enabling GSS will tell the GWT compiler to use GSS for all CssResource
interfaces. This flag can either be “false” (default) or “true”.


```xml
<set-configuration-property name="CssResource.enableGss" value="true" />
```

### Enable Autoconversion

If GSS is enabled and the GWT compiler finds a .css file, it will first convert
this file to gss and then feed it into GSS on the fly.


```xml
<set-configuration-property name="CssResource.conversionMode" value="strict" />
```

### Autoconversion modes

The autoconversion has three modes:

  * <strong>strict</strong> - In strict mode any error in your CSS will fail the compile. This is the mode
you want to be using
  * <strong>lenient</strong> - Sometimes you can not fix all of the CSS of an application at once, we added
a lenient mode, which tries to convert the CSS into something valid. Of course
this is very error prone and you do want to use strict mode.
  * <strong>off</strong> - Once you have converted all CSS into GSS you can turn auto conversion off.

## How well is GSS tested with GWT

All of Google’s application have been using GSS instead of CSS since mid 2015.

## Prerequisites

  * You cannot mix css and gss on the same method (@Source annotation).
<strong>This would be invalid:</strong>


```java
// Invalid use since css and gss are mixed one method
@Source({"def.css", "converted.gss"})
MyCssResource css();
```

  * For base types in the SDK  every css file has a gss equivalent.

## Gradual migration

  1. Run the conversion tool on a certain subdirectory of your app, this will create
     a .gss file for every .css file.
  2. Change all extensions on ClientBundle to .gss to use the new files.
  3. Verify that this part of the app is not visually broken
  4. Submit your change
  5. Continue until everything is changed
  6. Verify that the whole app works and that it is not using CSS Parser anymore,
turn off auto conversion:


```xml
<set-configuration-property name="CssResource.conversionMode" value="off" />
```

  7. Remove all .css
  8. Live happily ever after in CSS3 land

## One step migration

  1. Run the conversion tool on the main directory of your app, this will create a
.gss file for every .css file.
  2. Change all extensions on ClientBundle to .gss to use the new files
  3. Delete all .css files
  4. Verify your application
  5. Submit your code changes
  6. Turn off auto conversion for app:


```xml
<set-configuration-property name="CssResource.conversionMode" value="off" />
```

  7. Live happily ever after in CSS3 land

## UiBinder

Since UiBinder has no notion of a file extension we extended UiBinder to allow
for an extra attribute on the style tag. To use GSS in UiBinder you have to add <strong>gss=”true”</strong>.


```xml
<ui:style gss="true" >
  /* In here you can now use GSS */
</ui:style>
```

Note: Like on CssResource css and gss can not be mixed here. If you importing a
.gss file in your UiBinder file you will need to enable gss and also write Gss
inside of the style element:


```xml
<ui:style gss="true" src="foo.gss">
  /* Need to use GSS here now! */
</ui:style>
/* This is invalid! */
<ui:style gss="true" src="foo.css">
</ui:style>
```

### Setting the default in UiBinder

You can also control the default for CSS / GSS in all UiBinder fields by
setting a configuration property:


```xml
<set-configuration-property name="CssResource.gssDefaultInUiBinder" value="true" />
```

Note: This only applies of there is no gss attribute on the given style
element.

## Css conversion tool - Css2Gss

The Css conversion tool Css2Gss lets you automatically convert your css files
to gss. The same underlying tool is used by the GWT compiler to convert css to
gss on the fly.

Note: <strong>Css2Gss only supports strict mode</strong> in conversion and will fail to convert invalid css files. We can not
accurately convert broken css in all cases so we rather not try to convert it
at all.

You can run the tool like this:


```shell
java -cp gwt-user.jar com.google.gwt.resources.converter.Css2Gss
fileToConvert.css
```

### Known conversion tool issues

#### Comments are removed

Since the old flute css parser does not give us any access to these comments,
there is nothing we can do about it. You will need to manually re add comments
as needed.

#### Files depending on other files @def’s fail conversion

Since the converter needs to convert all variables to a GSS compatible format,
it needs to be aware of all variables that are defined in scope. It therefore
refuses to convert if it encounters an undefined variable.

In order to get around this limitation the converter allows you to specify a
list of css files to be parsed for building up scope:


```shell
java -cp gwt-user.jar com.google.gwt.resources.converter.Css2Gss -scope
foo.css,bar.css fileToConvert.css
```

### Known issues with GSS

#### Running your gwt host page in quirks mode

Running your host page in quirks mode will make GSS fail. If browsers are
running in quirks mode they are considering .a and .A to be the same selector.
GSS is using upper and lower case characters for different classes and you will
see collisions in quirks mode due to this. Since it has been illegal to run GWT
applications in quirks mode for a long time, you will need to move your
application over to “HTML 5” by adding <!doctype html> in your host page.

