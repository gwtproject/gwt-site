Missing parts of JRE Emulation
===

This page lists methods, fields and inner classes that are
missing from [the GWT emulation of JRE](RefJreEmulation.html).
Here you can also find links to GitHub issues that either track
progress on implementing the missing emulation or explain why it's
not likely to be implemented.

Top-level classes that are not emulated are not listed here.

<ol class="toc" id="pageToc">
  <li><a href="#Package_java_beans">java.beans</a></li>
  <li><a href="#Package_java_io">java.io</a></li>
  <li><a href="#Package_java_lang">java.lang</a></li>
  <li><a href="#Package_java_lang_annotation">java.lang.annotation</a></li>
  <li><a href="#Package_java_lang_reflect">java.lang.reflect</a></li>
  <li><a href="#Package_java_math">java.math</a></li>
  <li><a href="#Package_java_nio_charset">java.nio.charset</a></li>
  <li><a href="#Package_java_security">java.security</a></li>
  <li><a href="#Package_java_sql">java.sql</a></li>
  <li><a href="#Package_java_text">java.text</a></li>
  <li><a href="#Package_java_util">java.util</a></li>
  <li><a href="#Package_java_util_concurrent">java.util.concurrent</a></li>
  <li><a href="#Package_java_util_concurrent_atomic">java.util.concurrent.atomic</a></li>
  <li><a href="#Package_java_util_function">java.util.function</a></li>
  <li><a href="#Package_java_util_logging">java.util.logging</a></li>
  <li><a href="#Package_java_util_stream">java.util.stream</a></li>
</ol>

