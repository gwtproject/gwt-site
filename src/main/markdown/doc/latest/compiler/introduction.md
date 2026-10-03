# Introduction to the GWT Compiler

This document is intended for developers who want to learn a little more about how the GWT compiler works, and is
not required reading for using GWT. The developers writing and updating this document are themselves still learning,
and welcome advice and feedback on both the compiler and this document.

The GWT compiler's purpose is to translate and optimize Java source code into JavaScript output. Any optimizing
compiler's job, more broadly, is to preserve the effect of the original code's semantics, while removing unnecessary
operations and improving the efficiency of the remaining operations. We refer to the translation pieces as 
"normalization" where the compiler rewrites some Java or JS construct to another form in the same language, 
"transformation" where the compiler rewrites one AST to another, and "optimization" where the compiler rewrites a given
AST to produce more efficient output. Finally, "linking" refers to the process of taking the finished JS fragments and
making them usable in a JavaScript runtime.

1. [Compiler overview](#compiler-overview)
2. [ASTs](#asts)
3. [Normalization](#normalization)
4. [Optimization passes](#optimization-passes)
5. [SourceMaps](#sourcemaps)
7. [Verifying changes and diagnosing bugs](#verifying-changes-and-diagnosing-bugs)
8. [SOYC and FlightRecorder](#soyc-and-flightrecorder)
6. [FAQ: "Will this be optimized?"](#faq)

## Compiler overview <a id="compiler-overview"></a>

## ASTs <a id="asts"></a>

There are three ASTs involved in the compiler: JDT's own Java AST, used to parse Java sources, resolve references,
and perform type inference, the GWT Java AST, and the GWT JavaScript AST. Each has its own purpose, benefits, and 
quirks.

### JDT AST

For the most part, we use JDT to parse Java sources, take advantage of its ability to handle type inference for us, and
report errors in the source quickly to the user. 

### GWT's Java AST

From the JDT AST, the `GwtAstBuilder` class produces the GWT Java AST. We qualify this as the "GWT" Java AST, as there
are some specific node types that aren't true Java, or have not-quite-Java semantics.

#### JDebuggerStatement

#### JUnsafeTypeCoercion

#### JsniMethodBody/JsniMethodRef

#### JPermutationDependentValue

#### JNameOf

#### JNumericEntry

#### JProgram

### GWT's JS AST

Like the GWT Java AST, this is not quite conventional JS. First, it is limited to roughly ES3 - it hasn't been updated
to support `async`/`await`, `const`/`let`, or the `for`-`of` syntax, among others. It also is missing ES3's `with`.

#### JsProgram

#### JsNameOf

#### JsNumericEntry

#### JsProgramFragment


## Normalization <a id="normalization"></a>

Devirtualizer - replaces virtual method calls with static calls on String/Number/Boolean, Array types, and JSO types.
CatchBlockNormalizer - replaces catch blocks with if statements to support Java's ability to handle exception types differently.
PostOptimizationCompoundAssignmentNormalizer
LongCastNormalizer
LongEmulationNormalizer
TypeCoercionNormalizer
SplitCaseStatementValues
ComputeCastabilityInformation/ComputeExhaustiveCastabilityInformation
ImplementCastsAndTypeChecks
ImplementJsVarargs
ArrayNormalizer
EqualityNormalizer
ResolveRuntimeTypeReferences

ReplaceGetClassOverrides

--

JsNormalizer

EvalFunctionsAtTopScope

JsStackEmulator

JsBreakUpLargeVarStatements

## Optimization passes <a id="optimization-passes"></a>

Pruner - Removed unused types, methods, fields, and parameters. Roughly analogous to "tree shaking" tools that exist for
JS, but runs again after other passes have made changes to find more code to remove.
Finalizer - Marks classes, fields, methods, and variables as `final` where possible, which can enable other optimizations.
MakeCallsStatic - Rewrites instance methods to be static where there are no possible overrides, with "this" as the first
argument. Depends on the method being final, and a given callsite being specific enough to be statically resolved. The
instance method is retained, but delegates to the new static implementation. 
TypeTightener
MethodCallTightener
MethodCallSpecializer
DeadCodeElimination
MethodInliner
SameParameterValueOptimizer (optional)
EnumOrdinalizer (optional)

RemoveEmptySuperCalls (post loop)

RemoveSpecializations (post loop)

--

JsStaticEval
JsInliner
JsUnusedFunctionRemover

DuplicateClinitRemover (only run once)
JsDuplicateCaseFolder (only run once)

## SourceMaps <a id="sourcemaps"></a>

...

## Verifying changes and diagnosing bugs <a id="verifying-changes-and-diagnosing-bugs"></a>

Implementing new passes or changes are best accompanied by unit tests that run just that pass on cases that are
expected to be impacted, or to be left alone. This isn't sufficient to catch unexpected interactions though - existing
codebases should have their ASTs printed out and compared before/after to ensure that changes are as expected.

The Java system property `gwt.jjs.dumpAst` can be set to a path to dump the GWT ASTs to at various points throughout
compilation. It defaults to writing the entire program at once, and has no delimiter between passes. The system property
`gwt.jjs.dumpAst.filter` allows the output to be filtered to certain source files or ranges of lines within those files.
Sources are comma delimited, and line ranges are appended to the end of the file as `:<startLine>-<endLine>`. For 
example, `com/example/MyClass.java:10-20` would only print lines 10-20 of `MyClass.java`, tracing them through 
compilation, even if they are inlined into other classes.

Unfiltered, this can be helpful to make a change to the compiler and compile a full application before and after,
and use `diff` to compare the two outputs. Consider setting specifying a large enough context for the diff to see 
more of the method that is impacted to figure out where the change is in the codebase, e.g.

```shell
diff -U5 before.ast after.ast
...
```

## SOYC and FlightRecorder <a id="soyc-and-flightrecorder"></a>

...

## FAQ <a id="faq"></a>

AKA "Will this be optimized?"

#### Does `final`/`sealed` make any difference?
Generally no, at least in terms of how the compiler looks at your code. GWT is a "whole world" compiler, seeing all
classes at once, so it already knows if a class has subclasses, if a field is assigned only once, and so on. 

The flags can still be helpful for human readers and for some language features like matching on subclasses when
compiling the Java sources.

The `Finalizer` optimization pass will add `final` to the Java AST where possible but omitted, or where newly possible 
by other optimizations.

<!-- https://matrix.to/#/!AvXTCxQwVkniJBaCEi:gitter.im/$zE-7ufF-iuJeq8a8yZ26eHZ7PiKrVbl7XAi1dA7p9xM?via=gitter.im&via=matrix.org -->
#### Will local variables be inlined away?
For constants, yes. Other cases, not at this time - reordering some kinds of statements can result in side effects.

#### Will getters/setters be inlined to simplify callers?
Usually yes, provided the body of the method is simple enough, and either not overridden or the compiler can be certain
that only one implementation can be called.

#### Will assertions be optimized out?
By default, in production mode, yes, but you can enable them with `-ea`, same as normal Java. They are always enabled
at this time in dev mode. Writing detailed error messages here will enhance your dev mode experience, but will
not add additional runtime cost or size to your production compiled app. The downside is that if you those assertions
are disabled in production mode, there will be no exception thrown, which could break later code.

When assertions are compiled by GWT, they presently are replaced as follows:
```java
assert <some-condition> : <some-expression>
```
```java
if (<some-condition>) {
    GWT.debugger();
    throw new AssertionError(<some-expression>);
}
```
When compiled out, both the condition and the expression will be removed, so no side effects are left.

#### What about debugger statements?
Debugger statements, either from JSNI, from `GWT.debugger()`, from jsinterop-base's `Js.debugger()`, or from JSNI are
never optimized out. All browsers skip them, unless dev tools are open while JS is executing. Most dev tools have an
option to ignore `debugger` statements.

#### Why does my generated code contain `null.nullMethod()` or `null.nullField`?
When the compiler determines that no constructor for a class can ever be called, it removes the class, but sometimes
references to that class are left behind. Those references must now always be `null`, so any method call or field
reference will always result in a null pointer exception. Usually this code is unreachable and pruned anyway, but it is
possible to encounter this in production code - but typically, that code would have thrown a NullPointerException anyway.

#### What's the difference between `Js.cast(...)` and `Js.uncheckedCast(...)`?
The jsinterop-base method `Js.cast(...)` should be used when the cast is legal, but confuses Java, so you can't use a
plain Java cast. For example, when you cast `java.lang.String` to `elemental2.core.JsString` - clearly legal, but will
confuse the compiler. The code generated from `Js.cast(...)` will have the same runtime type checks as a normal Java cast,
and will fail in the same circumstances as well. Disabling runtime cast checks will also disable `Js.cast(...)`.

On the other hand, `Js.uncheckedCast(...)` should be used when the code is trying to circumvent the type system, and
there must not be a runtime check. This could be to work around a limitation or incorrect type in elemental2, or a bug
in a browser, or to knowingly do something "wrong" that may still make sense in the browser (such as casting
an `Int8Array` to `byte[]`).
