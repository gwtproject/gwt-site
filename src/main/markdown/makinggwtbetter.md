Making GWT better
===

If you are interested in understanding the internals of Google Web Toolkit (GWT), building from source, or contributing ideas or modifications to the project, then this document is for you.

1.  [Introduction](#introduction)
    1.  [Licensing](#licensing)
    2.  [Why is GWT Open Source?](#whyopensource)
    3.  [Design Axioms](#designaxioms)
2.  [The GWT Community](#community)
    1.  [Please Be Friendly](#befriendly)
    2.  [Where to Discuss GWT](#wheretodiscussgwt)
    3.  [How to Report a Bug](#issuetracking)
3.  [Working with the Code](#workingoncode)
    1.  [Checking out the Source](#checkingout)
    2.  [Compiling from Source](#compiling)
    3.  [Testing](#testing)
4.  [Contributing Code](#contributingcode)
    1.  [Code Style](#codestyle)
    2.  [Contributor License Agreement](#clas)
    3.  [Submitting Patches](#submittingpatches)
    4.  [GWT Committers](#committers)
5.  [Contributing to Webpage &amp; Documentation](#webpage)

## Introduction

To start with the basics, why does GWT exist in the first place? The short answer is that GWT exists to make the web better for users. We've infused a slightly longer answer into our mission statement:

> GWT's mission is to radically improve the web
> experience for users by enabling developers to use existing Java tools
> to build no-compromise AJAX for any modern browser.

We spent a lot of time on that sentence and really tried to pack it with meaning. So, if you'll indulge us, let's explain why our mission statement is formulated the way that it is:

<dl>
  <dt><q>to radically improve</q></dt>
  <dd>The unconventional premise of GWT (i.e. compiling to
  JavaScript) sets the tone for how willing we are to be open-minded
  about approaches that make a big impact. We are willing to invest
  extra consideration and work to find solutions of the
  "order-of-magnitude better" rather than the "good enough" variety.</dd>

  <dt><q>the web experience</q></dt>
  <dd>The experience we want to optimize is the <i>web</i>
  experience. The web has different DNA than desktop applications, and
  GWT was designed to be of the web tradition rather than providing a
  way of slavishly cloning desktop idioms in a browser. Sure, there is
  some overlap. But when there's tension, we prioritize the simple, core
  web concepts like hyperlinks, history, URLs, and bookmarks.</dd>

  <dt><q>for users</q></dt>
  <dd>As in <i>end users</i>. We strongly prioritize features that
  can make the biggest differences to end users. Obviously, we want to make
  developers' lives easier, too, but never at the expense of the user
  experience. A classic example: synchronous RPC is a Bad Idea, even
  though it's so much more convenient for developers.</dd>

  <dt><q>by enabling developers</q></dt>
  <dd>GWT is about <i>enabling</i> developers to do great things,
  not necessarily spoon-feeding them or putting them in a straightjacket.
  GWT strives to be easy to use without sacrificing efficiency. We
  generally prioritize ensuring that the basics of GWT work very well
  rather than adding dozens of special-purpose widgets. It's much better
  to provide a solid platform so that other great libraries can be built
  on top of GWT rather than trying to be all things to all people out of
  the box.</dd>

  <dt><q>existing Java tools</q></dt>
  <dd>We aren't trying to build technology for its own sake! We
  chose the Java language primarily because there already are so many
  great tools out there, not to mention scads of books, articles,
  libraries, and expertise. GWT should take advantage of those tools by
  integrating with them rather than trying to replace them.</dd>

  <dt><q>no-compromise AJAX</q></dt>
  <dd>We want great results, where "great" is defined by how much
  it benefits end users. Sometimes there are conflicts between what is
  easy for us developers and what benefits end users the most. When the
  two are in conflict, end user experience almost always wins. <br />
  <br />
  We definitely do not view development in GWT as a form of compromise.
  We firmly believe that GWT should generate better JavaScript code than
  you would write by hand, and will generally choose to avoid making
  concessions to convenience if they hurt the performance of the
  resulting AJAX code. <br />
  <br />
  GWT is not intended to be an island. Great integration with JavaScript
  is a must-have, as is aggressive use of each browser's best
  capabilities, even if it requires yucky special-case coding (in which
  case, we'll try to hide it behind a no-overhead library).</dd>

  <dt><q>any modern browser</q></dt>
  <dd>We want GWT to be as portable as it can be so long as it
  doesn't involve sacrificing user experience in any significant way.</dd>
</dl>

So that's what GWT is all about. It's also useful to say what kinds of things GWT _isn't_ about...

<dl>
  <dt>Language wars</dt>
  <dd>Why does GWT support the Java programming language instead of
  language X? In a word, tools. There are lots of good Java
  tools. That's the entire explanation. It isn't that we don't like
  language X or that we think the Java programming language is
  somehow superior. We just like the tools.</dd>

  <dt>Being a monolithic framework</dt>
  <dd>We don't like those all-or-nothing heavy "app frameworks" any
  more than you do. That's why we designed GWT to be made up of
  independently useful parts.</dd>

  <dt>Hiding the browser programming model</dt>
  <dd>GWT is not an attempt to abstract away the browser from you.
  Neither is GWT an attempt to prevent you from writing JavaScript when
  you want to. GWT is simply a way to give us developers some extra
  leverage in the form of productivity tools and the ability to create
  higher-level abstractions when it's useful. If you're a JavaScript guru, we hope you'll
  find GWT is a great way for you to package up your best JavaScript
  work and make it easy to reuse.</dd>

  <dt>Bait and switch</dt>
  <dd>Hopefully, this one is self-evident by now. We've been saying
  from the beginning that we had no intention to ever charge for GWT,
  but hopefully now you know we mean it.</dd>

  <dt>Cloning traditional desktop UIs onto the web</dt>
  <dd>We want to foster <i>good</i> UIs, which may or may not be
  the same things as existing desktop UIs. We're trying to be pretty
  circumspect about introducing features "just because" they are used
  often in desktop applications.</dd>
</dl>

### Licensing<a id="licensing"></a>

All GWT source and pre-built binaries are provided under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0).

### Why is GWT Open Source?<a id="whyopensource"></a>

A good first question is, "Why wasn't GWT open source to begin with?" Since GWT's release in May 2006 at JavaOne, we've tried to stay totally focused on lowering the barrier for building AJAX apps. We weren't quite ready to open source the whole thing immediately because we knew we had plans for major infrastructure work (like Mac OS X hosted mode), and we really wanted to encourage everyone to focus on the idea of the product itself and how to write apps instead of creating distractions having to do with GWT's open sourceness.

GWT took off much faster than we expected, and it quickly became clear that the most sensible way to advance GWT would be to open it sooner rather than later. While we've never actually felt particularly stingy about keeping the source closed, now all code for the GWT Java to JavaScript compiler, the hosted mode browser, and so on can progress before your eyes. We're very much looking forward to contributions of ideas, bug reports, and patches.

### Design Axioms<a id="designaxioms"></a>

We adopted these on February 17, 2006.

*   User experience is primary
*   Simplify AJAX development
    *   Java debugging is non-negotiable
    *   Key facilities out of the box: history, RPC, localization, and unit testing
*   The big pieces must be independently useful
    *   Simple browser abstraction (i.e. using DOM without using Widget classes)
    *   Widget classes available but not required (i.e. use DOM and/or JSNI)
    *   Application management available but not required (e.g. history support is optional)
    *   RPC available but not required
    *   Java-to-JavaScript compiler is useful without the rest of the user libraries (implies JSNI and debugging aren't tangled up with gwt-user)
*   Interoperate with existing web pages
    *   Other than RPC, components operate without a GWT-specific server
    *   Scriptable from JavaScript and vice-versa
    *   Styling with CSS
*   Facilitate reuse of AJAX code
    *   Sharing of entire components and applications via self-contained JAR files
    *   Ability to publish Google Maps-style JavaScript API from Java classes
*   Optimize performance aggressively
    *   Don't do at run-time what you can do at compile-time
    *   Discourage unoptimizable patterns
*   Be secure by default
    *   Don't expose services automatically

## The GWT Community<a id="community"></a>

The GWT community exists primarily through mailing lists, the issue tracker and, to a lesser extent, the source control repository. You are definitely encouraged to contribute to the discussion and you can also help us to keep the effectiveness of the groups high by following and promoting the guidelines listed here.

### Please Be Friendly<a id="befriendly"></a>

Showing courtesy and respect to others is a vital part of the Google culture, and we strongly encourage everyone participating in GWT development to join us in accepting nothing less. Of course, being courteous is not the same as failing to constructively disagree with each other, but it does mean that we should be respectful of each other when enumerating the 42 technical reasons that a particular proposal may not be the best choice. There's never a reason to be antagonistic or dismissive toward anyone who is sincerely trying to contribute to a discussion.

Sure, web development is serious business and all that, but it's also a lot of fun. Let's keep it that way. Let's strive to be one of the friendliest communities in all of open source.

### Where to Discuss GWT<a id="wheretodiscussgwt"></a>

As always, discuss _using_ GWT in the [official GWT (GWT) developer discussion group](http://groups.google.com/group/Google-Web-Toolkit). There is an additional group for people to discuss any of the stuff in this document (e.g. how to build, how to submit patches) called [GWT Contributors](http://groups.google.com/group/Google-Web-Toolkit-Contributors). You don't have to actually submit code in order to sign up. Your participation itself is a valuable contribution.

### How to Report a Bug<a id="issuetracking"></a>

See [Issue Tracking](lifeofanissue.html).

## Working with the Code<a id="workingoncode"></a>

If you want to get your hands dirty with the code inside GWT, this is the section for you.

### Checking Out the Source<a id="checkingout"></a>

Checking out the GWT source is most useful if you plan to compile GWT yourself. The pre-built GWT distribution already contains all the Java source, so you don't actually need to check it out from the repository just to debug through it. Just tweak your IDE to read source from the GWT jars.

GWT is hosted at [gwt.googlesource.com](https://gwt.googlesource.com/), so you check out the source for GWT using a [Git](http://git-scm.com/) client as you would for any other Git project:

`git clone https://gwt.googlesource.com/gwt trunk`

### Compiling from Source<a id="compiling"></a>

Other than a few native libs, everything is Java source that can
be built on any supported platform with the included GWT [Ant](http://ant.apache.org) build files. At the moment, you
can only build the native code binaries on Linux, and even then with a
bit of effort that is currently beyond the scope of this document. To
keep things simple, we've checked in pre-built native binaries. (Yes, we
know that's unusual. But the source and makefiles are all there... it's
just a pain to doc all that and make it build in a cross-platform way.)

1.  Install [Apache Ant 1.7.0](http://ant.apache.org/bindownload.cgi) or later. We'll assume that the `ant` command is in your path.
2.  Check out the GWT prerequisite tools and third-party libraries: `~/gwt$ git clone https://github.com/gwtproject/tools.git tools`
3.  Check out the GWT source: `~/gwt$ git clone https://gwt.googlesource.com/gwt trunk`
4.  Optionally, set an environment variable that contains the full path to the directory into which you just checked out the tools. (If the `tools` directory is a sibling of the GWT source directory, you don't have to specify `GWT_TOOLS`.) In this example, you executed the previous command from the directory `~/gwt`, so you'd type:`~/gwt$ export GWT_TOOLS=~/gwt/tools`
5.  Switch to the directory into which you checked out the GWT source. Let's assume you checked 
it out into `~/gwt/trunk`. Make sure you're in that directory... `~/gwt$ cd ~/gwt/trunk` then 
just invoke Ant `ant`
6.  The GWT build creates each binary distribution in the `build/dist` subdirectory of the source root directory. In this example, the distributions would be in `~/gwt/trunk/build`.

### Testing<a id="testing"></a>

Testing is very important to maintaining the quality of GWT. [Unit Tests](#unittesting) should be written for any new code, and changes should be verified to not break existing tests before they are submitted for review.  To perform the tests, simply run `ant test` and verify that there are no failures.

#### Ant JUnit Issues<a id="antjunit"></a>

There is a problem, detailed at [ant.apache.org](http://ant.apache.org/manual/Tasks/junit.html), where ant cannot find the JUnit classes.  The simple workaround is to either:

*   Delete ant-junit.jar from the ant lib directory (for example, `/usr/share/ant/lib/`)
*   Copy junit.jar into your ant lib directory

## Contributing Code<a id="contributingcode"></a>

Now that GWT is open source, we're excited that it is now easier
for our users to fix bugs and create new features, and we hope to get
great patches from the community. Before you fire up your favorite IDE
and begin hammering away at that new feature, though, please take the
time to read this section and understand the process. While it seems
rigorous, we want to keep a high standard of quality in the code base.

### Code Style<a id="codestyle"></a>

To keep the source consistent, readable, diffable and easy to
merge, we use a fairly rigid coding style, and all patches will be
expected to conform to the style outlined here. To keep things as simple
as possible, many of these style rules will be automatically verified by
the GWT build; style issues that can't be caught by the build system should be
addressed during code review.

To make it easier to format your GWT code correctly, use the
[GWT style formatter](https://github.com/gwtproject/gwt/blob/main/eclipse/settings/code-style/gwt-format.xml)
for Eclipse (Preferences | Java > Code Style > Formatter | Import...).

In general, the GWT style is based on [the
standard Java conventions](http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html), except for the differences spelled out below.

For the record, we know that coding style is often a
controversial topic. We acknowledge that plenty of great approaches
exist out there. We're simply trying to pick one that is at least
somewhat consistent with Oracle's Java coding conventions, codify it well,
and stick to it.

#### Comments and Javadoc

Every file should have an Apache license header at the top,
prefaced with a copyright notice. A package statement and import
statements should follow, each block separated by a blank line. Next is
the class or interface declaration. In the Javadoc comments, describe
what the class or interface does.

```java
/*
 * Copyright 2006 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.foo;

import com.google.bar.Blah;
import com.google.bar.Yada;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Does X and Y and provides an abstraction for Z.
 */
public class Foo {
  ...
}
```

#### Class Structure and Member Sort Order

Java types should have the following member order:

1.  Nested Types (mixing inner and static classes is okay)
2.  Static Fields
3.  Static Initializers
4.  Static Methods
5.  Instance Fields
6.  Instance Initializers
7.  Constructors
8.  Instance Methods

Members that fall into the same category (e.g. static methods)
should also be sorted in this order based on visibility:

1.  public
2.  protected
3.  default
4.  private

All methods should be sorted alphabetically. Sorting is optional
but recommended for fields. For example, the following class excerpt is
correctly sorted:

```java
public abstract class Foo {
  // Type declarations.
  public class FooBaz {
  }
 
  private class FooBar {
  }

  // Static field declarations.
  // Remember, fields do NOT need to be sorted.
  static String B;
  static String A;

  // Static initializer declarations.
  static {
  }

  // Static methods declarations.
  // Remember, methods do need to be sorted.
  static void aStatic() {
  }

  static void bStatic() {
  }

  // Instance field declaration.
  String bField;
  String aField;

  // Instance Initializer declarations.
  {
  }

  // Constructors declaration.
  public Foo() {
  }

  protected Foo(String s) {
  }

  Foo(int i) {
  }

  private Foo(boolean b) {
  }

  // Instance method declaration.
  public void b() {
  }

  public void c() {
  }

  protected void a() {
  }

  protected void d() {
  }

  protected void e() {
  }

  protected void f() {
  }

  String h() {
  }

  // The "abstract" keyword does not modify the position of the method.
  abstract String i();

  void j() {
  }

  private void g() {
  }
}
```

#### Indentation

We use 2-space indents for blocks. No tabs at all, anywhere.

We use 4-space indents after line wraps, including function calls
and assignments. For example, this is correct (4 spaces after the
newline):

```java
Instrument i =
    new Instrument();
```

and this is not correct (2 spaces after the newline):

```java
Instrument i =
  new Instrument();
```

#### Imports

The ordering of import statements is:

*   GWT imports
*   Imports from third parties (com, junit, net, org)
*   java and javax

To exactly match the IDE settings, the imports should be:

*   Alphabetical within each grouping. Capital letters are considered to come before lower case letter (e.g. Z before a).
*   There should be a blank line between each major grouping (google, com, junit, net, org, java, javax).

#### Line Length and Wrapping

Each line of text in your code should be at most 100 characters
long. Use linefeed characters to break lines (Unix-style). There are
some exceptions:

*   Java identifiers referenced from within [JSNI methods](doc/latest/DevGuideCodingBasicsJSNI.html#methods-fields) can get quite long and cannot be parsed if split across lines.
*   Exception: If a comment line contains an example command or a literal URL longer than 100 characters, that line may be longer than 100 characters for ease of cut and paste.
*   Exception: Import lines can go over the limit because humans rarely see them. This also simplifies tool writing.

#### Acronyms in names

Treat acronyms and abbreviations as words. The names are much more readable:

|Good            |Bad             |
|----------------|----------------|
|`XmlHttpRequest`|`XMLHTTPRequest`|
|`getCustomerId` |`getCustomerID` |

This style rule also applies when an acronym or abbreviation is the entire name:

|Good         |Bad          |
|-------------|-------------|
|`class Html` |`class HTML` |
|`String url;`|`String URL;`|
|`long id;`   |`long ID;`   |

Note that this is a reversal of an earlier, regretted decision, and
much code in GWT violates this rule. While it's not worth breaking
existing APIs for stylistic concerns, all new code should treat
acronyms as words.

For further justifications of this style rule, see Effective Java
Item 56 (Item 38 in 1st edition) and Java Puzzlers Number 68.

#### Parameterized type names

Parameterized type names should be one capital letter. However, if
readability demands longer names (particularly due to having multiple
parameters), the name should be capitalized and have suffix "T". In a
nutshell, prefer `<T>` or `<K, V>`, and devolve to `<KeyT, ValueT>` if
necessary.

|Good          |Bad                                        |Tolerable                         |
|--------------|-------------------------------------------|----------------------------------|
|`Foo<T>`      |`Foo<FooClientType>`                       |                                  |
|`Bar<K, V>`   |`Bar<KeyType, ValueType>`                  |                                  |
|`Baz<V, E, X>`|`Baz<EventType, ErrorType, ExpressionType>`|`Baz<EventT, ErrorT, ExpressionT>`|

### Unit Testing<a id="unittesting"></a>

Unit tests are very important, and we strongly encourage submissions that include them, adding new unit tests for new functionality or updating existing unit tests for bug fixes.

Tests for Java classes should be placed in a parallel source tree
under `test` and the test class name should be suffixed with
`Test`. For example:

```text
src/com/google/gwt/core/client/EntryPoint.java
test/com/google/gwt/core/client/EntryPointTest.java
```

The use of the parallel test tree has two major advantages:

*   You can do package scope testing (vs. a `tests` subpackage).
*   There is a clear separation between production and test code making packaging easier. This reduces the number of build breaks and the amount of time spent updating build files.

Note that there is a problem using JUnit tests with ant &mdash; a [workaround](#antjunit) is described above.

### Submitting Patches<a id="submittingpatches"></a>

Please do submit code. Contributions to all [GWT subprojects](https://github.com/orgs/gwtproject/repositories) are welcome.Here's what you need to do:

1.  Decide which code you want to submit. A submission should be a set of changes that addresses one issue in the repository's issue tracker. For the [GWT SDK](https://github.com/gwtproject/gwt/) you can check the list of [its open issues](https://github.com/gwtproject/gwt/issues). Please don't mix more than one logical change per submittal, because it makes the history hard to follow. If you want to make a change that doesn't have a corresponding issue in the issue tracker, please create one.
2.  Also, coordinate with team members that are listed on the issue in question. This ensures that work isn't being duplicated and communicating your plan early also generally leads to better patches.
3.  Make a fork of the appropriate repository.
4.  Implement your changes, make sure that your code adheres to the [GWT source code style](#codestyle).
5.  Ensure that there are unit tests for your code.
6.  Push your changes to the forked repository.
7.  Create a pull request, follow the suggestions in the pull request template.

### Code Review

Code review is an essential part of the development process.
Anyone is welcome to submit a code review for any [open pull request](https://github.com/search?q=org%3Agwtproject+is%3Apr+is%3Aopen&type=pullrequests). 
Together with the final review done by GWT maintainers,
code reviews by the community have a positive impact on the code quality.

### GWT Committers<a id="committers"></a>

The current members of the GWT engineering team are the only committers at present. In the great tradition of eating one's own dogfood, we will be requiring each new GWT engineering team member to earn the right to become a committer by following the procedures in this document, writing consistently great code, and demonstrating repeatedly that he or she truly gets the zen of GWT.

### Contributing to webpage and Documentation<a id="webpage"></a>

This webpage and the documentation hosted on it are completely written in markdown. If you find an error, want to make an improvement, write an article or do something to the webpage feel free to do so.

Here is how it works:

All the code is stored on GitHub and can be directly edited on the web by pressing the
"Edit me on GitHub" button. This is the easiest way to make a change, but if you are
planing to make bigger changes to the website and want to test these locally first,
you can clone the repository:

`git clone https://github.com/gwtproject/gwt-site`

The source code is located in *src/main/markdown*. Instructions to test the changes locally can be found in the [README](https://github.com/gwtproject/gwt-site).

If you think your change is ready to be published on gwtproject.org you can send us
your changes with a pull request to https://github.com/gwtproject/gwt-site.

If you wish to contribute to the GWT code used on the site (to improve navigation), then you should

`git clone https://gwt.googlesource.com/gwt-site-webapp`

### Global .gitignore<a id="global_gitignore"></a>

Since our .gitignore files don't contain IDE or OS specific .gitignore entries you should setup your global .gitignore.

[This](https://help.github.com/articles/ignoring-files) is how you setup a global .gitignore.

[Here](https://github.com/github/gitignore/tree/main/Global) you find a list of IDE specific global .gitignore files.
