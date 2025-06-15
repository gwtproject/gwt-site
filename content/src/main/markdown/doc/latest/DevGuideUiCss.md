# CSS Styling

Like most web applications, GWT applications use cascading style sheets (CSS) for visual styling.

*   [Styling Existing Widgets](#widgets)
*   [Complex Styles](#complex)
*   [Associating CSS Files](#cssfiles)
*   [GWT Visual Themes](#themes)
*   [Documentation](#documentation)

## Styling Existing Widgets<a id="widgets"></a>

GWT widgets rely on cascading style sheets (CSS) for visual styling.

In GWT, each class of widget has an associated style name that binds it to a CSS rule. Furthermore, you can assign an id to a particular component to create a CSS rule that
applies just to that one component. By default, the class name for each component is `gwt-<classname>`. For example, the [Button widget](/javadoc/latest/com/google/gwt/user/client/ui/Button.html) has a default style of
`gwt-Button`.

In order to give all buttons a larger font, you could put the following rule in your application's CSS file:

```css
.gwt-Button { font-size: 150%; }
```

All of the widgets created with the GWT toolkit will have a default class name, but a widget's style name can be set using [setStyleName()](/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName-java.lang.String-).
Static elements can have their class set in the HTML source code for your application.

Another way to use style sheets is to refer to a single widget. For that, you would need to know the value of the `id` attribute for the widget or DOM element.

By default, neither the browser nor GWT creates default `id` attributes for widgets. You must explicitly create an `id` for the elements you want to refer to in
this manner, and you must insure that each "id" value is unique. A common way to do this is to set them on static elements in your [HTML host page](DevGuideOrganizingProjects.html#DevGuideHostPage)

```html
<div id="my-button-id"/>
```

To set the id for a GWT widget, retrieve its DOM Element and then set the `id` attribute as follows:

```java
Button b = new Button();
  DOM.setElementAttribute(b.getElement(), "id", "my-button-id")
```

This would allow you to reference a specific widget in a style sheet as follows:

```css
#my-button-id { font-size: 100%; }
```

## Complex Styles<a id="complex"></a>

Some widgets have multiple styles associated with them. [MenuBar](/javadoc/latest/com/google/gwt/user/client/ui/MenuBar.html), for example, has the following styles:

```css
.gwt-MenuBar { 
       /* properties applying to the menu bar itself */ 
   }
   .gwt-MenuBar .gwt-MenuItem { 
       /* properties applying to the menu bar's menu items */ 
   }
   .gwt-MenuBar .gwt-MenuItem-selected { 
       /* properties applying to the menu bar's selected menu items */
   }
```

In the above style sheet code, there are two style rules that apply to menu items. The first applies to all menu items (both selected and unselected), while the second (with
the -selected suffix) applies only to selected menu items. A selected menu item's style name will be set to `"gwt-MenuItem gwt-MenuItem-selected"`, specifying that both
style rules will be applied. The most common way of doing this is to use [setStyleName](/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName-java.lang.String-) to set
the base style name, then [addStyleName()](/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#addStyleName-java.lang.String-) and [removeStyleName()](/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#removeStyleName-java.lang.String-)
to add and remove the second style name.

## Associating CSS Files<a id="cssfiles"></a>

There are multiple approaches for associating CSS files with your module:

*   Using a `<link>` tag in the [host HTML page](DevGuideOrganizingProjects.html#DevGuideHostPage).
*   Using the `<stylesheet>` element in the [module XML file](DevGuideOrganizingProjects.html#DevGuideModuleXml).
*   Using a [CssResource](DevGuideClientBundle.html#CssResource) contained within a [ClientBundle](DevGuideClientBundle.html).
*   Using an inline [`<ui:style>`](DevGuideUiBinder.html#Hello_Stylish_World) element in a
UiBinder template.

Modern GWT applications typically use a combination of `CssResource` and UiBinder. Older applications should use only one of the first two choices.

### Including Style sheets in the HTML Host Page (Deprecated)

Typically, style sheets are placed in a package that is part of your module's [public path](DevGuideOrganizingProjects.html#DevGuideModules). All you need to do to reference
them is simply include a `<link>` to the style sheet in your [host page](DevGuideOrganizingProjects.html#DevGuideHostPage), such as:

```html
<link rel="stylesheet" href="mystyles.css" type="text/css"/>
```

### Including Style sheets in the Module XML file (Deprecated)

Another way to include your style sheet within your module is to use the `<stylesheet>` element in your [module
XML file](DevGuideOrganizingProjects.html#DevGuideModuleXml). This uses [automatic resource inclusion](DevGuideOrganizingProjects.html#DevGuideAutomaticResourceInclusion) to bundle the `.css` file with your
module.

The difference between using a `<link>` tag in HTML and the `<stylesheet>` element in your module XML file is that with the mdoule XML file approach,
the style sheet will always follow your module, no matter which host HTML page you deploy it from.

Why does this matter? Because if you create and share a module, it does not include a host page and therefore, you cannot guarantee the style sheet's availability. Automatic
Resource Inclusion solves this problem. If you do not care about sharing or re-using your module then you can just use the standard HTML link rel stuff in the host page.

**Tip:** Use a unique name for the .css file with included resources to avoid collisions. If you automatically include "styles.css" and share your module and someone
puts it on a page that already has "styles.css" there will be problems.

## GWT Visual Themes<a id="themes"></a>

GWT comes with three default visual themes that you can choose from: standard, chrome, and dark. The standard theme uses subtle shades of blue to create an lively user
interface. The chrome theme uses gray scale backgrounds for a refined, professional look. The dark theme uses dark shades of gray and black with bright indigo highlights for a
bold, eye catching experience. When you inherit a visual theme, almost all widgets will have some default styles associated with them. The visual themes allow you to focus more
time on application development and less time on styling your application.

By default, new GWT applications use the standard theme, but you can select any one of the themes mentioned above. Open your module XML file (gwt.xml) and uncomment the line
that inherits the theme of your choice.

```xml
<!-- Inherit the default GWT style sheet. You can change       -->
<!-- the theme of your GWT application by uncommenting          -->
<!-- any one of the following lines.                           -->
<!-- <inherits name='com.google.gwt.user.theme.standard.Standard'/> -->
<!-- <inherits name="com.google.gwt.user.theme.chrome.Chrome"/> -->
<inherits name="com.google.gwt.user.theme.dark.Dark"/>
```

GWT visual themes also come in RTL (right-to-left) versions if you are designing a website for a language that is written right-to-left, such as Arabic. You can include the RTL
version by adding RTL to the end of the module name:

```xml
<inherits name="com.google.gwt.user.theme.dark.DarkRTL"/>
```

### Bandwidth Sensitive Applications

If you are program a bandwidth sensitive application, such as a phone application, you may not want to require that users download the entire style sheet associated with your
favorite theme (about 27k). Alternatively, you can create your own stripped down version of the style sheet that only defines the styles applicable to your application. To do
this, first include the public resources associated with one of the themes by adding the following line to your `gwt.xml` file:

```xml
<inherits name='com.google.gwt.user.theme.standard.StandardResources'/>
```

Each theme has a "Resources" version that only includes the public resources associated with the theme, but does not inject a style sheet into the page. You will need to create
a new style sheet and inject it into the page as described in the sections above.

Finally, copy the contents of the file `public/gwt/standard/standard.css` style sheet located in the package `com.google.gwt.user.theme.standard` into your new
style sheet. Strip out any styles you do not want to include, reducing the size of the file. When you run your application, GWT will inject your stripped down version of the style
sheet, but you can still reference the files associate with the standard visual theme.

## Documentation<a id="documentation"></a>

It is standard practice to document the relevant CSS style names for each widget class as part of its documentation comment. For a simple example, see [Button](/javadoc/latest/com/google/gwt/user/client/ui/Button.html). For a more complex example, see [MenuBar](/javadoc/latest/com/google/gwt/user/client/ui/MenuBar.html).