<h2 id="Package_java_beans">Package java.beans</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/beans/Beans.html">Beans</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10211"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10211 (Access to system resources)</a>:</strong> isGuiAvailable(), setDesignTime(boolean), setGuiAvailable(boolean)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> getInstanceOf(Object, Class), isInstanceOf(Object, Class), instantiate(ClassLoader, String, BeanContext, AppletInitializer), instantiate(ClassLoader, String), instantiate(ClassLoader, String, BeanContext)</dd>
</dl>
<h2 id="Package_java_io">Package java.io</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/Externalizable.html">Externalizable</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> writeExternal(ObjectOutput), readExternal(ObjectInput)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/InputStream.html">InputStream</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> readAllBytes(), readNBytes(int), readNBytes(byte[], int, int), transferTo(OutputStream), nullInputStream(), skipNBytes(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/PrintStream.html">PrintStream</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> writeBytes(byte[])</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> format(String, Object[]), format(Locale, String, Object[]), printf(Locale, String, Object[]), printf(String, Object[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/Reader.html">Reader</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> nullReader(), transferTo(Writer)</dd>
</dl>
<h2 id="Package_java_lang">Package java.lang</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Boolean.html">Boolean</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10213"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10213 (Read access to properties)</a>:</strong> getBoolean(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Byte.html">Byte</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9643"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9643 (Unsigned emulation)</a>:</strong> toUnsignedLong(byte), toUnsignedInt(byte), compareUnsigned(byte, byte)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.html">Character</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/1989"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#1989 (Unicode)</a>:</strong> getName(int), isJavaIdentifierStart(char), isJavaIdentifierStart(int), isJavaIdentifierPart(char), isJavaIdentifierPart(int), toString(int), reverseBytes(char), isDigit(int), isLowerCase(int), isUpperCase(int), isSurrogate(char), highSurrogate(int), lowSurrogate(int), toLowerCase(int), toUpperCase(int), getType(char), getType(int), isLetter(int), isLetterOrDigit(int), isTitleCase(int), isDefined(int), isDefined(char), isIdeographic(int), isUnicodeIdentifierStart(int), isUnicodeIdentifierStart(char), isUnicodeIdentifierPart(int), isUnicodeIdentifierPart(char), isIdentifierIgnorable(int), isIdentifierIgnorable(char), toTitleCase(int), toTitleCase(char), digit(int, int), getNumericValue(int), getNumericValue(char), isSpaceChar(int), isSpaceChar(char), isISOControl(char), isISOControl(int), getDirectionality(int), getDirectionality(char), isMirrored(char), isMirrored(int), isAlphabetic(int), codePointOf(String), UNASSIGNED, UPPERCASE_LETTER, LOWERCASE_LETTER, TITLECASE_LETTER, MODIFIER_LETTER, OTHER_LETTER, NON_SPACING_MARK, ENCLOSING_MARK, COMBINING_SPACING_MARK, DECIMAL_DIGIT_NUMBER, LETTER_NUMBER, OTHER_NUMBER, SPACE_SEPARATOR, LINE_SEPARATOR, PARAGRAPH_SEPARATOR, CONTROL, FORMAT, PRIVATE_USE, SURROGATE, DASH_PUNCTUATION, START_PUNCTUATION, END_PUNCTUATION, CONNECTOR_PUNCTUATION, OTHER_PUNCTUATION, MATH_SYMBOL, CURRENCY_SYMBOL, MODIFIER_SYMBOL, OTHER_SYMBOL, INITIAL_QUOTE_PUNCTUATION, FINAL_QUOTE_PUNCTUATION, DIRECTIONALITY_UNDEFINED, DIRECTIONALITY_LEFT_TO_RIGHT, DIRECTIONALITY_RIGHT_TO_LEFT, DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC, DIRECTIONALITY_EUROPEAN_NUMBER, DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR, DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR, DIRECTIONALITY_ARABIC_NUMBER, DIRECTIONALITY_COMMON_NUMBER_SEPARATOR, DIRECTIONALITY_NONSPACING_MARK, DIRECTIONALITY_BOUNDARY_NEUTRAL, DIRECTIONALITY_PARAGRAPH_SEPARATOR, DIRECTIONALITY_SEGMENT_SEPARATOR, DIRECTIONALITY_WHITESPACE, DIRECTIONALITY_OTHER_NEUTRALS, DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING, DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE, DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING, DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE, DIRECTIONALITY_POP_DIRECTIONAL_FORMAT, DIRECTIONALITY_LEFT_TO_RIGHT_ISOLATE, DIRECTIONALITY_RIGHT_TO_LEFT_ISOLATE, DIRECTIONALITY_FIRST_STRONG_ISOLATE, DIRECTIONALITY_POP_DIRECTIONAL_ISOLATE</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.UnicodeBlock.html">Character.UnicodeBlock</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/1989"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#1989 (Unicode)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.UnicodeScript.html">Character.UnicodeScript</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/1989"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#1989 (Unicode)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.Subset.html">Character.Subset</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/1989"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#1989 (Unicode)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Class.html">Class</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> forName(String, boolean, ClassLoader), forName(String), forName(Module, String), getModule(), getProtectionDomain(), isAssignableFrom(Class), isInstance(Object), getModifiers(), isHidden(), cast(Object), isAnnotation(), isRecord(), getClassLoader(), newInstance(), getInterfaces(), getEnclosingClass(), getResourceAsStream(String), getResource(String), getPackageName(), getMethod(String, Class[]), getNestHost(), getPermittedSubclasses(), toGenericString(), isSynthetic(), getGenericSuperclass(), getPackage(), getGenericInterfaces(), getSigners(), getEnclosingMethod(), getEnclosingConstructor(), getDeclaringClass(), isAnonymousClass(), isLocalClass(), isMemberClass(), getClasses(), getFields(), getMethods(), getConstructors(), getField(String), getConstructor(Class[]), getDeclaredClasses(), getDeclaredFields(), getRecordComponents(), getDeclaredMethods(), getDeclaredConstructors(), getDeclaredField(String), getDeclaredMethod(String, Class[]), getDeclaredConstructor(Class[]), asSubclass(Class), getAnnotatedSuperclass(), getAnnotatedInterfaces(), isNestmateOf(Class), getNestMembers(), isSealed()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Double.html">Double</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> toHexString(double)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10216"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10216 (Raw bits)</a>:</strong> doubleToRawLongBits(double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Enum.html">Enum</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/5067"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#5067 (Cloning objects)</a>:</strong> clone()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Enum.EnumDesc.html">Enum.EnumDesc</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Float.html">Float</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> toHexString(float)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10216"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10216 (Raw bits)</a>:</strong> floatToRawIntBits(float)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Integer.html">Integer</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10213"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10213 (Read access to properties)</a>:</strong> getInteger(String, Integer), getInteger(String), getInteger(String, int)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> parseInt(CharSequence, int, int, int)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9643"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9643 (Unsigned emulation)</a>:</strong> toUnsignedLong(int), compareUnsigned(int, int), toUnsignedString(int, int), toUnsignedString(int), parseUnsignedInt(String, int), parseUnsignedInt(CharSequence, int, int, int), parseUnsignedInt(String), divideUnsigned(int, int), remainderUnsigned(int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Long.html">Long</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10213"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10213 (Read access to properties)</a>:</strong> getLong(String, long), getLong(String), getLong(String, Long)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9909"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9909</a>:</strong> compareUnsigned(long, long), toUnsignedString(long, int), toUnsignedString(long), parseLong(CharSequence, int, int, int), divideUnsigned(long, long), remainderUnsigned(long, long), parseUnsignedLong(String, int), parseUnsignedLong(CharSequence, int, int, int), parseUnsignedLong(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Math.html">Math</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9909"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9909</a>:</strong> multiplyHigh(long, long), fma(double, double, double), fma(float, float, float)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html">Object</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/5067"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#5067 (Cloning objects)</a>:</strong> clone()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Short.html">Short</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9643"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9643 (Unsigned emulation)</a>:</strong> toUnsignedLong(short), toUnsignedInt(short), compareUnsigned(short, short)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StackTraceElement.html">StackTraceElement</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> isNativeMethod(), getModuleName(), getModuleVersion(), getClassLoaderName()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StrictMath.html">StrictMath</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9909"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9909</a>:</strong> multiplyHigh(long, long), fma(float, float, float), fma(double, double, double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html">String</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> format(String, Object[]), format(Locale, String, Object[]), formatted(Object[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html">System</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10212"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10212 (Write access to properties)</a>:</strong> setProperty(String, String), getProperties(), setProperties(Properties), clearProperty(String)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> lineSeparator()</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10211"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10211 (Access to system resources)</a>:</strong> exit(int), runFinalization(), load(String), getSecurityManager(), loadLibrary(String), console(), inheritedChannel(), setSecurityManager(SecurityManager), getenv(String), getenv(), getLogger(String, ResourceBundle), setIn(InputStream), mapLibraryName(String), in</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getLogger(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.Logger.html">System.Logger</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.LoggerFinder.html">System.LoggerFinder</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ThreadLocal.html">ThreadLocal</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9627"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9627</a>:</strong> initialValue()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Throwable.html">Throwable</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> printStackTrace(PrintWriter)</dd>
</dl>
<h2 id="Package_java_lang_annotation">Package java.lang.annotation</h2>
<dl>
</dl>
<h2 id="Package_java_lang_reflect">Package java.lang.reflect</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/Array.html">Array</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> newInstance(Class, int), newInstance(Class, int[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/Type.html">Type</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> getTypeName()</dd>
</dl>
<h2 id="Package_java_math">Package java.math</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigDecimal.html">BigDecimal</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9964"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9964</a>:</strong> sqrt(MathContext)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigInteger.html">BigInteger</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9964"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#9964</a>:</strong> sqrt(), sqrtAndRemainder()</dd>
</dl>
<h2 id="Package_java_nio_charset">Package java.nio.charset</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/charset/Charset.html">Charset</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> displayName(Locale)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10218"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#10218 (Emulation of java.nio)</a>:</strong> newDecoder(), decode(ByteBuffer), newEncoder(), encode(String), encode(CharBuffer), canEncode(), contains(Charset), isRegistered(), aliases(), isSupported(String), displayName()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/charset/StandardCharsets.html">StandardCharsets</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10219"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10219</a>:</strong> US_ASCII, UTF_16BE, UTF_16LE, UTF_16</dd>
</dl>
<h2 id="Package_java_security">Package java.security</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/security/MessageDigest.html">MessageDigest</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10211"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10211 (Access to system resources)</a>:</strong> getInstance(String, String), getInstance(String, Provider), getProvider()</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10218"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#10218 (Emulation of java.nio)</a>:</strong> update(ByteBuffer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/security/MessageDigestSpi.html">MessageDigestSpi</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> clone()</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10218"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#10218 (Emulation of java.nio)</a>:</strong> engineUpdate(ByteBuffer)</dd>
</dl>
<h2 id="Package_java_sql">Package java.sql</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Date.html">Date</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/611"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#611 (Time)</a>:</strong> valueOf(LocalDate), toLocalDate()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Time.html">Time</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/611"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#611 (Time)</a>:</strong> valueOf(LocalTime), toLocalTime()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Timestamp.html">Timestamp</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/611"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#611 (Time)</a>:</strong> valueOf(LocalDateTime), toLocalDateTime()</dd>
</dl>
<h2 id="Package_java_text">Package java.text</h2>
<dl>
</dl>
<h2 id="Package_java_util">Package java.util</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/AbstractMap.html">AbstractMap</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/5067"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#5067 (Cloning objects)</a>:</strong> clone()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html">Arrays</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10246"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10246</a>:</strong> compareUnsigned(short[], short[]), compareUnsigned(byte[], int, int, byte[], int, int), compareUnsigned(byte[], byte[]), compareUnsigned(long[], long[]), compareUnsigned(int[], int, int, int[], int, int), compareUnsigned(long[], int, int, long[], int, int), compareUnsigned(short[], int, int, short[], int, int), compareUnsigned(int[], int[])</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10215"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10215</a>:</strong> equals(double[], int, int, double[], int, int), equals(boolean[], int, int, boolean[], int, int), equals(Object[], int, int, Object[], int, int, Comparator), equals(Object[], Object[], Comparator), equals(Object[], int, int, Object[], int, int), equals(float[], int, int, float[], int, int), equals(int[], int, int, int[], int, int), equals(long[], int, int, long[], int, int), equals(byte[], int, int, byte[], int, int), equals(char[], int, int, char[], int, int), equals(short[], int, int, short[], int, int), compare(boolean[], int, int, boolean[], int, int), compare(byte[], byte[]), compare(byte[], int, int, byte[], int, int), compare(boolean[], boolean[]), compare(Object[], int, int, Object[], int, int, Comparator), compare(double[], int, int, double[], int, int), compare(Comparable[], Comparable[]), compare(Comparable[], int, int, Comparable[], int, int), compare(int[], int, int, int[], int, int), compare(Object[], Object[], Comparator), compare(int[], int[]), compare(long[], long[]), compare(long[], int, int, long[], int, int), compare(float[], float[]), compare(float[], int, int, float[], int, int), compare(double[], double[]), compare(char[], int, int, char[], int, int), compare(char[], char[]), compare(short[], int, int, short[], int, int), compare(short[], short[]), mismatch(short[], int, int, short[], int, int), mismatch(int[], int[]), mismatch(boolean[], int, int, boolean[], int, int), mismatch(long[], int, int, long[], int, int), mismatch(long[], long[]), mismatch(int[], int, int, int[], int, int), mismatch(byte[], int, int, byte[], int, int), mismatch(boolean[], boolean[]), mismatch(byte[], byte[]), mismatch(char[], char[]), mismatch(char[], int, int, char[], int, int), mismatch(short[], short[]), mismatch(Object[], Object[]), mismatch(Object[], int, int, Object[], int, int), mismatch(Object[], Object[], Comparator), mismatch(Object[], int, int, Object[], int, int, Comparator), mismatch(float[], float[]), mismatch(float[], int, int, float[], int, int), mismatch(double[], double[]), mismatch(double[], int, int, double[], int, int)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> copyOf(Object[], int, Class), copyOfRange(Object[], int, int, Class)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/BitSet.html">BitSet</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10218"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#10218 (Emulation of java.nio)</a>:</strong> valueOf(ByteBuffer), valueOf(LongBuffer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html">Collections</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> emptyEnumeration(), indexOfSubList(List, List), lastIndexOfSubList(List, List), unmodifiableNavigableSet(NavigableSet), unmodifiableNavigableMap(NavigableMap), emptySortedSet(), emptyNavigableSet(), emptySortedMap(), emptyNavigableMap()</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/9630"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#9630 (Reflection)</a>:</strong> checkedCollection(Collection, Class), checkedQueue(Queue, Class), checkedSet(Set, Class), checkedSortedSet(SortedSet, Class), checkedNavigableSet(NavigableSet, Class), checkedList(List, Class), checkedMap(Map, Class, Class), checkedSortedMap(SortedMap, Class, Class), checkedNavigableMap(NavigableMap, Class, Class)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Date.html">Date</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/611"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#611 (Time)</a>:</strong> from(Instant), toInstant()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Locale.html">Locale</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> clone(), getDefault(Category), lookup(List, Collection), filter(List, Collection), filter(List, Collection, FilteringMode), getLanguage(), getDisplayName(), getDisplayName(Locale), getAvailableLocales(), getUnicodeLocaleType(String), getCountry(), stripExtensions(), hasExtensions(), getVariant(), getScript(), setDefault(Category, Locale), setDefault(Locale), getUnicodeLocaleAttributes(), getUnicodeLocaleKeys(), getDisplayLanguage(), getDisplayLanguage(Locale), getDisplayScript(Locale), getDisplayScript(), getDisplayCountry(), getDisplayCountry(Locale), getDisplayVariant(), getDisplayVariant(Locale), filterTags(List, Collection), filterTags(List, Collection, FilteringMode), lookupTag(List, Collection), getISOCountries(IsoCountryCode), getISOCountries(), getISOLanguages(), getExtension(char), getExtensionKeys(), toLanguageTag(), forLanguageTag(String), getISO3Language(), getISO3Country(), FRENCH, GERMAN, ITALIAN, JAPANESE, KOREAN, CHINESE, SIMPLIFIED_CHINESE, TRADITIONAL_CHINESE, FRANCE, GERMANY, ITALY, JAPAN, KOREA, UK, CANADA, CANADA_FRENCH, CHINA, PRC, TAIWAN, PRIVATE_USE_EXTENSION, UNICODE_LOCALE_EXTENSION</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Locale.Category.html">Locale.Category</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Locale.IsoCountryCode.html">Locale.IsoCountryCode</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Locale.FilteringMode.html">Locale.FilteringMode</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Locale.LanguageRange.html">Locale.LanguageRange</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Locale.Builder.html">Locale.Builder</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Objects.html">Objects</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10221"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10221 (Miscellaneous Java 17 omissions)</a>:</strong> checkIndex(long, long), checkFromIndexSize(long, long, long), checkFromToIndex(long, long, long)</dd>
</dl>
<h2 id="Package_java_util_concurrent">Package java.util.concurrent</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html">ConcurrentHashMap</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> forEach(long, BiConsumer), forEach(long, BiFunction, Consumer), keySet(Object), mappingCount(), newKeySet(int), search(long, BiFunction), reduce(long, BiFunction, BiFunction), reduceToDouble(long, ToDoubleBiFunction, double, DoubleBinaryOperator), reduceToLong(long, ToLongBiFunction, long, LongBinaryOperator), reduceToInt(long, ToIntBiFunction, int, IntBinaryOperator), forEachKey(long, Consumer), forEachKey(long, Function, Consumer), searchKeys(long, Function), reduceKeys(long, BiFunction), reduceKeys(long, Function, BiFunction), reduceKeysToDouble(long, ToDoubleFunction, double, DoubleBinaryOperator), reduceKeysToLong(long, ToLongFunction, long, LongBinaryOperator), reduceKeysToInt(long, ToIntFunction, int, IntBinaryOperator), forEachValue(long, Function, Consumer), forEachValue(long, Consumer), searchValues(long, Function), reduceValues(long, BiFunction), reduceValues(long, Function, BiFunction), reduceValuesToDouble(long, ToDoubleFunction, double, DoubleBinaryOperator), reduceValuesToLong(long, ToLongFunction, long, LongBinaryOperator), reduceValuesToInt(long, ToIntFunction, int, IntBinaryOperator), forEachEntry(long, Consumer), forEachEntry(long, Function, Consumer), searchEntries(long, Function), reduceEntries(long, BiFunction), reduceEntries(long, Function, BiFunction), reduceEntriesToDouble(long, ToDoubleFunction, double, DoubleBinaryOperator), reduceEntriesToLong(long, ToLongFunction, long, LongBinaryOperator), reduceEntriesToInt(long, ToIntFunction, int, IntBinaryOperator)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.KeySetView.html">ConcurrentHashMap.KeySetView</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> Inner class missing</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html">ExecutorService</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10210"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10210 (Threads)</a>:</strong> awaitTermination(long, TimeUnit)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Executors.html">Executors</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10210"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10210 (Threads)</a>:</strong> newFixedThreadPool(int), newFixedThreadPool(int, ThreadFactory), newSingleThreadExecutor(ThreadFactory), newSingleThreadExecutor(), newCachedThreadPool(ThreadFactory), newCachedThreadPool(), newSingleThreadScheduledExecutor(ThreadFactory), newSingleThreadScheduledExecutor(), newScheduledThreadPool(int, ThreadFactory), newScheduledThreadPool(int), defaultThreadFactory(), privilegedThreadFactory()</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> newWorkStealingPool(), newWorkStealingPool(int), unconfigurableExecutorService(ExecutorService), unconfigurableScheduledExecutorService(ScheduledExecutorService), privilegedCallable(Callable), privilegedCallableUsingCurrentClassLoader(Callable), callable(PrivilegedAction), callable(PrivilegedExceptionAction)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/TimeUnit.html">TimeUnit</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10210"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10210 (Threads)</a>:</strong> timedJoin(Thread, long)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/611"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#611 (Time)</a>:</strong> convert(Duration), of(ChronoUnit), sleep(long), timedWait(Object, long), toChronoUnit()</dd>
</dl>
<h2 id="Package_java_util_concurrent_atomic">Package java.util.concurrent.atomic</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicBoolean.html">AtomicBoolean</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> getOpaque(), setOpaque(boolean), getAcquire(), setRelease(boolean), compareAndExchange(boolean, boolean), compareAndExchangeAcquire(boolean, boolean), compareAndExchangeRelease(boolean, boolean), weakCompareAndSetPlain(boolean, boolean), weakCompareAndSetAcquire(boolean, boolean), weakCompareAndSetRelease(boolean, boolean), weakCompareAndSetVolatile(boolean, boolean), getPlain(), setPlain(boolean)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicInteger.html">AtomicInteger</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> getOpaque(), setOpaque(int), getAcquire(), setRelease(int), compareAndExchange(int, int), compareAndExchangeAcquire(int, int), compareAndExchangeRelease(int, int), weakCompareAndSetPlain(int, int), weakCompareAndSet(int, int), weakCompareAndSetAcquire(int, int), weakCompareAndSetRelease(int, int), weakCompareAndSetVolatile(int, int), getAndUpdate(IntUnaryOperator), updateAndGet(IntUnaryOperator), getAndAccumulate(int, IntBinaryOperator), accumulateAndGet(int, IntBinaryOperator), getPlain(), setPlain(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicLong.html">AtomicLong</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> getOpaque(), setOpaque(long), getAcquire(), setRelease(long), compareAndExchange(long, long), compareAndExchangeAcquire(long, long), compareAndExchangeRelease(long, long), weakCompareAndSetPlain(long, long), weakCompareAndSet(long, long), weakCompareAndSetAcquire(long, long), weakCompareAndSetRelease(long, long), weakCompareAndSetVolatile(long, long), getAndUpdate(LongUnaryOperator), updateAndGet(LongUnaryOperator), getAndAccumulate(long, LongBinaryOperator), accumulateAndGet(long, LongBinaryOperator), getPlain(), setPlain(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicReference.html">AtomicReference</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> getOpaque(), setOpaque(Object), getAcquire(), setRelease(Object), compareAndExchange(Object, Object), compareAndExchangeAcquire(Object, Object), compareAndExchangeRelease(Object, Object), weakCompareAndSetPlain(Object, Object), weakCompareAndSetAcquire(Object, Object), weakCompareAndSetRelease(Object, Object), weakCompareAndSetVolatile(Object, Object), getAndUpdate(UnaryOperator), updateAndGet(UnaryOperator), getAndAccumulate(Object, BinaryOperator), accumulateAndGet(Object, BinaryOperator), getPlain(), setPlain(Object)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicReferenceArray.html">AtomicReferenceArray</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> getOpaque(int), setOpaque(int, Object), getAcquire(int), setRelease(int, Object), compareAndExchange(int, Object, Object), compareAndExchangeAcquire(int, Object, Object), compareAndExchangeRelease(int, Object, Object), weakCompareAndSetPlain(int, Object, Object), weakCompareAndSetAcquire(int, Object, Object), weakCompareAndSetRelease(int, Object, Object), weakCompareAndSetVolatile(int, Object, Object), getAndUpdate(int, UnaryOperator), updateAndGet(int, UnaryOperator), getAndAccumulate(int, Object, BinaryOperator), accumulateAndGet(int, Object, BinaryOperator), getPlain(int), setPlain(int, Object)</dd>
</dl>
<h2 id="Package_java_util_function">Package java.util.function</h2>
<dl>
</dl>
<h2 id="Package_java_util_logging">Package java.util.logging</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/Formatter.html">Formatter</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getHead(Handler), getTail(Handler)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/Handler.html">Handler</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getEncoding(), getFilter(), setEncoding(String), setFilter(Filter), setErrorManager(ErrorManager), getErrorManager(), reportError(String, Exception, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/Level.html">Level</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getResourceBundleName(), getLocalizedName()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/LogManager.html">LogManager</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getProperty(String), checkAccess(), reset(), readConfiguration(InputStream), readConfiguration(), updateConfiguration(Function), updateConfiguration(InputStream, Function), getLoggingMXBean(), addConfigurationListener(Runnable), removeConfigurationListener(Runnable), LOGGING_MXBEAN_NAME</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/LogRecord.html">LogRecord</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10210"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10210 (Threads)</a>:</strong> getThreadID(), setThreadID(int), getLongThreadID(), setLongThreadID(long)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10211"><span class="issueStatus" aria-label="Won't be implemented">❌</span>#10211 (Access to system resources)</a>:</strong> getResourceBundleName(), setResourceBundleName(String), setResourceBundle(ResourceBundle), getResourceBundle()</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getSequenceNumber(), setSequenceNumber(long), getSourceClassName(), getSourceMethodName(), getParameters(), setParameters(Object[]), setSourceClassName(String), setSourceMethodName(String)</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/611"><span class="issueStatus" aria-label="Workaround available via external library">🧩</span>#611 (Time)</a>:</strong> getInstant(), setInstant(Instant)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/Logger.html">Logger</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/3946"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#3946 (Localization)</a>:</strong> log(Level, String, Object), log(Level, String, Object[]), logp(Level, String, String, String, Object), logp(Level, String, String, Throwable, Supplier), logp(Level, String, String, String, Throwable), logp(Level, String, String, String), logp(Level, String, String, String, Object[]), logp(Level, String, String, Supplier), logrb(Level, String, String, ResourceBundle, String, Object[]), logrb(Level, ResourceBundle, String, Object[]), logrb(Level, String, String, String, String, Throwable), logrb(Level, String, String, ResourceBundle, String, Throwable), logrb(Level, ResourceBundle, String, Throwable), logrb(Level, String, String, String, String), logrb(Level, String, String, String, String, Object), logrb(Level, String, String, String, String, Object[])</dd>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10220"><span class="issueStatus" aria-label="Evaluating feasibility">🤔</span>#10220 (Logging)</a>:</strong> getLogger(String, String), getFilter(), setFilter(Filter), getResourceBundleName(), getAnonymousLogger(String), getAnonymousLogger(), setResourceBundle(ResourceBundle), getResourceBundle(), entering(String, String, Object), entering(String, String), entering(String, String, Object[]), exiting(String, String, Object), exiting(String, String), throwing(String, String, Throwable), global</dd>
</dl>
<h2 id="Package_java_util_stream">Package java.util.stream</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html">Collectors</a></dt>
  <dd><strong><a href="https://github.com/gwtproject/gwt/issues/10217"><span class="issueStatus" aria-label="Planned to be implemented, patches or reviews welcome">⏳</span>#10217 (Concurrent APIs)</a>:</strong> groupingByConcurrent(Function, Collector), groupingByConcurrent(Function, Supplier, Collector), groupingByConcurrent(Function), toConcurrentMap(Function, Function), toConcurrentMap(Function, Function, BinaryOperator, Supplier), toConcurrentMap(Function, Function, BinaryOperator)</dd>
</dl>
