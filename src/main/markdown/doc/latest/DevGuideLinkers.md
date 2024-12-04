# Linkers<a id="DevGuideLinkers-intro"></a>

The Linker subsystem takes care of writing the GWT compiler's output. It's responsible for each output file's name, location, and content.

Inside the compiler, each output file is represented by an
[Artifact](https://www.gwtproject.org/javadoc/latest/com/google/gwt/core/ext/linker/Artifact.html).

You usually do not need to care about them, unless you have specific requirements.

Linkers create several kinds of files (see
[EmittedArtifact.Visibility](https://www.gwtproject.org/javadoc/latest/com/google/gwt/core/ext/linker/EmittedArtifact.Visibility.html)):

- **Public files** are those intended to be downloaded by web browsers.
This includes JavaScript and other resource files.
- **Source files** are only served to browsers during development.
This includes Java source code and sourcemaps that browser debuggers use.
- **Deployed files** may be needed by application servers but shouldn't be served to browsers.
- **Private files** include various optional reports about what happened during the compile.
Soyc reports are one example.
- There are also non-emitted artifacts which are used to pass data from one linker to another,
but aren't written as output.

Linkers are divided into three categories:

- **pre-linkers** run before the primary linker, in the lexical order of their name.
- **primary linker** is the main linker. Only one primary linker is run for a compilation.
- **post-linkers** run after the primary linker, in reverse lexical order of their name.

To use a `Linker`, simply add `<add-linker name="linker_name" />` to your `Module.gwt.xml` file:

```xml
<module>
  <inherits name="com.google.gwt.core.Core" />
  <add-linker name="sso" />
</module>
```

Or you can also inherit another module which adds the linker for you.

This tag is additive for pre- and post-linkers; only the last primary linker will be run.
The `xsiframe` linker is the default if nothing is specified.

## Available linkers<a id="DevGuideLinkers-available"></a>

Please refer to the documentation of each linker to check how to use it.

The following `primary linkers` are defined:

- **xsiframe** (class [CrossSiteIframeLinker](/javadoc/latest/com/google/gwt/core/linker/CrossSiteIframeLinker.html)):
This linker uses an iframe to hold the code and a script tag to download the code. This is the default linker if no linker is specified.
- **direct_install** (class [DirectInstallLinker](/javadoc/latest/com/google/gwt/core/linker/DirectInstallLinker.html)):
A linker that adds a script tag directly to the iframe.
- **sso** (class [SingleScriptLinker](/javadoc/latest/com/google/gwt/core/linker/SingleScriptLinker.html)):
A Linker for producing a single JavaScript file from a GWT module.

The following linkers are deprecated:

- **xs**: use `xsiframe` instead.
- **std**: use `xsiframe` instead.

## Custom linkers<a id="DevGuideLinkers-custom"></a>

In case you have specific needs not covered by the existing linkers, you can create and use your custom linkers.

First, implement your linker. It must be a class extending the [Linker](/javadoc/latest/com/google/gwt/core/ext/Linker.html)
abstract class (check its documentation), or
[AbstractLinker](https://www.gwtproject.org/javadoc/latest/com/google/gwt/core/ext/linker/AbstractLinker.html).

Using the `@LinkerOrder` annotation, you can specify whether it is a pre-, post- or primary linker.
You must also annotate it with the `@Shardable` annotation.

Second, register your linker by putting `<define-linker name="short_name" class="fully_qualified_class_name" />`
in your `Module.gwt.xml` file.
The `name` attribute must be a valid Java identifier and is used to identify the Linker in `<add-linker>` tags.

It is permissible to redefine an already-defined Linker by declaring a new `<define-linker>` tag with the same name.

You can then use your linker with `<add-linker>` like any other linker.

## Internal linkers<a id="DevGuideLinkers-internal"></a>

For reference, here are some other linkers that GWT uses. They are not part of GWT's public API
and you shouldn't refer to them directly.

The following `pre-linkers` are defined:

- **rpcPolicyManifest** (class [RpcPolicyManifestLinker](/javadoc/latest/com/google/gwt/user/linker/rpc/RpcPolicyManifestLinker.html)):
Emit a file containing a map of RPC proxy classes to the partial path of the RPC policy file.
Automatically turned on for applications that use GWT-RPC.

The following `post-linkers` are defined:

- **soycReport** (class [SoycReportLinker](/javadoc/latest/com/google/gwt/core/linker/SoycReportLinker.html)):
Converts SOYC report files into emitted private artifacts. This linker is enabled by default, but the SOYC reports
themselves are off by default and enabled by the `-soyc` compiler flag. If the compiler doesn't generate the reports
then this linker doesn't do anything.
- **symbolMaps** (class [SymbolMapsLinker](/javadoc/latest/com/google/gwt/core/linker/SymbolMapsLinker.html)):
This Linker exports the symbol maps associated with each compilation result as a private file. This linker is enabled by default.
- **rpcLog** (class [RpcLogLinker](/javadoc/latest/com/google/gwt/user/linker/rpc/RpcLogLinker.html)): This linker
emits [RpcLogArtifact](/javadoc/latest/com/google/gwt/user/linker/rpc/RpcLogArtifact.html)` as output files.
Automatically turned on for applications that use GWT-RPC.
- **precompress** (class [PrecompressLinker](/javadoc/latest/com/google/gwt/precompress/linker/PrecompressLinker.html)):
A linker that precompresses the public artifacts that it sees. Used through inheriting `com.google.gwt.precompress.Precompress`.

