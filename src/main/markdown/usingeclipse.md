# Using Eclipse
The GWT Eclipse Plugin provides the Eclipse IDE with a set of tools for developing GWT Applications.
It includes a GWT Editor, GSS syntax highlighting, debug launchers, WTP Server Runtime integration 
and other features that help GWT application development.

*   [Download](#eclipse)
*   [Install](#install)
*   [Create](#creating)
*   [Debug](#debug)
*   [Change](#change)
*   [Compile](#compile)


## Reference

* [GWT Eclipse Plugin Documentation](http://gwt-plugins.github.io/documentation/gwt-eclipse-plugin/Download.html)


## Download Eclipse<a id="eclipse"></a>
First download and install the Eclipse IDE from [Eclipse Downloads](http://www.eclipse.org/downloads/). 

* Choose `Eclipse IDE for Java Developers` if there is not a need for WTP or Maven integration. 
* Choose `Eclipse IDE for EE Developers` if there is a need for, Maven, WTP servers and other EE integration. (Recommended)


## Install the GWT Plugin<a id="install"></a>
Install the GWT Eclipse Plugin by going to the Eclipse Marketplace and choosing the features. 

First goto the Eclipse Marketplace. 
<img src="images/eclipse/EclipseMarketPlace.png" style="display:block; width:400px;" />

Next search for GWT and select the GWT Eclipse Plugin. 
<img src="images/eclipse/SearchForGWT.png" style="display:block; width:400px;" />

Next show your support and become a star gazer. 
<img src="images/eclipse/StarGazer.png" style="display:block;" />

Next select the GWT Eclipse Plugin features. 
<img src="images/eclipse/MarketPlaceFeatures.png" style="display:block; width:400px;" />

## Create<a id="create"></a>
Create a GWT Web application. 

Select a New GWT Application Project.
<img src="images/eclipse/SelectNewGwtWebApplication.png" style="display:block; width:400px;" />

Then fill out the New GWT Application dialog.
<img src="images/eclipse/NewGwtWebApplicationDialog.png" style="display:block; width:400px;" />


## Debug<a id="debug"></a>
Debug the web application by creating a launcher for it.
This can be done by going to the GWT launcher shortcuts. 

Start by reating a launcher for the applicaiton. Or reuse a previously created launcher.
<img src="images/eclipse/GwtDevelopmentModeWithJetty.png" style="display:block; width:400px;" />

The Console view will log the CodeServer output. 
<img src="images/eclipse/CodeServerConsoleView.png" style="display:block; width:400px;" />

Then the Dev Mode view will focus. 
Right click ont he bookmark and select `Open with SDBG Chrome Js Debugger` the first time you start.
Opening with the Javascript Debugger will allow you to see the browser logging in the IDE and use breakpoints.  
<img src="images/eclipse/DevModeView.png" style="display:block; width:400px;" />

Then the Chrome browser will open and send a request to the CodeServer to start to compile. 
<img src="images/eclipse/BrowserCompile.png" style="display:block; width:400px;" />


## Change<a id="change"></a>
Make a modification to the application then reload the changes in the browser by clicking on the Chrome refresh button.

Instead of right clicking on the URL, double click to reopen Chrome with Javascript Debugger. 
<img src="images/eclipse/DevModeViewDoubleClick.png" style="display:block; width:400px;" />

## Compile <a id="compile"></a>
Compile by right clicking on the project and going to the GWT menus. 

<img src="images/eclipse/Compile.png" style="display:block; width:400px;" />


