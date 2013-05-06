<i>Ray Cromwell, Senior Software Engineer</i>
<br>
<i>Updated June 2012</i>

<p> Elemental is a new library for fast, lightweight, and "to the metal" web programming in GWT.
It's intended for developers who are comfortable working with the browser API's that JavaScript
programmers use. We think it will be an excellent 'thin' library for both mobile and desktop web
applications.
</p>

<h2 id="Contents">What's In It?</h2>

<p> Elemental includes every HTML5 feature, including DOM access of course, but also
bleeding edge features like WebGL, WebAudio, WebSockets, WebRTC, Web Intents, Shadow DOM,
the File API, and more. We can do this because we generate Java code directly from the WebIDL
files used by JavaScript engines.
</p>

<p> Elemental also includes high-performance collections and a new JSON library. These libraries
work equally well in a browser or in a server (JVM) environment.
</p>

<h2 id="Performance">Performance</h2>

<p> Elemental is designed to allow the GWT compiler to generate high-performance JavaScript with no
overhead. It does this by using only
<a href="../doc/latest/DevGuideCodingBasicsOverlay.html">JavaScript overlay types.</a>. Also,
the collection classes map directly to underlying JavaScript collections with no overhead.
</p>

<h2 id="Example">Example Usage</h2>
<p> Elemental uses Java interfaces to hide most of the generated overlay types it uses. JS APIs instances
can be obtained by the <code>Window</code> or <code>Document</code> interfaces. Here is a simple example to play a sound using
the Web Audio API.

<pre class="prettyprint">
package com.myapp;
import elemental.client.*;
import elemental.dom.*;
import elemental.html.*;

public class ElementalExample implements EntryPoint {
  public void onModuleLoad() {
    Window window = Browser.getWindow();
    AudioContext audioContext = window.newAudioContext();
    Oscillator osc = audioContext.createOscillator();
    osc.setType(Oscillator.SQUARE);
    osc.connect((AudioParam) audioContext.getDestination(), 0);
    osc.getFrequency().setValue(440.0f);
    osc.noteOn(0);
  }  
}
</pre>

<p> In general, HTML elements can be constructed by invoking methods starting with the pattern
<code>Document.createXXXElement</code> where XXX could be elements like <code>Select</code>,
or <code>Video</code>. JS APIs which allow the <code>new</code> keyword to construct instances
are found on <code>Window.newXXX</code> where XXX represents the underlying Javascript 
constructor name.

<h2 id="Caveats">Caveats</h2>

<p>
Elemental is a work in progress. Due to the nature of it being auto-generated from IDL specifications,
and those specifications for HTML5 changing rapidly over time, there are no unit tests for
generated code, and usage of bleeding edge HTML5 are untested and may break.

<p>
Elemental also introduces a new set of native JS collections which are not interoperable with Java
Collections. 

<p>
Currently, Elemental is generated from the WebKit IDL files and contains many references to
vendor-prefixed browser extensions. Using these draft-spec extensions may result in code that doesn't 
work on Firefox, Opera, or IE. Later versions of Elemental will include shim-generation
to force generated code to use the non-vendor prefixed APIs across all browsers.
