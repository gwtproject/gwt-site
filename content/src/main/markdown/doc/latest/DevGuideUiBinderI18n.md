# i18n UIBinder

This document explores the internationalization features of <a href="/javadoc/latest/com/google/gwt/uibinder/client/UiBinder.html">UiBinder</a>
templates. More general information about UiBinder can be found in <a href="DevGuideUiBinder.html">Declarative Layout with UiBinder</a>. 

1.  [Background](#Background)
2.  [Bonjour, Tout Le Monde](#Bonjour)
3.  [Simple HTML Tags Inside a Message](#Simple_HTML_tags_inside_a_message)
4.  [Messages with Unclobberable Portions](#Messages_with_unclobberable_portions)
5.  [Messages with Values Computed at Runtime](#Messages_with_runtime_computed_values)
6.  [Messages Containing Widgets (HTMLPanel Only)](#Messages_containing_widgets)
7.  [HTML Attributes that Need Translation](#HTML_attributes_that_need_translation)
8.  [Words with Multiple Meanings](#Words_with_Multiple_Meanings)

## Background<a id="Background"></a>

UiBinder templates can be marked up for localization. You use the
`<ui:msg>` and `<ui:attribute>` elements to indicate which portions
of the template should be translated, then provide properties
files with localized versions of the messages when building your app.

As in the main <a href="DevGuideUiBinder.html">UiBinder
page</a>, the rest of this page explains how to make your UI templates
localizable through a series of typical use cases.

**Note** You will see a lot of parallels to working with the [Messages](DevGuideI18nMessages.html) system, and with good reason: UiBinder's I18n features are implemented by generating a hidden [`com.google.gwt.i18n.client.Messages`](/javadoc/latest/com/google/gwt/i18n/client/Messages.html) interface for each template. Except for plural forms, anything you can do via Messages you should also be able to do in a template.
 
## Bonjour, Tout Le Monde<a id="Bonjour"></a>

Here's how to turn the feature on.

**Original**

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'>
  <div>Hello, world.</div>
</ui:UiBinder>
```

**Tagged**

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
    ui:generateFormat='com.google.gwt.i18n.server.PropertyCatalogFactory'
    ui:generateKeys="com.google.gwt.i18n.server.keygen.MD5KeyGenerator"
    ui:generateLocales="default">
  <div><ui:msg description="Greeting">Hello, world.</ui:msg></div>
</ui:UiBinder>
```

We've done two things here. We've configured UiBinder's I18N features
by adding a few attributes to the root `<ui:UiBinder>` element,
and we've tagged our text as being an individual message that needs translation.

First look at our "Hello, world." text. By putting it in
a `<ui:msg>` element we've identified it as a single
piece of translatable text, a _message_. The message's
`description` attribute will accompany the message on its way to the
translator to explain its use. The description may well be the only
bit of context the translator sees, so even though it's an optional
attribute you really should always provide one.

Now that we have something that needs to be translated, we set our
configuration to say how it should be done via attributes on the
root `<ui:UiBinder>` element. These `ui:generate*`
attributes correspond to the arguments of LocalizableResource's
[@Generate](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Generate.html)
annotation. Here's what they mean.

<dl>
  <dt>`ui:generateFormat`
  <dd>We want a file in the java properties
format to be generated. 

   <dt>`ui:generateKeys`
   <dd>We want the property keys to be MD5 hashes of the message
     content, so that the translations will survive as the messages
     move around within the template. (And if we take some care with
     how we manage our translated properties files, a message that
     happens to be used in more than one template will only need to be
     translated once.)

   <dt>`ui:generateLocales`
   <dd>And we want the
generated properties to be part of the default locale. (Read more
about the default locale and fallback properties
in [Locales in GWT](DevGuideI18nLocale.html).)
</dl>

When you compile your application, pass the -extras argument to 
the gwt compiler to tell it to generate its "extra" auxiliary files.
A properties file will be generated for each template, containing
an entry for each message tagged for localization, something like:

```properties
# Generated from my.app.HelloWorldMyBinderImplGenMessages
# for locale default

# Description: Greeting
022A824F26735ED0582324BE34F3CAE1=Hello, world.

```

The names of the generated files are a bit unfortunate, based on
the UiBinder interfaces you declare. For example, if this template is
used by `com.example.app.client.Hello.Binder`; in a module
named `App`; and you've run the gwt compiler with `-extra
/tmp/gwt-extras`; you'll find the file in
`/tmp/gwt-extras/com.example.app.App/com.example.app.client.HelloBinderImplGenMessages.properties`. No
kidding. Hopefully this will be cleaned up in a future release of the
toolkit.

Your translators can then create localized versions of this file
that can sit next to your ui.xml file. Continuing the example at hand,
a Mexican Spanish version of this properties file would be named
`HelloBinderImplGenMessages_es_MX.properties` (note that the
"com.example..." prefix is dropped).

You don't have to use md5 keys. If you prefer to create your own,
the `<ui:msg>` element accepts a `key` attribute
to let you do just that. On the other hand, if you stick with md5 keys
you can gather all of your app's translations for a particular locale
into a single file, rather than keeping them scattered throughout your
code base.  The magic name
is `LocalizableResource_<locale>.properties`, and you
must put it in package `com/google/gwt/i18n/client`. (This
works because i18n property file lookup walks up the class inheritance
tree, and all Messages interfaces descend from LocalizableResource.)

Some projects use scripts to maintain these unified translation files.
See this [wiki
entry](http://code.google.com/p/puzzlebazar/wiki/UiBinderInternationalisation) from the puzzlebazar project for an example.

There are are a couple of other I18N attributes that can be set on
the `<ui:UiBinder>` element, corresponding to other
Localizable annotations, but you'll rarely change them from their
default values.

<dl>
  <dt>`ui:defaultLocale`
  <dd>See <a href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.DefaultLocale.html"
    >@DefaultLocale</a> for details
  <dt>`ui:generateFilename`
  <dd>See <a href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Generate.html#fileName"
    >@Generate</a>(fileName = "...") for details
</dl>
<dl>
  <dt>`ui:baseMessagesInterface`
  <dd>Sets the base interface to use for generated messages.  The value must
  be the fully-qualified class name of an interface that extends [`Messages`](/javadoc/latest/com/google/gwt/i18n/client/Messages.html).
  You can then put whatever annotations you want there, making it easy to have
  company or project-wide settings that can be changed in just one place.  You
  can still use the other attributes to override defaults inherited from that
  interface if necessary.
</dl>

## Simple HTML Tags Inside a Message<a id="Simple_HTML_tags_inside_a_message"></a>

A message element in a context where HTML is appropriate can hold HTML markup. 

**Original**

```html
We <b>strongly</b> urge you to reconsider.
```

*Tagged*

```xml
<ui:msg>We <b>strongly</b> urge you to reconsider.</ui:msg>
```

Simple formatting is reasonable to put in front of a translator,
and so UiBinder supports HTML in messages, not just text.

## Messages with Unclobberable Portions<a id="Messages_with_unclobberable_portions"></a>

Sometimes you will need to put opaque blocks inside of a message,
to keep your translators from breaking your app.

**Original**

```html
<!-- Uh oh, don't want translator to mess up brand CSS or the trademark span -->

<div><span class="brand">Colgate</span>, with MFP!<span class="tm">TM</span></div>
```

**Tagged**

```xml
<div>
  <ui:msg description="blurb"><span class="brand" ui:ph="brandedSpan">Colgate</span>,
  with MFP!<ui:ph name="trademark"><span class="tm">TM</span></ui:ph></ui:msg>
</div>
```

**Generated**

```properties
# Description: blurb
# 0=arg0 (Example: <span class='brand'>), 1=arg1 (Example: </span>), 2=arg2 (Example: <span class='tm'>TM</span>)
6E8B421C6A7C1FEAE23FAA9D43C90D5E={0}Colgate{1}, with MFP\!{2}

```

There are two examples in here. First, you see a `ui:ph` attribute that can be added to any child of a `ui:msg`, to indicate that _placeholders_ should be created to protect it from translators. Two placeholders are created, for the opening and closing tags of the element (in this case, `brandedSpanOpen` and `brandedSpanClose`).

Second, we see an element, also named `ui:ph`, that can surround an arbitrary bit of markup to be protected in its entirety (in this case, the trademark placeholder).

So, you have both an element to surround untranslatable runs: **`<ui:ph>` **don't
translate** `</ui:ph>`**, and an attribute to put in arbitrary elements to hide their begin and
end tags from translators, but keep their content as part of the translatable message:
`<span ui:ph>attribute</span>`. Note that you can put the `ui:ph` attribute in any DOM
element, it's not particular to `<span>`.

## Messages with Values Computed at Runtime<a id="Messages_with_runtime_computed_values"></a>

When you want to change portions of a template at runtime, you typically put a ui:field in a 
span or other element and play with its HTML. When you do that inside a `<ui:msg>`, it is
automatically protected from translation for you.

**Original**

```html
<!-- Java code will make calls like getClosingDate().setInnerText(closingDate()) -->
(closed <span ui:field="closingDate" /> through <span ui:field="reopeningDate"/>)
```

**Tagged**

```xml
<ui:msg description='closed for business message'>
  (closed <span ui:field='closingDate' /> through <span ui:field='reopeningDate'/>)
</ui:msg>
```

**Generated**

```properties
# Description: closed for business message
# 0=arg0 (Example: <span id=''>), 1=arg1 (Example: </span>), 2=arg2 (Example: <span id=''>), 3=arg3 (Example: </span>)
E30D43242E1AD2AC2EFA1AEEEFDFCC33=(closed {0}{1} through {2}{3})

```

There is good news and bad news here. The good news is that you
don't have to add any `ui:ph` attributes or elements to protect
the begin and end tags of the spans marked with `ui:field`
attributes. The bad news is that there is nothing stopping the
translator from sticking arbitrary things between those begin and end tags. (Notice
`{0}{1}` and `{2}{3}`.)

If that's a concern, you need to put the named spans inside
`<ui:ph>` elements to make them opaque, like so:

**Tagged**

```xml
<ui:msg>
  (closed <ui:ph name='closingDate' example="7/12/2008"><span ui:field="closingDate"/></ui:ph>
  through <ui:ph name='reopeningDate' example="7/12/2008"><span ui:field="reopeningDate"/></ui:ph>)
</ui:msg>
```

**Generated**

```properties
# 0=arg0 (Example: 7/12/2008), 1=arg1 (Example: 7/12/2008)
53B9CF65553DFAA091435791E5C731E7=(closed {0} through {1})

```

The `example` attribute is optional, and allows you to give
the translator a more useful explanation of what your placeholders are for.

## Message Containing Widgets (HTMLPanel Only)<a id="Messages_containing_widgets"></a>

When working with `<g:[HTMLPanel](/javadoc/latest/com/google/gwt/user/client/ui/HTMLPanel.html)>`
elements, you may find yourself placing widgets inside your messages. No problem!

**Original**

```xml
<g:HTMLPanel>
  Meeting starts at
    <my:TimePicker ui:field="startPicker"/>
  and ends at
    <my:TimePicker ui:field="endPicker"/>.
</g:HTMLPanel>
```

**Tagged**

```xml
<g:HTMLPanel>
  <ui:msg>Meeting starts at
    <my:TimePicker ui:field="startPicker"/>
  and ends at
    <my:TimePicker ui:field="endPicker"/>.
  </ui:msg>
</g:HTMLPanel>
```



```properties
# 0=arg0 (Example: <span>), 1=arg1 (Example: </span>), 2=arg2 (Example: <span>), 3=arg3 (Example: </span>)
23CBEA252C9901BF84D757FAD4968289=Meeting starts at {0}{1} and ends at {2}{3}.

```

Note that there is no `ui:ph` attribute on the widgets. There's no need for them, as there is no ambiguity about what must be done when a widget shows up in the middle of a message. Note also that you can only do this kind of thing (widgets in messages) inside of an HTMLPanel, the only widget in the GWT collection that intermixes markup and child widgets.

**More than you wanted to know:** You may
have noticed that the message in the generated properties file has
"too many" placeholders for each widget, which means the translator
might introduce unwanted text into the spans that will be replaced
with the widgets at runtime. If that happens, no harm will be done
beyond wasting the translator's time, as the text will be lost when
then widget is put in place. Your user won't see it.

Things get even more interesting when you put a widget with text
body inside a message in an HTMLPanel. (That is, a widget that
implements [HasText](/javadoc/latest/com/google/gwt/user/client/ui/HasText.html)
or [HasHTML](/javadoc/latest/com/google/gwt/user/client/ui/HasHTML.html).)

**Original**

```xml
<g:HTMLPanel>
  To do the thing, <g:Hyperlink targetHistoryToken="/doThe#thing">click here</g:Hyperlink>
  and massage vigorously.
</g:HTMLPanel>
```

**Tagged**

```xml
<g:HTMLPanel>
  <ui:msg>
    To do the thing, <g:Hyperlink targetHistoryToken="/doThe#thing">click here</g:Hyperlink>
    and massage vigorously.
  </ui:msg>
</g:HTMLPanel>
```

**Generated**

```properties
# 0=arg0 (Example: <span>), 1=arg1 (Example: </span>)
8EFBF967A3FEFE78C41C8A298562A094=To do the thing, {0}click here{1} and massage vigorously.

```

## HTML Attributes that Need Translation<a id="HTML_attributes_that_need_translation"></a>

Body text isn't the only thing that will need translation &mdash;
attributes may need the same treatment. The title attribute, for
tool-tip text, and the `alt` tag of an `<img>` are the most common
examples.

**Original**

```html
<th title="Gross recipts">Gross</th>
```

**Tagged**

```xml
<th title="Gross receipts">
  <ui:attribute ui:name='title' ui:description='Tooltip text for gross column'/>
  <ui:msg description='name of gross column'>Gross</ui:msg>
</th>
```

## Words with Multiple Meanings<a id="Words_with_Multiple_Meanings"></a>

Be careful of words that mean different things in different contexts.

**Original**

```xml
Favorite Color:
  <ui:RadioButton name="color">Red</ui:RadioButton>
  <ui:RadioButton name="color">Orange</ui:RadioButton>

Favorite Fruit:
  <ui:RadioButton name="fruit">Apple</ui:RadioButton>
  <ui:RadioButton name="fruit">Orange</ui:RadioButton>
```

**Tagged**

```xml
Favorite Color:
  <ui:RadioButton name="color"><ui:msg>Red</ui:msg></ui:RadioButton>
  <ui:RadioButton name="color"><ui:msg meaning="the color"/>Orange</ui:msg></ui:RadioButton>

Favorite Fruit:
  <ui:RadioButton name="fruit"><ui:msg>Apple</ui:msg></ui:RadioButton>
  <ui:RadioButton name="fruit"><ui:msg meaning="the fruit">Orange<ui:msg></ui:RadioButton>
```

**Generated**

```properties
# Meaning: the color
4404BE8C34552617D633271BBC1FAB07=Orange

# Meaning: the fruit
7A6DCA1ACC86B4A7D7574CD6BDD4E0C1=Orange

9F6290F4436E5A2351F12E03B6433C3C=Apple

EE38E4D5DD68C4E440825018D549CB47=Red

```

The punchline here is that a translator may well be working with no
more context than the attributes you set on an individual message. And
if you're set up to share a big pool of translations distinguished only
by their MD5 hash sums, you may simply be unable to provide translations
for the two flavors of Orange needed here.

You can get around this by using the optional `meaning` attribute.
Unlike `description`, a message's meaning actually affects its hash id.
