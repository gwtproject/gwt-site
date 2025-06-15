Accessibility
===

1.  [Overview](#DevGuideAccessibility)
2.  [Making Widgets Accessible](#DevGuideAccessibilityHowTo)
3.  [Adding ARIA Roles](#ariaRoles)
4.  [Adding ARIA Properties and States](#ariaPropertiesAndStates)
5.  [Adding Keyboard Accessibility](#kbdA11y)
6.  [Indicating Selection Changes](#selectionChanges)
7.  [Associating Meaningful Labels](#labels)
8.  [Automatically Speaking Highlighted Content](#speaking)
9.  [General Advice For Widget Developers](#widgets)

## Overview<a id="DevGuideAccessibility"></a>

### Screen Readers

A screen reader is an assistive application that interprets what is displayed on the screen for a blind or visually impaired user. Screen readers can interact with the user in
a variety of ways, including speaking out loud or even producing a braille output.

#### Who uses screen readers?

Many people find it helpful to be able to interact with their computers in multiple ways. Though Google does not keep statistics on how many of our users are using screen
readers, as the population ages, more people will require assistive technology. It is important to make sure that our applications are accessible for everybody.

#### How do screen readers work?

Screen readers listen for a standard set of events that are raised by platform-specific APIs. For example, when an alert window pops up on your screen, Microsoft Windows would
expose this event using the Microsoft Active Accessibility API, while a Linux machine would use the Linux Access Toolkit. [ChromeVox](https://chrome.google.com/webstore/detail/kgejglhpjiefppelpmljglcjbhoiplfn) for Chrome and [FireVox](http://www.firevox.clcworld.net/) for Firefox are screen reader browser extensions that are built
on top of the latest web technologies.

### GWT and Screen Readers

Ajax applications are often written in ways that screen readers have difficulty interpreting correctly. A GWT developer writing a tree widget, for example, might use a list
element that has been altered to behave like a tree control. But a screen reader would present the control as a list - an incorrect description that renders the tree unusable.
Screen readers will also treat HTML span or div elements as regular static text elements, regardless of the presence of JavaScript event handlers for user interaction; you can
easily imagine how this causes problems.

### ARIA

[ARIA](http://www.w3.org/WAI/intro/aria) is a W3C specification for making rich Internet applications accessible via a standard set of DOM properties. It is still new but there exist helpful resources on the web. More information can be found at [Google accessibility site](http://www.google.com/accessibility/) and [Chrome accessibility extensions page](http://code.google.com/p/google-axs-chrome/).

Adding accessibility support to GWT widgets involves adding the relevant properties to DOM elements that can be used by browsers to raise events during user interaction. Screen readers can react to these events to represent the function of GWT widgets. The DOM properties specified by ARIA are classified into Roles with their Properties and States.

An ARIA `role` attribute is added to HTML elements to define widgets and page structure elements that can be interpreted by a reader application/device, and therefore describes the way the widget should behave. A role is static and does not change over the lifetime of a widget. Some examples of ARIA roles: `tree`, `menubar`, `menuitem`, and `tab`. The ARIA role of a widget is expressed as a DOM attribute named `role` with its value set to one of the ARIA role strings.

There are also ARIA properties and states. ARIA states supply information about the changes in the web page that can be used for alerts, notification, navigation assistance. The state is changed as a result of an user interaction and developers should consider changing the widget state when handling user actions. For example, a checkbox widget could be in the states "checked" or "unchecked". A state is dynamic and should be updated during user interaction. Some examples of ARIA states: `aria-disabled`, `aria-pressed` and `aria-expanded`. An ARIA property is a characteristic feature of a widget that can change over time but more rarely than the ARIA state, and is often not changed as a result of user action. Examples of properties are `aria-haspopup`, `aria-label` and `aria-level`, which are semantic information about the interactivity between the page layout and a particular widget. ARIA states and properties are DOM attributes themselves &mdash; for example, to express that a toggle button widget has been pressed, we need to add the attribute `aria-pressed = 'true'` to the widgets HTML element. As another example,  a textarea widget that has a popup attached when in focus will have the attribute `aria-haspopup = 'true'`.

The role of a widget determines the set of states and properties that it supports. A widget with the role of `list`, for example, would not expose the `aria-pressed` state but will have the `aria-expanded` state.

Also, accessible widgets require keyboard support. Screen readers will speak the element that has keyboard focus, so keyboard accessibility can be accomplished by moving focus to different elements in response to keyboard commands.

### GWT and ARIA

Once ARIA roles, properties and states are added to the DOM of a GWT widget, browsers will raise the appropriate
events to the screen reader. As ARIA is still a work in progress, browsers may not raise an event for every ARIA
property, and screen readers may not recognize all of the events being raised.

Recently, we upgraded the accessibility support in GWT and deprecated the [Accessibility](/javadoc/latest/com/google/gwt/user/client/ui/Accessibility.html) class which contained a subset of the ARIA roles and states. We added a GWT
ARIA library that contains all the roles, states and properties defined in the ARIA standard. The roles are organized in
a hierarchy of interfaces. For each role there is a set of states and properties that are supported. There is a getter
for each role in the [Roles factory](/javadoc/latest/com/google/gwt/aria/client/Roles.html). For each role there exist accessors
for getting, setting and removing states and properties, or an extra attribute like `tabindex`.

The ARIA standard specifies the HTML attribute type for the state and property
values. These attribute types are supported in the new library with compile-time checks. Enumerations have been added for
HTML attributes of type token like 'aria-dropeffect' property type, 'aria-checked' type etc. There is a common type added for
IDREF(S) in the class. All the states and properties are defined in the [State](/javadoc/latest/com/google/gwt/aria/client/State.html) and [Property](/javadoc/latest/com/google/gwt/aria/client/Property.html) classes. The
`tabIndex` attribute has also been added as an extra attribute in the ARIA standard and we have [added it to the library](/javadoc/latest/com/google/gwt/aria/client/ExtraAttribute.html). We encourage users to get access to states and
properties through the role by getting it from the [Roles factory](/javadoc/latest/com/google/gwt/aria/client/Roles.html) because
the factory checks that the state or property is supported for the role.

Many GWT widgets now have keyboard accessibility and ARIA properties. These include [CustomButton](/javadoc/latest/com/google/gwt/user/client/ui/CustomButton.html),
 [Tree](/javadoc/latest/com/google/gwt/user/client/ui/Tree.html), [TreeItem](/javadoc/latest/com/google/gwt/user/client/ui/TreeItem.html),
 [MenuBar](/javadoc/latest/com/google/gwt/user/client/ui/MenuBar.html), [MenuItem](/javadoc/latest/com/google/gwt/user/client/ui/MenuItem.html),
 [TabBar](/javadoc/latest/com/google/gwt/user/client/ui/TabBar.html), and [TabPanel](/javadoc/latest/com/google/gwt/user/client/ui/TabPanel.html).
 Also, all widgets that inherit from FocusWidget now have a tabindex by default, allowing for better keyboard navigation.


## Making Widgets Accessible<a id="DevGuideAccessibilityHowTo"></a>

The remainder of this page describes how to add accessibility support to your GWT application and widgets with the new ARIA library. A variety of different
techniques are discussed; there is not yet a standard and generally-applicable approach for making AJAX applications accessible, but we provide some suggestions.

## Adding ARIA Roles<a id="ariaRoles"></a>

The ARIA attribute `role` is an indication of the widget type; it describes the way the widget should behave. Roles are static and should not change during the lifetime of a widget. Widget authors should:

*   Pick the right role for the widget from the  [Roles factory](/javadoc/latest/com/google/gwt/aria/client/Roles.html) class.
*   Set this attribute at construction time.

The following is an example taken from the <a href="/javadoc/latest/com/google/gwt/user/client/ui/CustomButton.html">CustomButton</a> widget.
Adding the `button` role indicates to an assistive technology that the widget will behave like a
button.

```java
protected CustomButton() {
    ...
    // Add a11y role "button"
    Roles.getButtonRole().set(getElement());
    ...
  }
```

## Adding ARIA Properties and States<a id="ariaPropertiesAndStates"></a>

An ARIA state is an additional attribute that reflects the current state of a widget which is a result of a user action; for example, whether a checkbox is checked or unchecked. A state should be initialized at the time a widget is constructed and updated during user interaction.

An ARIA property is an additional attribute that provides additional information about the widget; for example, a label of a widget is a property and is set once when constructing the widget. An example of a property that changes its value is `aria-activedescendant` and is used to supply information about the currently selected child element for a composite widget like a tree or menu.

Note that:

*   A widget can have numerous state attributes, one or more property attributes, and only one role attribute.
*   State attributes are dynamic and change during the widget's lifetime. A role once set does not change.
*   Property attributes reflect additional widget features that make the applications more interactive and may or may not change over time.

#### Initializing States and Properties

Once a specific ARIA role has been associated with a widget, it is important to check which states and properties are associated with that role. For example, the `button` role has two state attributes:

<dl>
<dt>`aria-disabled`</dt>
<dd>Indicates that the widget is present, but is not allowed for user actions.</dd>

<dt>`aria-pressed`</dt>
<dd>Used for buttons that are toggleable to indicate their current pressed state.</dd>
</dl>

In the CustomButton widget, the `aria-pressed` and `aria-disabled` ARIA state is initialized as follows:

```java
protected CustomButton() {
    ...
    // Add a11y state "aria-pressed" and "aria-disabled"
    Roles.getButtonRole().setAriaPressedState(getElement(), PressedValue.of(false));
    Roles.getButtonRole().setAriaDisabledState(getElement(), false);
  }
```

#### Updating States During User Interaction

The [CustomButton](/javadoc/latest/com/google/gwt/user/client/ui/CustomButton.html) widget has support for multiple button faces, giving developers more stylistic control. Also, unlike the
[Button](/javadoc/latest/com/google/gwt/user/client/ui/Button.html) widget, a CustomButton can be toggleable, as is
the case with the CustomButton subclass [ToggleButton](/javadoc/latest/com/google/gwt/user/client/ui/ToggleButton.html). Event handlers attached
to the underlying DOM element update the button faces when the button is pressed. We need to toggle the ARIA state `aria-pressed` as shown below.

```java
void toggleDown() {
    // Update a11y state "aria-pressed"
    Roles.getButtonRole().setAriaPressedState(getElement(), PressedValue.of(true));
  }

  void toggleUp() {
    // Update a11y state "aria-pressed"
    Roles.getButtonRole().setAriaPressedState(getElement(), PressedValue.of(false));
  }

  void setInactive() {
    // Update a11y state "aria-disabled"
    Roles.getButtonRole().setAriaDisabledState(getElement(), false);
  }

  void setActive() {
    // Update a11y state "aria-disabled"
    Roles.getButtonRole().setAriaDisabledState(getElement(), true);
  }
```

It is important to make sure that all event handlers that change the state of the widget also change the ARIA state.

## Adding Keyboard Accessibility<a id="kbdA11y"></a>

Keyboard accessibility is a key requirement when access-enabling GWT widgets. When developing a new widget, ensure that it is keyboard accessible from the outset; adding
keyboard accessibility later can be difficult. Screen readers will speak the element that has keyboard focus, so keyboard accessibility can be accomplished by moving focus to
different elements in response to keyboard commands.

Proper keyboard accessibility affords the following end-user behavior:

*   Users can tab to move focus to the widget.
*   When the widget receives focus, the screen reader will interpret the ARIA roles and states that are set on the widget.
*   The screen reader will speak a description of the widget, and its textual content.

By default, the only elements in the HTML DOM that can receive keyboard focus are anchor and form fields. However, setting the DOM property `tabIndex` (note that this
corresponds to HTML attribute `tabindex`) to 0 will place such elements in the default tab sequence and thereby
enable them to receive keyboard focus. Setting `tabIndex = -1`, while removing the element from the tab sequence,
still allows the element to receive keyboard focus programmatically. Tabindex support can be added for any role by
setting the `tabindex` ARIA attribute. The code below shows how to add the heading element to the natural tab
order of the page.  You might want to do this if you want to allow further interaction with the heading (e.g. for some type of
clickable heading).

```java
// Set tab index for a heading element
  Roles.getHeadingRole().setTabindexExtraAttribute(heading.getElement(), 0);
```

In GWT, any widget that extends the [FocusWidget](/javadoc/latest/com/google/gwt/user/client/ui/FocusWidget.html) abstract class will be keyboard focusable by default. The FocusWidget abstract class includes a
[setFocus(boolean)](/javadoc/latest/com/google/gwt/user/client/ui/FocusWidget.html#setFocus-boolean-) method that
can be used to programmatically set the focus or remove focus on the widget. FocusWidget also includes a
[setTabIndex(int)](/javadoc/latest/com/google/gwt/user/client/ui/FocusWidget.html##setTabIndex\(int\)) method that allows the user to set the DOM property
`tabIndex` for the widget.

Keep in mind that extending FocusWidget does not guarantee focusability for your widget. The base element of the FocusWidget (passed to the superclass constructor) must be a naturally focusable HTML element.

For widgets that don't extend the FocusWidget abstract class, ensuring keyboard accessibility can be more difficult. Different browsers set focus in different ways, and focus on arbitrary elements is not supported everywhere. You can use [FocusPanel](/javadoc/latest/com/google/gwt/user/client/ui/FocusPanel.html) to enclose elements that need to receive keyboard focus; just be sure to test your widget on different browsers.

For an example of using the `tabIndex` property, see the MenuBar widget. The root menu is the only one that should be in the tab sequence; its sub-menus are not. To
achieve this, the tab index is set to 0 in the MenuBar's constructor, and as new MenuBars are added as sub-menus, their
tab index attribute is reset to -1.

## Indicating Selection Changes<a id="selectionChanges"></a>

Some widgets, such as GWT's [Tree](/javadoc/latest/com/google/gwt/user/client/ui/Tree.html) and [MenuBar](/javadoc/latest/com/google/gwt/user/client/ui/MenuBar.html) widgets,
consist of a container with a set of items. The container has a naturally focusable DOM element, but the items
themselves do not. The focusable element receives all keyboard input, and causes visual changes in the contained items
to indicate a change in item selection. For example, GWT's Tree widget contains [TreeItems](/javadoc/latest/com/google/gwt/user/client/ui/TreeItem.html); both of these elements are made up of `div` elements. However, the Tree also
has a naturally focusable hidden element which receives keyboard events. Whenever the user hits the arrow keys, this
element handles the event and causes the appropriate TreeItem to be highlighted.

While this technique works for sighted users, it plays havoc with screen readers. Since the TreeItems themselves never get natural focus when selected, there is no way for the screen reader to know that the item selection has changed. One possible way to remedy this would be to have each TreeItem be naturally focusable. Unfortunately, TreeItems can contain more than just text &mdash; they can contain other widgets, which themselves can be focusable. Here, delegating focus properly would be fairly complex &mdash; each TreeItem would have to handle all of the key events for its child widget, and decide whether or not to delegate key events to its child (for user interaction with the child widget), or to handle the key events itself (for Tree navigation). Keep in mind that hooking up keyboard event handlers for each item would become unwieldy, as Trees may become very large. One can avoid doing this by relying on the natural event bubbling of key events, and having an element at the root of the Tree widget be responsible for receiving events.

Another way to remedy the situation is to use the ARIA `aria-activedescendant` property. This property is set on an element that is naturally focusable, and its value is the HTML id of the currently-selected item. Whenever the item changes, the `aria-activedescendant` value should be updated to the id of the newly-selected item. The screen reader will notice the change in the value and read the element corresponding to the id. Below is an example of how this technique is used on the GWT Tree and TreeItem
widgets.

First, we set roles on the Tree's root element and its focusable element:

```java
// Called from Tree(...) constructor
  private void init(TreeImages images, boolean useLeafImages) {

    ...

    // Root element of Tree is a div
    setElement(DOM.createDiv());

    ...

    // Create naturally-focusable element
    focusable = FocusPanel.impl.createFocusable();

    ...

    // Hide element and append it to root div
    DOM.setIntStyleAttribute(focusable, "zIndex", -1);
    DOM.appendChild(getElement(), focusable);

    // Listen for key events on the root element
    sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.KEYEVENTS);

    ...

    // Add a11y role "tree" to the focusable element
    Roles.getTreeRole().set(focusable);
 }
```

Whenever an item selection changes, the value of the `aria-activedescendant` property is set on the focusable element, and the ARIA states and properties of the currently-selected item are set:

```java
// Called after a new item has been selected
  private void updateAriaAttributes() {

    // Get the element which contains the text (or widget) content within
    // the currently-selected TreeItem
    Element curSelectionContentElem = curSelection.getContentElem();

    ...

    // Set the 'aria-level' state. To do this, we need to compute the level of
    // the currently selected item.
    Roles.getTreeitemRole().setAriaLevelProperty(curSelectionContentElem, curSelectionLevel + 1);

    // Set other ARIA states
    ...

    / Update the 'aria-activedescendant' state for the focusable element to
    // match the id of the currently selected item

    Roles.getTreeRole().setAriaActivedescendantProperty(focusable,
        IdReference.of(DOM.getElementAttribute(curSelectionContentElem, "id")));
  }
```

Though it is not shown in this code snippet, when TreeItems are created, they are constructed out of several divs, only one of which contains the content that we wish to be
interpreted by the screen reader. This div is assigned a unique DOM id (which is generated using the [DOM.createUniqueId()](/javadoc/latest/com/google/gwt/user/client/DOM.html#createUniqueId--) method), and a role of `treeitem`. These attributes are not set on the root TreeItem div because it contains a child image, which we do not want to be read.

#### Caveats with this Approach

The obvious problem with this approach is that unique DOM ids need to be assigned to all of the possible items that could be selected. While this is easy enough to implement, it seems unwieldy to assign a DOM id to each item.

Also, there is a subtle problem with using the `aria-activedescendant` state. Originally, the intended use-case for this state was the implementation of a listbox with divs. Whenever the `aria-activedescendant` value of the parent div (which was the one with natural focus) would change, the screen reader would read out the text of the list item with the corresponding id, ignoring any roles or states set on the selected item. This is fine for widgets as simple as a listbox; the selected item has enough text for the user to understand what is selected. However, in the case of a Tree, the selected item's text may not be enough. For example, which level of the tree is the selected item on?

Some screen readers have started to do more than just read the text of items selected with `aria-activedescendant`, interpreting the item just as they would any other
element that received keyboard focus. However, not all screen readers do this yet.

## Associating Meaningful Labels<a id="labels"></a>

A web page will often include human-readable descriptive elements (such as [Label](/javadoc/latest/com/google/gwt/user/client/ui/Label.html) and
[HTML](/javadoc/latest/com/google/gwt/user/client/ui/HTML.html) widgets) that explain the purpose of a particular widget. However, the association
between a widget and its description may not be obvious to a browser or a screen reader. ARIA defines the `aria-labelledby` property which can be used to explicitly associate a widget with one or more descriptive elements.

In order to associate a label with a widget, ensure that descriptive elements all have a unique id. The assigned id can later be used with to set the `aria-labelledby` state of a widget to refer to the id values of any descriptive elements, thereby associating those descriptive elements with the widget.

## Automatically Speaking Highlighted Content<a id="speaking"></a>

AJAX components often highlight an item of interest without moving keyboard focus to that item. This creates a good end-user experience when using components such as
autocomplete widgets; the user can continue to type and obtain further refinements to the available set of choices. Because screen readers traditionally attempt to speak the item that has keyboard focus, they will not read highlighted items. ARIA live regions help make widgets such as autocomplete boxes usable for visually impaired users.

#### How It Works

The ARIA role `region` is used to declare areas that hold such live content, i.e., content that updates dynamically without having keyboard focus. The ARIA state
`aria-live` on such regions specifies the priority of such updates; think of this as a politeness setting. Here is a code example that should provide the general idea of
how to implement this technique for an auto-complete widget:

#### Initialize Live Region

The ARIA role `region` is added when instantiating the relevant DOM nodes in the AutoCompleteWidget constructor:

```java
public AutoCompleteWidget() {
    ...
    // Create a hidden div where we store the current item text for a
    // screen reader to speak
    ariaElement = DOM.createDiv();
    DOM.setStyleAttribute(ariaElement, "display", "none");
    Roles.getRegionRole(ariaElement);
    Roles.getRegionRole().setAriaLiveProperty(ariaElement, LiveValue.ASSERTIVE);
    DOM.appendChild(getElement(), ariaElement);
  }
```

Here, we have created a hidden div element that holds the content to be spoken. We've declared it to have `role = 'region'` and `live = 'assertive'`; the latter setting specifies that updates to this content have the highest priority. Next, we set up the needed associations so that the set of suggestions returned as the user types into the AutoCompleteWidget's text box are put in hidden div:

```java
// This method is called via a keyboard event handler
    private void showSuggestions(Collection suggestions) {
      if (suggestions.size() > 0) {

        // Popupulate the visible suggestion pop-up widget with the new selections
        // and show them
        ....
        // Generate the hidden div content based on the suggestions
        String hiddenDivText = "Suggestions ";

        for (Suggestion curSuggestion : suggestions) {
          hiddenDivText += " " + curSuggestion.getDisplayString();
        }

        DOM.setInnerText(ariaElement, hiddenDivText);
      }
    }
```

#### Problems with this Approach

With this technique, the developer has complete control over the text that is spoken by the screen reader. While this seems like a good thing for the developer, it's not great for users of screen readers. Taking this approach, developers of AutoComplete widgets may decide on different text that the screen reader should read. For example, another screen reader might prefix each suggestion with the "Suggestion x", where x is the index of the suggestion in the list. This leads to an inconsistent experience across applications. If both developers were able to make use of ARIA roles, properties and states, then a more consistent experience would result, in accordance with the ARIA specification.

A more direct problem with this approach is internationalization. Most developers would realize that the list of suggestions needs to be translated into different languages;
this list is directly displayed on the screen. However, the word 'Suggestions', which is the first word on the live region, could easily be missed, since it is never visually
displayed to the user. These sorts of descriptive terms must also be translated. If ARIA roles and states could be used, then translation of the spoken terms associated with the
roles and states would be the screen reader's job; developers would only need to be responsible for translating their content.

## General Advice For Widget Developers<a id="widgets"></a>

First and foremost, use native HTML controls whenever possible. Native HTML controls are well understood by screen readers. They do not require ARIA roles and states, which has
two main benefits:

*   ARIA is not yet supported by all major browsers. Screen reader and browser developers have already done the work to make HTML controls accessible.
*   Reimplementing native HTML controls using divs (for example) can cause poor performance. For example, suppose a developer were to re-implement a listbox using divs. One of the ARIA properties that applies to the `listitem` role is `aria-posinset`. This value indicates the position of the item within its parent container, which corresponds to the item's index in the listbox. The problem is that every time an item is added or removed from the listbox, one has to iterate through all of the items in the list, adjusting their `aria-posinset` values. Though there are some optimizations that can be done, this is still much slower than native HTML select elements.

If native HTML controls cannot be used and a custom widget has to be built, keep in mind that it is much easier to develop an accessible widget from the beginning than to go
back and add accessibility support to an existing widget. While adding ARIA roles, properties and states is relatively easy, ensuring that the appropriate elements receive keyboard focus
during user interaction can be more complicated.

Make sure to test that a new widget is accessible! There are three basic steps in the translation between the DOM and the screen reader:

*   **DOM**

      Since ARIA attributes are being added directly to the DOM, an easy way to check that the attributes are in the right location is to use your browser's dev tools's DOM inspector.

*   **Events**

      It is important to make sure that the browser raises the appropriate events in response to ARIA attributes, changes in focus, and changes in the widget itself. A Microsoft tool called the [Accessible Event Watcher](http://msdn.microsoft.com/en-us/library/dd317979\(VS.85\).aspx), or AccEvent, can allow you to check which events are being raised.

*   **Screen Reader**

      In the end, the best way to verify that your GWT widgets are accessible is by using a screen reader. Some screen readers may not be listening for all of the
      events raised by the browser, or they might expect the ARIA attributes to be added to the DOM in certain locations. The most widely used screen readers with some support for ARIA are
      [JAWS](http://www.freedomscientific.com/products/fs/jaws-product-page.asp), [Window-Eyes](http://www.gwmicro.com/Window-Eyes/). New browser screen readers are the
      [FireVox](http://www.firevox.clcworld.net/) text-to-speech add-on for Firefox and the [ChromeVox](https://chrome.google.com/webstore/detail/kgejglhpjiefppelpmljglcjbhoiplfn) extension for Chrome and Chrome OS.
