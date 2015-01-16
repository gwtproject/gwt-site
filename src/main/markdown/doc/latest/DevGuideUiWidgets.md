UiWidgets
===

You construct user interfaces in GWT applications using [widgets](/javadoc/latest/com/google/gwt/user/client/ui/Widget.html) that
are contained within [panels](/javadoc/latest/com/google/gwt/user/client/ui/Panel.html). Widgets allow you to interact with the user. Panels
control the placement of user interface elements on the page. Widgets and panels work the same way
on all browsers; by using them, you eliminate the need to write specialized code for each browser.

## Widgets

Widgets define your applications input and output with the user. Examples of widgets include the following:

*   [Button](/javadoc/latest/com/google/gwt/user/client/ui/Button.html) A user clicks the mouse button to
activate the button.
*   > ![img](images/Button.png)

*   [TextBox](/javadoc/latest/com/google/gwt/user/client/ui/TextBox.html) The application can display text and
the user can type in the text box.
*   > ![img](images/TextBox.png)

*   [Tree](/javadoc/latest/com/google/gwt/user/client/ui/Tree.html) A collapsible hierarchy of widgets.
*   > ![img](images/Tree.png)

*   [RichTextArea](/javadoc/latest/com/google/gwt/user/client/ui/RichTextArea.html) A text editor that allows
users to apply rich formatting of the text.
*   > ![img](images/RichTextArea.png)

For the complete list of GWT UI elements, see [Widget Gallery](RefWidgetGallery.html).

You are not limited to the set of widgets provided by GWT. There are a number of ways to [create custom
widgets](DevGuideUiCustomWidgets.html):

*   You can bundle together existing widgets and create a [Composite](/javadoc/latest/com/google/gwt/user/client/ui/Composite.html) widget.
*   You can write GWT bindings to an existing JavaScript widget.
*   You can create your own widget from scratch using either Java or JavaScript.

You can also use one or more of the many third party widget libraries written for GWT.

## Styles

Visual styles are applied to widgets using [Cascading Style Sheets (CSS)](DevGuideUiCss.html). Besides the default browser supplied
definitions, each GWT widget and panel has pre-defined style sheet class definitions documented in the class reference documentation.

## See Also

*   [Creating Custom Widgets](DevGuideUiCustomWidgets.html) Discussion of how to create your own widgets in GWT.

*   [Layout Using Panels](DevGuideUiPanels.html) Examples of how to use panels.
