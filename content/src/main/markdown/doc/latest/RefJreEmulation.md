JRE Emulation
===

GWT includes a library that emulates a subset of the Java runtime library. 
The list below shows the set of JRE packages, types and methods that GWT can translate automatically. 
Note that in some cases, only a subset of methods is supported for a given type.

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
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/beans/Beans.html">Beans</a></dt>
  <dd>Beans(), isDesignTime()</dd>
</dl>

<h2 id="Package_java_io">Package java.io</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/BufferedWriter.html">BufferedWriter</a></dt>
  <dd>BufferedWriter(Writer), BufferedWriter(Writer, int), close(), flush(), newLine(), write(char[], int, int), write(int), write(String, int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/ByteArrayInputStream.html">ByteArrayInputStream</a></dt>
  <dd>ByteArrayInputStream(byte[]), ByteArrayInputStream(byte[], int, int), available(), close(), mark(int), markSupported(), read(), read(byte[], int, int), reset(), skip(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/ByteArrayOutputStream.html">ByteArrayOutputStream</a></dt>
  <dd>ByteArrayOutputStream(), ByteArrayOutputStream(int), close(), reset(), size(), toByteArray(), toString(), toString(int), toString(String), write(byte[], int, int), write(int), writeTo(OutputStream)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Closeable.html">Closeable</a></dt>
  <dd>close()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Externalizable.html">Externalizable</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/FilterInputStream.html">FilterInputStream</a></dt>
  <dd>available(), close(), mark(int), markSupported(), read(), read(byte[], int, int), reset(), skip(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/FilterOutputStream.html">FilterOutputStream</a></dt>
  <dd>FilterOutputStream(OutputStream), close(), flush(), write(byte[], int, int), write(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Flushable.html">Flushable</a></dt>
  <dd>flush()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/IOException.html">IOException</a></dt>
  <dd>IOException(), IOException(String), IOException(String, Throwable), IOException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/InputStream.html">InputStream</a></dt>
  <dd>InputStream(), available(), close(), mark(int), markSupported(), read(), read(byte[]), read(byte[], int, int), reset(), skip(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/OutputStream.html">OutputStream</a></dt>
  <dd>OutputStream(), close(), flush(), write(byte[]), write(byte[], int, int), write(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/OutputStreamWriter.html">OutputStreamWriter</a></dt>
  <dd>OutputStreamWriter(OutputStream, String), OutputStreamWriter(OutputStream, Charset), close(), flush(), getEncoding(), write(char[], int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/PrintStream.html">PrintStream</a></dt>
  <dd>PrintStream(OutputStream), print(boolean), print(char), print(char[]), print(double), print(float), print(int), print(long), print(Object), print(String), println(), println(boolean), println(char), println(char[]), println(double), println(float), println(int), println(long), println(Object), println(String), flush(), close(), write(byte[], int, int), write(int), checkError()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Reader.html">Reader</a></dt>
  <dd>Reader(), close(), mark(int), markSupported(), read(), read(char[]), read(char[], int, int), ready(), reset(), skip(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Serializable.html">Serializable</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/StringReader.html">StringReader</a></dt>
  <dd>StringReader(String), close(), read(char[], int, int), markSupported(), mark(int), reset()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/UncheckedIOException.html">UncheckedIOException</a></dt>
  <dd>UncheckedIOException(String, IOException), UncheckedIOException(IOException), getCause()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/UnsupportedEncodingException.html">UnsupportedEncodingException</a></dt>
  <dd>UnsupportedEncodingException(), UnsupportedEncodingException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Writer.html">Writer</a></dt>
  <dd>close(), flush(), write(char[]), write(char[], int, int), write(int), write(String), write(String, int, int), append(char), append(CharSequence), append(CharSequence, int, int)</dd>
</dl>

<h2 id="Package_java_lang">Package java.lang</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Appendable.html">Appendable</a></dt>
  <dd>append(char), append(CharSequence), append(CharSequence, int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/ArithmeticException.html">ArithmeticException</a></dt>
  <dd>ArithmeticException(String), ArithmeticException()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/ArrayIndexOutOfBoundsException.html">ArrayIndexOutOfBoundsException</a></dt>
  <dd>ArrayIndexOutOfBoundsException(), ArrayIndexOutOfBoundsException(int), ArrayIndexOutOfBoundsException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/ArrayStoreException.html">ArrayStoreException</a></dt>
  <dd>ArrayStoreException(), ArrayStoreException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/AssertionError.html">AssertionError</a></dt>
  <dd>AssertionError(), AssertionError(boolean), AssertionError(char), AssertionError(double), AssertionError(float), AssertionError(int), AssertionError(long), AssertionError(Object), AssertionError(String, Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/AutoCloseable.html">AutoCloseable</a></dt>
  <dd>close()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Boolean.html">Boolean</a></dt>
  <dd style='margin-bottom: 0.5em;'>FALSE, TRUE, TYPE</dd>
  <dd>Boolean(boolean), Boolean(String), compare(boolean, boolean), hashCode(boolean), logicalAnd(boolean, boolean), logicalOr(boolean, boolean), logicalXor(boolean, boolean), parseBoolean(String), toString(boolean), valueOf(boolean), valueOf(String), booleanValue(), compareTo(Boolean), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Byte.html">Byte</a></dt>
  <dd style='margin-bottom: 0.5em;'>MIN_VALUE, MAX_VALUE, SIZE, BYTES, TYPE</dd>
  <dd>Byte(byte), Byte(String), compare(byte, byte), decode(String), hashCode(byte), parseByte(String), parseByte(String, int), toString(byte), valueOf(byte), valueOf(String), valueOf(String, int), byteValue(), compareTo(Byte), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/CharSequence.html">CharSequence</a></dt>
  <dd>charAt(int), length(), subSequence(int, int), toString(), chars()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Character.html">Character</a></dt>
  <dd style='margin-bottom: 0.5em;'>TYPE, MIN_RADIX, MAX_RADIX, MIN_VALUE, MAX_VALUE, MIN_SURROGATE, MAX_SURROGATE, MIN_LOW_SURROGATE, MAX_LOW_SURROGATE, MIN_HIGH_SURROGATE, MAX_HIGH_SURROGATE, MIN_SUPPLEMENTARY_CODE_POINT, MIN_CODE_POINT, MAX_CODE_POINT, SIZE, BYTES</dd>
  <dd>Character(char), charCount(int), codePointAt(char[], int), codePointAt(char[], int, int), codePointAt(CharSequence, int), codePointBefore(char[], int), codePointBefore(char[], int, int), codePointBefore(CharSequence, int), codePointCount(char[], int, int), codePointCount(CharSequence, int, int), compare(char, char), digit(char, int), forDigit(int, int), hashCode(char), isBmpCodePoint(int), isDigit(char), isHighSurrogate(char), isLetter(char), isLetterOrDigit(char), isLowerCase(char), isLowSurrogate(char), isSpace(char), isWhitespace(char), isWhitespace(int), isSupplementaryCodePoint(int), isSurrogatePair(char, char), isTitleCase(char), isUpperCase(char), isValidCodePoint(int), offsetByCodePoints(char[], int, int, int, int), offsetByCodePoints(CharSequence, int, int), toChars(int), toChars(int, char[], int), toCodePoint(char, char), toLowerCase(char), toString(char), toUpperCase(char), valueOf(char), charValue(), compareTo(Character), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Class.html">Class</a></dt>
  <dd>isClassMetadataEnabled(), desiredAssertionStatus(), getCanonicalName(), getComponentType(), getEnumConstants(), getName(), getSimpleName(), getSuperclass(), isArray(), isEnum(), isInterface(), isPrimitive(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/ClassCastException.html">ClassCastException</a></dt>
  <dd>ClassCastException(), ClassCastException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/CloneNotSupportedException.html">CloneNotSupportedException</a></dt>
  <dd>CloneNotSupportedException(), CloneNotSupportedException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Cloneable.html">Cloneable</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Comparable.html">Comparable</a></dt>
  <dd>compareTo(T)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Deprecated.html">Deprecated</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Double.html">Double</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, MIN_NORMAL, MAX_EXPONENT, MIN_EXPONENT, NaN, NEGATIVE_INFINITY, POSITIVE_INFINITY, SIZE, BYTES, TYPE</dd>
  <dd>Double(double), Double(String), compare(double, double), doubleToLongBits(double), hashCode(double), isFinite(double), isInfinite(double), isNaN(double), longBitsToDouble(long), max(double, double), min(double, double), parseDouble(String), sum(double, double), toString(double), valueOf(double), valueOf(String), byteValue(), compareTo(Double), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), isInfinite(), isNaN(), longValue(), shortValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Enum.html">Enum</a></dt>
  <dd>valueOf(Class, String), compareTo(E), equals(Object), getDeclaringClass(), hashCode(), name(), ordinal(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Error.html">Error</a></dt>
  <dd>Error(), Error(String, Throwable), Error(String), Error(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Exception.html">Exception</a></dt>
  <dd>Exception(), Exception(String), Exception(String, Throwable), Exception(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Float.html">Float</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, MAX_EXPONENT, MIN_EXPONENT, MIN_NORMAL, NaN, NEGATIVE_INFINITY, POSITIVE_INFINITY, SIZE, BYTES, TYPE</dd>
  <dd>Float(double), Float(float), Float(String), compare(float, float), floatToIntBits(float), hashCode(float), intBitsToFloat(int), isFinite(float), isInfinite(float), isNaN(float), max(float, float), min(float, float), parseFloat(String), sum(float, float), toString(float), valueOf(float), valueOf(String), byteValue(), compareTo(Float), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), isInfinite(), isNaN(), longValue(), shortValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/FunctionalInterface.html">FunctionalInterface</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/IllegalArgumentException.html">IllegalArgumentException</a></dt>
  <dd>IllegalArgumentException(), IllegalArgumentException(String), IllegalArgumentException(String, Throwable), IllegalArgumentException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/IllegalStateException.html">IllegalStateException</a></dt>
  <dd>IllegalStateException(), IllegalStateException(String), IllegalStateException(String, Throwable), IllegalStateException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/IndexOutOfBoundsException.html">IndexOutOfBoundsException</a></dt>
  <dd>IndexOutOfBoundsException(), IndexOutOfBoundsException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Integer.html">Integer</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, SIZE, BYTES, TYPE</dd>
  <dd>Integer(int), Integer(String), bitCount(int), compare(int, int), decode(String), hashCode(int), highestOneBit(int), lowestOneBit(int), max(int, int), min(int, int), numberOfLeadingZeros(int), numberOfTrailingZeros(int), parseInt(String), parseInt(String, int), reverse(int), reverseBytes(int), rotateLeft(int, int), rotateRight(int, int), signum(int), sum(int, int), toBinaryString(int), toHexString(int), toOctalString(int), toString(int), toString(int, int), valueOf(int), valueOf(String), valueOf(String, int), byteValue(), compareTo(Integer), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/InterruptedException.html">InterruptedException</a></dt>
  <dd>InterruptedException(), InterruptedException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Iterable.html">Iterable</a></dt>
  <dd>iterator(), forEach(Consumer), spliterator()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/JsException.html">JsException</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Long.html">Long</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, SIZE, BYTES, TYPE</dd>
  <dd>Long(long), Long(String), bitCount(long), compare(long, long), decode(String), hashCode(long), highestOneBit(long), lowestOneBit(long), max(long, long), min(long, long), numberOfLeadingZeros(long), numberOfTrailingZeros(long), parseLong(String), parseLong(String, int), reverse(long), reverseBytes(long), rotateLeft(long, int), rotateRight(long, int), signum(long), sum(long, long), toBinaryString(long), toHexString(long), toOctalString(long), toString(long), toString(long, int), valueOf(long), valueOf(String), valueOf(String, int), byteValue(), compareTo(Long), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Math.html">Math</a></dt>
  <dd style='margin-bottom: 0.5em;'>E, PI</dd>
  <dd>Math(), abs(double), abs(float), abs(int), abs(long), acos(double), asin(double), addExact(int, int), addExact(long, long), atan(double), atan2(double, double), cbrt(double), ceil(double), copySign(double, double), copySign(float, float), cos(double), cosh(double), decrementExact(int), decrementExact(long), exp(double), expm1(double), floor(double), floorDiv(int, int), floorDiv(long, long), floorMod(int, int), floorMod(long, long), hypot(double, double), incrementExact(int), incrementExact(long), log(double), log10(double), log1p(double), max(double, double), max(float, float), max(int, int), max(long, long), min(double, double), min(float, float), min(int, int), min(long, long), multiplyExact(int, int), multiplyExact(long, long), negateExact(int), negateExact(long), pow(double, double), random(), rint(double), round(double), round(float), subtractExact(int, int), subtractExact(long, long), scalb(double, int), scalb(float, int), signum(double), signum(float), sin(double), sinh(double), sqrt(double), tan(double), tanh(double), toDegrees(double), toIntExact(long), toRadians(double), nextAfter(double, double), nextAfter(float, double), nextUp(double), nextUp(float), nextDown(double), nextDown(float)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/NegativeArraySizeException.html">NegativeArraySizeException</a></dt>
  <dd>NegativeArraySizeException(), NegativeArraySizeException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/NullPointerException.html">NullPointerException</a></dt>
  <dd>NullPointerException(), NullPointerException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Number.html">Number</a></dt>
  <dd>Number(), byteValue(), doubleValue(), floatValue(), intValue(), longValue(), shortValue()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/NumberFormatException.html">NumberFormatException</a></dt>
  <dd>NumberFormatException(), NumberFormatException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Object.html">Object</a></dt>
  <dd>Object(), equals(Object), getClass(), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Override.html">Override</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Record.html">Record</a></dt>
  <dd>hashCode(), equals(Object), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Runnable.html">Runnable</a></dt>
  <dd>run()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/RuntimeException.html">RuntimeException</a></dt>
  <dd>RuntimeException(), RuntimeException(String), RuntimeException(String, Throwable), RuntimeException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/SafeVarargs.html">SafeVarargs</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/SecurityException.html">SecurityException</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Short.html">Short</a></dt>
  <dd style='margin-bottom: 0.5em;'>MIN_VALUE, MAX_VALUE, SIZE, BYTES, TYPE</dd>
  <dd>Short(short), Short(String), compare(short, short), decode(String), hashCode(short), parseShort(String), parseShort(String, int), reverseBytes(short), toString(short), valueOf(short), valueOf(String), valueOf(String, int), byteValue(), compareTo(Short), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StackTraceElement.html">StackTraceElement</a></dt>
  <dd>StackTraceElement(), StackTraceElement(String, String, String, int), getClassName(), getFileName(), getLineNumber(), getMethodName(), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StrictMath.html">StrictMath</a></dt>
  <dd style='margin-bottom: 0.5em;'>E, PI</dd>
  <dd>StrictMath(), abs(double), abs(float), abs(int), acos(double), addExact(int, int), addExact(long, long), asin(double), atan(double), atan2(double, double), ceil(double), copySign(double, double), copySign(float, float), cos(double), cosh(double), exp(double), expm1(double), floor(double), floorDiv(int, int), floorDiv(long, long), floorMod(int, int), floorMod(long, long), hypot(double, double), log(double), log10(double), log1p(double), max(double, double), max(float, float), max(int, int), max(long, long), min(double, double), min(float, float), min(int, int), min(long, long), multiplyExact(int, int), multiplyExact(long, long), pow(double, double), random(), rint(double), round(double), round(float), scalb(double, int), scalb(float, int), signum(double), signum(float), sin(double), sinh(double), sqrt(double), subtractExact(int, int), subtractExact(long, long), tan(double), tanh(double), toDegrees(double), toIntExact(long), toRadians(double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html">String</a></dt>
  <dd style='margin-bottom: 0.5em;'>CASE_INSENSITIVE_ORDER</dd>
  <dd>String(), String(byte[]), String(byte[], int, int), String(byte[], int, int, String), String(byte[], int, int, Charset), String(byte[], String), String(byte[], Charset), String(char[]), String(char[], int, int), String(int[], int, int), String(String), String(StringBuffer), String(StringBuilder), copyValueOf(char[]), copyValueOf(char[], int, int), join(CharSequence, CharSequence[]), join(CharSequence, Iterable), valueOf(boolean), valueOf(char), valueOf(char[], int, int), valueOf(char[]), valueOf(double), valueOf(float), valueOf(int), valueOf(long), valueOf(Object), charAt(int), codePointAt(int), codePointBefore(int), codePointCount(int, int), compareTo(String), compareToIgnoreCase(String), concat(String), contains(CharSequence), contentEquals(CharSequence), contentEquals(StringBuffer), endsWith(String), equals(Object), equalsIgnoreCase(String), getBytes(), getBytes(String), getBytes(Charset), getChars(int, int, char[], int), hashCode(), indexOf(int), indexOf(int, int), indexOf(String), indexOf(String, int), intern(), isEmpty(), lastIndexOf(int), lastIndexOf(int, int), lastIndexOf(String), lastIndexOf(String, int), length(), matches(String), offsetByCodePoints(int, int), regionMatches(boolean, int, String, int, int), regionMatches(int, String, int, int), replace(char, char), replace(CharSequence, CharSequence), replaceAll(String, String), replaceFirst(String, String), split(String), split(String, int), startsWith(String), startsWith(String, int), subSequence(int, int), substring(int), substring(int, int), toCharArray(), toLowerCase(), toLowerCase(Locale), toUpperCase(), toUpperCase(Locale), toString(), trim(), strip(), stripLeading(), stripTrailing(), isBlank(), lines(), repeat(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuffer.html">StringBuffer</a></dt>
  <dd>StringBuffer(), StringBuffer(CharSequence), StringBuffer(int), StringBuffer(String), append(boolean), append(char), append(char[]), append(char[], int, int), append(CharSequence), append(CharSequence, int, int), append(double), append(float), append(int), append(long), append(Object), append(String), append(StringBuffer), appendCodePoint(int), delete(int, int), deleteCharAt(int), insert(int, boolean), insert(int, char), insert(int, char[]), insert(int, char[], int, int), insert(int, CharSequence), insert(int, CharSequence, int, int), insert(int, double), insert(int, float), insert(int, int), insert(int, long), insert(int, Object), insert(int, String), replace(int, int, String), reverse()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuilder.html">StringBuilder</a></dt>
  <dd>StringBuilder(), StringBuilder(CharSequence), StringBuilder(int), StringBuilder(String), append(boolean), append(char), append(char[]), append(char[], int, int), append(CharSequence), append(CharSequence, int, int), append(double), append(float), append(int), append(long), append(Object), append(String), append(StringBuffer), appendCodePoint(int), delete(int, int), deleteCharAt(int), insert(int, boolean), insert(int, char), insert(int, char[]), insert(int, char[], int, int), insert(int, CharSequence), insert(int, CharSequence, int, int), insert(int, double), insert(int, float), insert(int, int), insert(int, long), insert(int, Object), insert(int, String), replace(int, int, String), reverse()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringIndexOutOfBoundsException.html">StringIndexOutOfBoundsException</a></dt>
  <dd>StringIndexOutOfBoundsException(), StringIndexOutOfBoundsException(String), StringIndexOutOfBoundsException(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/SuppressWarnings.html">SuppressWarnings</a></dt>
  <dd>value()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html">System</a></dt>
  <dd style='margin-bottom: 0.5em;'>err, out</dd>
  <dd>System(), arraycopy(Object, int, Object, int, int), currentTimeMillis(), nanoTime(), gc(), getProperty(String), getProperty(String, String), identityHashCode(Object), setErr(PrintStream), setOut(PrintStream)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/ThreadLocal.html">ThreadLocal</a></dt>
  <dd>ThreadLocal(), get(), set(T), remove(), withInitial(Supplier)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Throwable.html">Throwable</a></dt>
  <dd>Throwable(), Throwable(String), Throwable(String, Throwable), Throwable(Throwable), getBackingJsObject(), addSuppressed(Throwable), fillInStackTrace(), getCause(), getLocalizedMessage(), getMessage(), getStackTrace(), getSuppressed(), initCause(Throwable), printStackTrace(), printStackTrace(PrintStream), setStackTrace(StackTraceElement[]), toString(), of(Object)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/UnsupportedOperationException.html">UnsupportedOperationException</a></dt>
  <dd>UnsupportedOperationException(), UnsupportedOperationException(String), UnsupportedOperationException(String, Throwable), UnsupportedOperationException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Void.html">Void</a></dt>
  <dd style='margin-bottom: 0.5em;'>TYPE</dd>
</dl>

<h2 id="Package_java_lang_annotation">Package java.lang.annotation</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Annotation.html">Annotation</a></dt>
  <dd>annotationType(), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/AnnotationFormatError.html">AnnotationFormatError</a></dt>
  <dd>AnnotationFormatError()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/AnnotationTypeMismatchException.html">AnnotationTypeMismatchException</a></dt>
  <dd>AnnotationTypeMismatchException()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Documented.html">Documented</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/ElementType.html">ElementType</a></dt>
  <dd style='margin-bottom: 0.5em;'>ANNOTATION_TYPE, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, MODULE, PACKAGE, PARAMETER, RECORD_COMPONENT, TYPE, TYPE_PARAMETER, TYPE_USE</dd>
  <dd>values(), valueOf(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/IncompleteAnnotationException.html">IncompleteAnnotationException</a></dt>
  <dd style='margin-bottom: 0.5em;'>annotationType, elementName</dd>
  <dd>IncompleteAnnotationException(Class, String), annotationType(), elementName()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Inherited.html">Inherited</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Native.html">Native</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Repeatable.html">Repeatable</a></dt>
  <dd>value()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Retention.html">Retention</a></dt>
  <dd>value()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/RetentionPolicy.html">RetentionPolicy</a></dt>
  <dd style='margin-bottom: 0.5em;'>CLASS, RUNTIME, SOURCE</dd>
  <dd>values(), valueOf(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/annotation/Target.html">Target</a></dt>
  <dd>value()</dd>
</dl>

<h2 id="Package_java_lang_reflect">Package java.lang.reflect</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/reflect/Array.html">Array</a></dt>
  <dd>get(Object, int), getBoolean(Object, int), getByte(Object, int), getChar(Object, int), getDouble(Object, int), getFloat(Object, int), getInt(Object, int), getLength(Object), getLong(Object, int), getShort(Object, int), set(Object, int, Object), setBoolean(Object, int, boolean), setByte(Object, int, byte), setChar(Object, int, char), setDouble(Object, int, double), setFloat(Object, int, float), setInt(Object, int, int), setLong(Object, int, long), setShort(Object, int, short)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/reflect/Type.html">Type</a></dt>
</dl>

<h2 id="Package_java_math">Package java.math</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/math/BigDecimal.html">BigDecimal</a></dt>
  <dd style='margin-bottom: 0.5em;'>ONE, ROUND_CEILING, ROUND_DOWN, ROUND_FLOOR, ROUND_HALF_DOWN, ROUND_HALF_EVEN, ROUND_HALF_UP, ROUND_UNNECESSARY, ROUND_UP, TEN, ZERO</dd>
  <dd>BigDecimal(BigInteger), BigDecimal(BigInteger, int), BigDecimal(BigInteger, int, MathContext), BigDecimal(BigInteger, MathContext), BigDecimal(char[]), BigDecimal(char[], int, int), BigDecimal(char[], int, int, MathContext), BigDecimal(char[], MathContext), BigDecimal(double), BigDecimal(double, MathContext), BigDecimal(int), BigDecimal(int, MathContext), BigDecimal(long), BigDecimal(long, MathContext), BigDecimal(String), BigDecimal(String, MathContext), valueOf(double), valueOf(long), valueOf(long, int), abs(), abs(MathContext), add(BigDecimal), add(BigDecimal, MathContext), byteValueExact(), compareTo(BigDecimal), divide(BigDecimal), divide(BigDecimal, int), divide(BigDecimal, int, int), divide(BigDecimal, int, RoundingMode), divide(BigDecimal, MathContext), divide(BigDecimal, RoundingMode), divideAndRemainder(BigDecimal), divideAndRemainder(BigDecimal, MathContext), divideToIntegralValue(BigDecimal), divideToIntegralValue(BigDecimal, MathContext), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), intValueExact(), longValue(), longValueExact(), max(BigDecimal), min(BigDecimal), movePointLeft(int), movePointRight(int), multiply(BigDecimal), multiply(BigDecimal, MathContext), negate(), negate(MathContext), plus(), plus(MathContext), pow(int), pow(int, MathContext), precision(), remainder(BigDecimal), remainder(BigDecimal, MathContext), round(MathContext), scale(), scaleByPowerOfTen(int), setScale(int), setScale(int, int), setScale(int, RoundingMode), shortValueExact(), signum(), stripTrailingZeros(), subtract(BigDecimal), subtract(BigDecimal, MathContext), toBigInteger(), toBigIntegerExact(), toEngineeringString(), toPlainString(), toString(), ulp(), unscaledValue()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/math/BigInteger.html">BigInteger</a></dt>
  <dd style='margin-bottom: 0.5em;'>ONE, TEN, ZERO</dd>
  <dd>BigInteger(byte[]), BigInteger(byte[], int, int), BigInteger(int, byte[]), BigInteger(int, byte[], int, int), BigInteger(int, int, Random), BigInteger(int, Random), BigInteger(String), BigInteger(String, int), probablePrime(int, Random), valueOf(long), abs(), add(BigInteger), and(BigInteger), andNot(BigInteger), bitCount(), bitLength(), byteValueExact(), clearBit(int), compareTo(BigInteger), divide(BigInteger), divideAndRemainder(BigInteger), doubleValue(), equals(Object), flipBit(int), floatValue(), gcd(BigInteger), getLowestSetBit(), hashCode(), intValue(), intValueExact(), isProbablePrime(int), longValue(), longValueExact(), max(BigInteger), min(BigInteger), mod(BigInteger), modInverse(BigInteger), modPow(BigInteger, BigInteger), multiply(BigInteger), negate(), nextProbablePrime(), not(), or(BigInteger), pow(int), remainder(BigInteger), setBit(int), shiftLeft(int), shiftRight(int), shortValueExact(), signum(), subtract(BigInteger), testBit(int), toByteArray(), toString(), toString(int), xor(BigInteger)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/math/MathContext.html">MathContext</a></dt>
  <dd style='margin-bottom: 0.5em;'>DECIMAL128, DECIMAL32, DECIMAL64, UNLIMITED</dd>
  <dd>MathContext(int), MathContext(int, RoundingMode), MathContext(String), equals(Object), getPrecision(), getRoundingMode(), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/math/RoundingMode.html">RoundingMode</a></dt>
  <dd style='margin-bottom: 0.5em;'>UP, DOWN, CEILING, FLOOR, HALF_UP, HALF_DOWN, HALF_EVEN, UNNECESSARY</dd>
  <dd>values(), valueOf(String), valueOf(int)</dd>
</dl>

<h2 id="Package_java_nio_charset">Package java.nio.charset</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/charset/Charset.html">Charset</a></dt>
  <dd>availableCharsets(), defaultCharset(), forName(String), name(), compareTo(Charset), hashCode(), equals(Object), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/charset/IllegalCharsetNameException.html">IllegalCharsetNameException</a></dt>
  <dd>IllegalCharsetNameException(String), getCharsetName()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/charset/StandardCharsets.html">StandardCharsets</a></dt>
  <dd style='margin-bottom: 0.5em;'>ISO_8859_1, UTF_8</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/charset/UnsupportedCharsetException.html">UnsupportedCharsetException</a></dt>
  <dd>UnsupportedCharsetException(String), getCharsetName()</dd>
</dl>

<h2 id="Package_java_security">Package java.security</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/DigestException.html">DigestException</a></dt>
  <dd>DigestException(), DigestException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/GeneralSecurityException.html">GeneralSecurityException</a></dt>
  <dd>GeneralSecurityException(), GeneralSecurityException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/MessageDigest.html">MessageDigest</a></dt>
  <dd>getInstance(String), isEqual(byte[], byte[]), digest(), digest(byte[]), digest(byte[], int, int), getAlgorithm(), getDigestLength(), reset(), update(byte), update(byte[]), update(byte[], int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/MessageDigestSpi.html">MessageDigestSpi</a></dt>
  <dd>MessageDigestSpi()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/NoSuchAlgorithmException.html">NoSuchAlgorithmException</a></dt>
  <dd>NoSuchAlgorithmException(), NoSuchAlgorithmException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/SHA256Digest.html">SHA256Digest</a></dt>
</dl>

<h2 id="Package_java_sql">Package java.sql</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Date.html">Date</a></dt>
  <dd>Date(int, int, int), Date(long), valueOf(String), getHours(), getMinutes(), getSeconds(), setHours(int), setMinutes(int), setSeconds(int), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Time.html">Time</a></dt>
  <dd>Time(int, int, int), Time(long), valueOf(String), getDate(), getDay(), getMonth(), getYear(), setDate(int), setMonth(int), setYear(int), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Timestamp.html">Timestamp</a></dt>
  <dd>Timestamp(int, int, int, int, int, int, int), Timestamp(long), valueOf(String), after(Timestamp), before(Timestamp), compareTo(Date), compareTo(Timestamp), equals(Object), equals(Timestamp), getNanos(), getTime(), hashCode(), setNanos(int), setTime(long), toString()</dd>
</dl>

<h2 id="Package_java_text">Package java.text</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/Normalizer.html">Normalizer</a></dt>
  <dd>Normalizer(), normalize(CharSequence, Normalizer.Form), isNormalized(CharSequence, Normalizer.Form)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/Normalizer.Form.html">Normalizer.Form</a></dt>
  <dd style='margin-bottom: 0.5em;'>NFD, NFC, NFKD, NFKC</dd>
  <dd>values(), valueOf(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/ParseException.html">ParseException</a></dt>
  <dd>ParseException(String, int), getErrorOffset()</dd>
</dl>

<h2 id="Package_java_util">Package java.util</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractCollection.html">AbstractCollection</a></dt>
  <dd>add(E), addAll(Collection), clear(), contains(Object), containsAll(Collection), isEmpty(), remove(Object), removeAll(Collection), retainAll(Collection), toArray(), toArray(T[]), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractList.html">AbstractList</a></dt>
  <dd>add(E), add(int, E), addAll(int, Collection), clear(), equals(Object), hashCode(), indexOf(Object), iterator(), lastIndexOf(Object), listIterator(), listIterator(int), remove(int), set(int, E), subList(int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractMap.html">AbstractMap</a></dt>
  <dd>clear(), containsKey(Object), containsValue(Object), equals(Object), get(Object), hashCode(), isEmpty(), keySet(), put(K, V), putAll(Map), remove(Object), size(), toString(), values()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractMap.SimpleEntry.html">AbstractMap.SimpleEntry</a></dt>
  <dd>SimpleEntry(K, V), SimpleEntry(Map.Entry)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractMap.SimpleImmutableEntry.html">AbstractMap.SimpleImmutableEntry</a></dt>
  <dd>SimpleImmutableEntry(K, V), SimpleImmutableEntry(Map.Entry), setValue(V)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractQueue.html">AbstractQueue</a></dt>
  <dd>add(E), addAll(Collection), clear(), element(), remove()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractSequentialList.html">AbstractSequentialList</a></dt>
  <dd>add(int, E), addAll(int, Collection), get(int), iterator(), remove(int), set(int, E)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/AbstractSet.html">AbstractSet</a></dt>
  <dd>equals(Object), hashCode(), removeAll(Collection)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ArrayDeque.html">ArrayDeque</a></dt>
  <dd>ArrayDeque(), ArrayDeque(int), ArrayDeque(Collection), add(E), addFirst(E), addLast(E), clear(), clone(), contains(Object), descendingIterator(), element(), getFirst(), getLast(), isEmpty(), iterator(), offer(E), offerFirst(E), offerLast(E), peek(), peekFirst(), peekLast(), poll(), pollFirst(), pollLast(), pop(), push(E), remove(), remove(Object), removeFirst(), removeFirstOccurrence(Object), removeLast(), removeLastOccurrence(Object), size(), spliterator(), toArray(T[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ArrayList.html">ArrayList</a></dt>
  <dd>ArrayList(), ArrayList(Collection), ArrayList(int), add(E), add(int, E), addAll(Collection), addAll(int, Collection), clear(), clone(), contains(Object), ensureCapacity(int), get(int), indexOf(Object), iterator(), forEach(Consumer), isEmpty(), lastIndexOf(Object), remove(int), remove(Object), removeIf(Predicate), replaceAll(UnaryOperator), set(int, E), size(), sort(Comparator), toArray(), toArray(T[]), trimToSize()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Arrays.html">Arrays</a></dt>
  <dd>asList(T[]), binarySearch(byte[], int, int, byte), binarySearch(byte[], byte), binarySearch(char[], int, int, char), binarySearch(char[], char), binarySearch(double[], int, int, double), binarySearch(double[], double), binarySearch(float[], int, int, float), binarySearch(float[], float), binarySearch(int[], int, int, int), binarySearch(int[], int), binarySearch(long[], int, int, long), binarySearch(long[], long), binarySearch(Object[], int, int, Object), binarySearch(Object[], Object), binarySearch(short[], int, int, short), binarySearch(short[], short), binarySearch(T[], int, int, T, Comparator), binarySearch(T[], T, Comparator), copyOf(boolean[], int), copyOf(byte[], int), copyOf(char[], int), copyOf(double[], int), copyOf(float[], int), copyOf(int[], int), copyOf(long[], int), copyOf(short[], int), copyOf(T[], int), copyOfRange(boolean[], int, int), copyOfRange(byte[], int, int), copyOfRange(char[], int, int), copyOfRange(double[], int, int), copyOfRange(float[], int, int), copyOfRange(int[], int, int), copyOfRange(long[], int, int), copyOfRange(short[], int, int), copyOfRange(T[], int, int), deepEquals(Object[], Object[]), deepHashCode(Object[]), deepToString(Object[]), equals(boolean[], boolean[]), equals(byte[], byte[]), equals(char[], char[]), equals(double[], double[]), equals(float[], float[]), equals(int[], int[]), equals(long[], long[]), equals(Object[], Object[]), equals(short[], short[]), fill(boolean[], boolean), fill(boolean[], int, int, boolean), fill(byte[], byte), fill(byte[], int, int, byte), fill(char[], char), fill(char[], int, int, char), fill(double[], double), fill(double[], int, int, double), fill(float[], float), fill(float[], int, int, float), fill(int[], int), fill(int[], int, int, int), fill(long[], int, int, long), fill(long[], long), fill(Object[], int, int, Object), fill(Object[], Object), fill(short[], int, int, short), fill(short[], short), hashCode(boolean[]), hashCode(byte[]), hashCode(char[]), hashCode(double[]), hashCode(float[]), hashCode(int[]), hashCode(long[]), hashCode(Object[]), hashCode(short[]), parallelPrefix(double[], DoubleBinaryOperator), parallelPrefix(double[], int, int, DoubleBinaryOperator), parallelPrefix(int[], IntBinaryOperator), parallelPrefix(int[], int, int, IntBinaryOperator), parallelPrefix(long[], LongBinaryOperator), parallelPrefix(long[], int, int, LongBinaryOperator), parallelPrefix(T[], BinaryOperator), parallelPrefix(T[], int, int, BinaryOperator), setAll(T[], IntFunction), setAll(double[], IntToDoubleFunction), setAll(int[], IntUnaryOperator), setAll(long[], IntToLongFunction), parallelSetAll(T[], IntFunction), parallelSetAll(double[], IntToDoubleFunction), parallelSetAll(int[], IntUnaryOperator), parallelSetAll(long[], IntToLongFunction), sort(byte[]), sort(byte[], int, int), sort(char[]), sort(char[], int, int), sort(double[]), sort(double[], int, int), sort(float[]), sort(float[], int, int), sort(int[]), sort(int[], int, int), sort(long[]), sort(long[], int, int), sort(Object[]), sort(Object[], int, int), sort(short[]), sort(short[], int, int), sort(T[], Comparator), sort(T[], int, int, Comparator), parallelSort(byte[]), parallelSort(byte[], int, int), parallelSort(char[]), parallelSort(char[], int, int), parallelSort(double[]), parallelSort(double[], int, int), parallelSort(float[]), parallelSort(float[], int, int), parallelSort(int[]), parallelSort(int[], int, int), parallelSort(long[]), parallelSort(long[], int, int), parallelSort(short[]), parallelSort(short[], int, int), parallelSort(T[]), parallelSort(T[], Comparator), parallelSort(T[], int, int), parallelSort(T[], int, int, Comparator), spliterator(double[]), spliterator(double[], int, int), spliterator(int[]), spliterator(int[], int, int), spliterator(long[]), spliterator(long[], int, int), spliterator(T[]), spliterator(T[], int, int), stream(double[]), stream(double[], int, int), stream(int[]), stream(int[], int, int), stream(long[]), stream(long[], int, int), stream(T[]), stream(T[], int, int), toString(boolean[]), toString(byte[]), toString(char[]), toString(double[]), toString(float[]), toString(int[]), toString(long[]), toString(Object[]), toString(short[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/BitSet.html">BitSet</a></dt>
  <dd>BitSet(), BitSet(int), and(BitSet), andNot(BitSet), cardinality(), clear(), clear(int), clear(int, int), clone(), equals(Object), flip(int), flip(int, int), get(int), get(int, int), hashCode(), intersects(BitSet), isEmpty(), length(), nextClearBit(int), nextSetBit(int), previousClearBit(int), previousSetBit(int), or(BitSet), set(int), set(int, boolean), set(int, int), set(int, int, boolean), size(), toString(), xor(BitSet), toByteArray(), toLongArray(), valueOf(byte[]), valueOf(long[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Collection.html">Collection</a></dt>
  <dd>add(E), addAll(Collection), clear(), contains(Object), containsAll(Collection), isEmpty(), parallelStream(), remove(Object), removeAll(Collection), removeIf(Predicate), retainAll(Collection), size(), spliterator(), stream(), toArray(), toArray(T[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Collections.html">Collections</a></dt>
  <dd style='margin-bottom: 0.5em;'>EMPTY_LIST, EMPTY_MAP, EMPTY_SET</dd>
  <dd>synchronizedCollection(Collection), synchronizedList(List), synchronizedMap(Map), synchronizedNavigableMap(NavigableMap), synchronizedNavigableSet(NavigableSet), synchronizedSet(Set), synchronizedSortedMap(SortedMap), synchronizedSortedSet(SortedSet), addAll(Collection, T[]), asLifoQueue(Deque), binarySearch(List, T), binarySearch(List, T, Comparator), copy(List, List), disjoint(Collection, Collection), emptyIterator(), emptyList(), emptyListIterator(), emptyMap(), emptySet(), enumeration(Collection), fill(List, T), frequency(Collection, Object), list(Enumeration), max(Collection), max(Collection, Comparator), min(Collection), min(Collection, Comparator), newSetFromMap(Map), nCopies(int, T), replaceAll(List, T, T), reverse(List), reverseOrder(), reverseOrder(Comparator), rotate(List, int), shuffle(List), shuffle(List, Random), singleton(T), singletonList(T), singletonMap(K, V), sort(List), sort(List, Comparator), swap(List, int, int), unmodifiableCollection(Collection), unmodifiableList(List), unmodifiableMap(Map), unmodifiableSet(Set), unmodifiableSortedMap(SortedMap), unmodifiableSortedSet(SortedSet)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Comparator.html">Comparator</a></dt>
  <dd>compare(T, T), equals(Object), reversed(), thenComparing(Comparator), thenComparing(Function, Comparator), thenComparing(Function), thenComparingInt(ToIntFunction), thenComparingLong(ToLongFunction), thenComparingDouble(ToDoubleFunction), comparing(Function, Comparator), comparing(Function), comparingDouble(ToDoubleFunction), comparingInt(ToIntFunction), comparingLong(ToLongFunction), naturalOrder(), nullsFirst(Comparator), nullsLast(Comparator), reverseOrder()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ConcurrentModificationException.html">ConcurrentModificationException</a></dt>
  <dd>ConcurrentModificationException(), ConcurrentModificationException(String), ConcurrentModificationException(Throwable), ConcurrentModificationException(String, Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Date.html">Date</a></dt>
  <dd>Date(), Date(int, int, int), Date(int, int, int, int, int), Date(int, int, int, int, int, int), Date(long), Date(String), parse(String), UTC(int, int, int, int, int, int), after(Date), before(Date), clone(), compareTo(Date), equals(Object), getDate(), getDay(), getHours(), getMinutes(), getMonth(), getSeconds(), getTime(), getTimezoneOffset(), getYear(), hashCode(), setDate(int), setHours(int), setMinutes(int), setMonth(int), setSeconds(int), setTime(long), setYear(int), toGMTString(), toLocaleString(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Deque.html">Deque</a></dt>
  <dd>addFirst(E), addLast(E), descendingIterator(), getFirst(), getLast(), offerFirst(E), offerLast(E), peekFirst(), peekLast(), pollFirst(), pollLast(), pop(), push(E), removeFirst(), removeFirstOccurrence(Object), removeLast(), removeLastOccurrence(Object)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/DoubleSummaryStatistics.html">DoubleSummaryStatistics</a></dt>
  <dd>DoubleSummaryStatistics(), accept(double), combine(DoubleSummaryStatistics), getAverage(), getCount(), getMin(), getMax(), getSum(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EmptyStackException.html">EmptyStackException</a></dt>
  <dd>EmptyStackException()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EnumMap.html">EnumMap</a></dt>
  <dd>EnumMap(Class), EnumMap(EnumMap), EnumMap(Map), clear(), clone(), containsKey(Object), containsValue(Object), entrySet(), get(Object), put(K, V), remove(Object), size()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EnumSet.html">EnumSet</a></dt>
  <dd>allOf(Class), complementOf(EnumSet), copyOf(Collection), copyOf(EnumSet), noneOf(Class), of(E), of(E, E[]), range(E, E), clone()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Enumeration.html">Enumeration</a></dt>
  <dd>hasMoreElements(), nextElement(), asIterator()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EventListener.html">EventListener</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EventObject.html">EventObject</a></dt>
  <dd>EventObject(Object), getSource()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/HashMap.html">HashMap</a></dt>
  <dd>HashMap(), HashMap(int), HashMap(int, float), HashMap(Map), clone()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/HashSet.html">HashSet</a></dt>
  <dd>HashSet(), HashSet(Collection), HashSet(int), HashSet(int, float), add(E), clear(), clone(), contains(Object), isEmpty(), iterator(), remove(Object), size()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/IdentityHashMap.html">IdentityHashMap</a></dt>
  <dd>IdentityHashMap(), IdentityHashMap(int), IdentityHashMap(Map), clone(), equals(Object), hashCode()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/IntSummaryStatistics.html">IntSummaryStatistics</a></dt>
  <dd>IntSummaryStatistics(), accept(int), combine(IntSummaryStatistics), getAverage(), getCount(), getMin(), getMax(), getSum(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Iterator.html">Iterator</a></dt>
  <dd>hasNext(), next(), forEachRemaining(Consumer), remove()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/LinkedHashMap.html">LinkedHashMap</a></dt>
  <dd>LinkedHashMap(), LinkedHashMap(int), LinkedHashMap(int, float), LinkedHashMap(int, float, boolean), LinkedHashMap(Map), clear(), clone(), containsKey(Object), containsValue(Object), entrySet(), get(Object), put(K, V), remove(Object), size()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/LinkedHashSet.html">LinkedHashSet</a></dt>
  <dd>LinkedHashSet(), LinkedHashSet(Collection), LinkedHashSet(int), LinkedHashSet(int, float), clone()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/LinkedList.html">LinkedList</a></dt>
  <dd>LinkedList(), LinkedList(Collection), add(E), addFirst(E), addLast(E), clear(), clone(), descendingIterator(), element(), getFirst(), getLast(), listIterator(int), offer(E), offerFirst(E), offerLast(E), peek(), peekFirst(), peekLast(), poll(), pollFirst(), pollLast(), pop(), push(E), remove(), removeFirst(), removeFirstOccurrence(Object), removeLast(), removeLastOccurrence(Object), size()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/List.html">List</a></dt>
  <dd>of(), of(E), of(E, E), of(E, E, E), of(E, E, E, E), of(E, E, E, E, E), of(E, E, E, E, E, E), of(E, E, E, E, E, E, E), of(E, E, E, E, E, E, E, E), of(E, E, E, E, E, E, E, E, E), of(E, E, E, E, E, E, E, E, E, E), __ofInternal(E[]), of(E[]), add(int, E), addAll(int, Collection), get(int), indexOf(Object), lastIndexOf(Object), listIterator(), listIterator(int), remove(int), replaceAll(UnaryOperator), set(int, E), sort(Comparator), spliterator(), subList(int, int), copyOf(Collection)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ListIterator.html">ListIterator</a></dt>
  <dd>add(E), hasPrevious(), nextIndex(), previous(), previousIndex(), remove(), set(E)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Locale.html">Locale</a></dt>
  <dd style='margin-bottom: 0.5em;'>ROOT, ENGLISH, US</dd>
  <dd>getDefault()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/LongSummaryStatistics.html">LongSummaryStatistics</a></dt>
  <dd>LongSummaryStatistics(), accept(int), accept(long), combine(LongSummaryStatistics), getAverage(), getCount(), getMin(), getMax(), getSum(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Map.html">Map</a></dt>
  <dd>of(), of(K, V), of(K, V, K, V), of(K, V, K, V, K, V), of(K, V, K, V, K, V, K, V), of(K, V, K, V, K, V, K, V, K, V), of(K, V, K, V, K, V, K, V, K, V, K, V), of(K, V, K, V, K, V, K, V, K, V, K, V, K, V), of(K, V, K, V, K, V, K, V, K, V, K, V, K, V, K, V), of(K, V, K, V, K, V, K, V, K, V, K, V, K, V, K, V, K, V), of(K, V, K, V, K, V, K, V, K, V, K, V, K, V, K, V, K, V, K, V), entry(K, V), ofEntries(Map.Entry[]), clear(), compute(K, BiFunction), computeIfAbsent(K, Function), computeIfPresent(K, BiFunction), containsKey(Object), containsValue(Object), entrySet(), forEach(BiConsumer), get(Object), getOrDefault(Object, V), isEmpty(), keySet(), merge(K, V, BiFunction), put(K, V), putIfAbsent(K, V), putAll(Map), remove(Object), remove(Object, Object), replace(K, V), replace(K, V, V), replaceAll(BiFunction), size(), values(), copyOf(Map)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Map.Entry.html">Map.Entry</a></dt>
  <dd>equals(Object), getKey(), getValue(), hashCode(), setValue(V), comparingByKey(), comparingByKey(Comparator), comparingByValue(), comparingByValue(Comparator)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/MissingResourceException.html">MissingResourceException</a></dt>
  <dd>MissingResourceException(String, String, String), getClassName(), getKey()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/NavigableMap.html">NavigableMap</a></dt>
  <dd>ceilingEntry(K), ceilingKey(K), descendingKeySet(), descendingMap(), firstEntry(), floorEntry(K), floorKey(K), headMap(K, boolean), higherEntry(K), higherKey(K), lastEntry(), lowerEntry(K), lowerKey(K), navigableKeySet(), pollFirstEntry(), pollLastEntry(), subMap(K, boolean, K, boolean), tailMap(K, boolean)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/NavigableSet.html">NavigableSet</a></dt>
  <dd>ceiling(E), descendingIterator(), descendingSet(), floor(E), headSet(E, boolean), higher(E), lower(E), pollFirst(), pollLast(), subSet(E, boolean, E, boolean), tailSet(E, boolean)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/NoSuchElementException.html">NoSuchElementException</a></dt>
  <dd>NoSuchElementException(), NoSuchElementException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Objects.html">Objects</a></dt>
  <dd>compare(T, T, Comparator), deepEquals(Object, Object), equals(Object, Object), equals(String, String), hashCode(Object), hash(Object[]), isNull(Object), nonNull(Object), requireNonNull(T), requireNonNull(T, String), requireNonNull(T, Supplier), toString(Object), toString(Object, String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Optional.html">Optional</a></dt>
  <dd>empty(), of(T), ofNullable(T), isPresent(), isEmpty(), get(), ifPresent(Consumer), ifPresentOrElse(Consumer, Runnable), filter(Predicate), map(Function), flatMap(Function), or(Supplier), stream(), orElse(T), orElseGet(Supplier), orElseThrow(), orElseThrow(Supplier), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/OptionalDouble.html">OptionalDouble</a></dt>
  <dd>empty(), of(double), isPresent(), isEmpty(), getAsDouble(), ifPresent(DoubleConsumer), ifPresentOrElse(DoubleConsumer, Runnable), stream(), orElse(double), orElseGet(DoubleSupplier), orElseThrow(), orElseThrow(Supplier), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/OptionalInt.html">OptionalInt</a></dt>
  <dd>empty(), of(int), isPresent(), isEmpty(), getAsInt(), ifPresent(IntConsumer), ifPresentOrElse(IntConsumer, Runnable), stream(), orElse(int), orElseGet(IntSupplier), orElseThrow(), orElseThrow(Supplier), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/OptionalLong.html">OptionalLong</a></dt>
  <dd>empty(), of(long), isPresent(), isEmpty(), getAsLong(), ifPresent(LongConsumer), ifPresentOrElse(LongConsumer, Runnable), stream(), orElse(long), orElseGet(LongSupplier), orElseThrow(), orElseThrow(Supplier), equals(Object), hashCode(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/PrimitiveIterator.html">PrimitiveIterator</a></dt>
  <dd>forEachRemaining(C)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/PrimitiveIterator.OfDouble.html">PrimitiveIterator.OfDouble</a></dt>
  <dd>nextDouble(), next(), forEachRemaining(DoubleConsumer), forEachRemaining(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/PrimitiveIterator.OfInt.html">PrimitiveIterator.OfInt</a></dt>
  <dd>nextInt(), next(), forEachRemaining(IntConsumer), forEachRemaining(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/PrimitiveIterator.OfLong.html">PrimitiveIterator.OfLong</a></dt>
  <dd>nextLong(), next(), forEachRemaining(LongConsumer), forEachRemaining(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/PriorityQueue.html">PriorityQueue</a></dt>
  <dd>PriorityQueue(), PriorityQueue(Collection), PriorityQueue(int), PriorityQueue(int, Comparator), PriorityQueue(Comparator), PriorityQueue(PriorityQueue), PriorityQueue(SortedSet), addAll(Collection), clear(), comparator(), contains(Object), iterator(), offer(E), peek(), poll(), remove(Object), removeAll(Collection), retainAll(Collection), size(), spliterator(), toArray(), toArray(T[])</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Queue.html">Queue</a></dt>
  <dd>element(), offer(E), peek(), poll(), remove()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html">Random</a></dt>
  <dd>Random(), Random(long), nextBoolean(), nextBytes(byte[]), nextDouble(), nextFloat(), nextGaussian(), nextInt(), nextInt(int), nextLong(), setSeed(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/RandomAccess.html">RandomAccess</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Set.html">Set</a></dt>
  <dd>of(), of(E), of(E, E), of(E, E, E), of(E, E, E, E), of(E, E, E, E, E), of(E, E, E, E, E, E), of(E, E, E, E, E, E, E), of(E, E, E, E, E, E, E, E), of(E, E, E, E, E, E, E, E, E), of(E, E, E, E, E, E, E, E, E, E), of(E[]), spliterator(), copyOf(Collection)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/SortedMap.html">SortedMap</a></dt>
  <dd>comparator(), firstKey(), headMap(K), lastKey(), subMap(K, K), tailMap(K)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/SortedSet.html">SortedSet</a></dt>
  <dd>comparator(), first(), headSet(E), last(), subSet(E, E), tailSet(E), spliterator()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterator.html">Spliterator</a></dt>
  <dd style='margin-bottom: 0.5em;'>DISTINCT, ORDERED, NONNULL, CONCURRENT, SORTED, SIZED, IMMUTABLE, SUBSIZED</dd>
  <dd>characteristics(), estimateSize(), forEachRemaining(Consumer), getComparator(), getExactSizeIfKnown(), hasCharacteristics(int), tryAdvance(Consumer), trySplit()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterator.OfDouble.html">Spliterator.OfDouble</a></dt>
  <dd>tryAdvance(Consumer), forEachRemaining(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterator.OfInt.html">Spliterator.OfInt</a></dt>
  <dd>tryAdvance(Consumer), forEachRemaining(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterator.OfLong.html">Spliterator.OfLong</a></dt>
  <dd>tryAdvance(Consumer), forEachRemaining(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterator.OfPrimitive.html">Spliterator.OfPrimitive</a></dt>
  <dd>tryAdvance(C), trySplit(), forEachRemaining(C)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterators.html">Spliterators</a></dt>
  <dd>emptySpliterator(), emptyDoubleSpliterator(), emptyIntSpliterator(), emptyLongSpliterator(), spliterator(Object[], int), spliterator(Object[], int, int, int), spliterator(int[], int), spliterator(int[], int, int, int), spliterator(long[], int), spliterator(long[], int, int, int), spliterator(double[], int), spliterator(double[], int, int, int), spliterator(Collection, int), spliterator(Iterator, long, int), spliteratorUnknownSize(Iterator, int), spliterator(PrimitiveIterator.OfInt, long, int), spliteratorUnknownSize(PrimitiveIterator.OfInt, int), spliterator(PrimitiveIterator.OfLong, long, int), spliteratorUnknownSize(PrimitiveIterator.OfLong, int), spliterator(PrimitiveIterator.OfDouble, long, int), spliteratorUnknownSize(PrimitiveIterator.OfDouble, int), iterator(Spliterator), iterator(Spliterator.OfDouble), iterator(Spliterator.OfInt), iterator(Spliterator.OfLong)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterators.AbstractDoubleSpliterator.html">Spliterators.AbstractDoubleSpliterator</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterators.AbstractIntSpliterator.html">Spliterators.AbstractIntSpliterator</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterators.AbstractLongSpliterator.html">Spliterators.AbstractLongSpliterator</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Spliterators.AbstractSpliterator.html">Spliterators.AbstractSpliterator</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Stack.html">Stack</a></dt>
  <dd>Stack(), clone(), empty(), peek(), pop(), push(E), search(Object)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/StringJoiner.html">StringJoiner</a></dt>
  <dd>StringJoiner(CharSequence), StringJoiner(CharSequence, CharSequence, CharSequence), add(CharSequence), length(), merge(StringJoiner), setEmptyValue(CharSequence), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/TooManyListenersException.html">TooManyListenersException</a></dt>
  <dd>TooManyListenersException(), TooManyListenersException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/TreeMap.html">TreeMap</a></dt>
  <dd>TreeMap(), TreeMap(Comparator), TreeMap(Map), TreeMap(SortedMap), clear(), comparator(), entrySet(), headMap(K, boolean), put(K, V), remove(Object), size(), subMap(K, boolean, K, boolean), tailMap(K, boolean)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/TreeSet.html">TreeSet</a></dt>
  <dd>TreeSet(), TreeSet(Collection), TreeSet(Comparator), TreeSet(SortedSet), add(E), ceiling(E), clear(), comparator(), contains(Object), descendingIterator(), descendingSet(), first(), floor(E), headSet(E), headSet(E, boolean), higher(E), iterator(), last(), lower(E), pollFirst(), pollLast(), remove(Object), size(), subSet(E, boolean, E, boolean), subSet(E, E), tailSet(E), tailSet(E, boolean)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Vector.html">Vector</a></dt>
  <dd>Vector(), Vector(Collection), Vector(int), Vector(int, int), add(E), add(int, E), addAll(Collection), addAll(int, Collection), addElement(E), capacity(), clear(), clone(), contains(Object), containsAll(Collection), copyInto(Object[]), elementAt(int), elements(), ensureCapacity(int), firstElement(), forEach(Consumer), get(int), indexOf(Object), indexOf(Object, int), insertElementAt(E, int), isEmpty(), iterator(), lastElement(), lastIndexOf(Object), lastIndexOf(Object, int), remove(int), removeAll(Collection), removeAllElements(), removeElement(Object), removeElementAt(int), removeIf(Predicate), replaceAll(UnaryOperator), set(int, E), setElementAt(E, int), setSize(int), size(), sort(Comparator), subList(int, int), toArray(), toArray(T[]), toString(), trimToSize()</dd>
</dl>

<h2 id="Package_java_util_concurrent">Package java.util.concurrent</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Callable.html">Callable</a></dt>
  <dd>call()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/CancellationException.html">CancellationException</a></dt>
  <dd>CancellationException(), CancellationException(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html">ConcurrentHashMap</a></dt>
  <dd>ConcurrentHashMap(), ConcurrentHashMap(int), ConcurrentHashMap(int, float), ConcurrentHashMap(Map), putIfAbsent(K, V), remove(Object, Object), replace(K, V, V), replace(K, V), containsKey(Object), get(Object), put(K, V), containsValue(Object), remove(Object), entrySet(), contains(Object), elements(), keys(), newKeySet()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ConcurrentMap.html">ConcurrentMap</a></dt>
  <dd>putIfAbsent(K, V), remove(Object, Object), replace(K, V), replace(K, V, V)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Delayed.html">Delayed</a></dt>
  <dd>getDelay(TimeUnit)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ExecutionException.html">ExecutionException</a></dt>
  <dd>ExecutionException(String, Throwable), ExecutionException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executor.html">Executor</a></dt>
  <dd>execute(Runnable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ExecutorService.html">ExecutorService</a></dt>
  <dd>shutdown(), shutdownNow(), isShutdown(), isTerminated(), submit(Callable), submit(Runnable, T), submit(Runnable), invokeAll(Collection), invokeAll(Collection, long, TimeUnit), invokeAny(Collection), invokeAny(Collection, long, TimeUnit)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html">Executors</a></dt>
  <dd>callable(Runnable, T), callable(Runnable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.html">Flow</a></dt>
  <dd>defaultBufferSize()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.Processor.html">Flow.Processor</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.Publisher.html">Flow.Publisher</a></dt>
  <dd>subscribe(Flow.Subscriber)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.Subscriber.html">Flow.Subscriber</a></dt>
  <dd>onSubscribe(Flow.Subscription), onNext(T), onError(Throwable), onComplete()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.Subscription.html">Flow.Subscription</a></dt>
  <dd>request(long), cancel()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Future.html">Future</a></dt>
  <dd>cancel(boolean), isCancelled(), isDone(), get(), get(long, TimeUnit)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/RejectedExecutionException.html">RejectedExecutionException</a></dt>
  <dd>RejectedExecutionException(), RejectedExecutionException(String), RejectedExecutionException(String, Throwable), RejectedExecutionException(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/RunnableFuture.html">RunnableFuture</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ScheduledExecutorService.html">ScheduledExecutorService</a></dt>
  <dd>schedule(Runnable, long, TimeUnit), schedule(Callable, long, TimeUnit), scheduleAtFixedRate(Runnable, long, long, TimeUnit), scheduleWithFixedDelay(Runnable, long, long, TimeUnit)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ScheduledFuture.html">ScheduledFuture</a></dt>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/TimeUnit.html">TimeUnit</a></dt>
  <dd style='margin-bottom: 0.5em;'>NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS</dd>
  <dd>values(), valueOf(String), convert(long, TimeUnit), toNanos(long), toMicros(long), toMillis(long), toSeconds(long), toMinutes(long), toHours(long), toDays(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/TimeoutException.html">TimeoutException</a></dt>
  <dd>TimeoutException(), TimeoutException(String)</dd>
</dl>

<h2 id="Package_java_util_concurrent_atomic">Package java.util.concurrent.atomic</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/AtomicBoolean.html">AtomicBoolean</a></dt>
  <dd>AtomicBoolean(boolean), AtomicBoolean(), get(), compareAndSet(boolean, boolean), weakCompareAndSet(boolean, boolean), set(boolean), lazySet(boolean), getAndSet(boolean), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/AtomicInteger.html">AtomicInteger</a></dt>
  <dd>AtomicInteger(int), AtomicInteger(), get(), set(int), lazySet(int), getAndSet(int), compareAndSet(int, int), getAndIncrement(), getAndDecrement(), getAndAdd(int), incrementAndGet(), decrementAndGet(), addAndGet(int), toString(), intValue(), longValue(), floatValue(), doubleValue()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/AtomicLong.html">AtomicLong</a></dt>
  <dd>AtomicLong(long), AtomicLong(), get(), set(long), lazySet(long), getAndSet(long), compareAndSet(long, long), getAndIncrement(), getAndDecrement(), getAndAdd(long), incrementAndGet(), decrementAndGet(), addAndGet(long), toString(), intValue(), longValue(), floatValue(), doubleValue()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/AtomicReference.html">AtomicReference</a></dt>
  <dd>AtomicReference(), AtomicReference(V), compareAndSet(V, V), get(), getAndSet(V), lazySet(V), set(V), weakCompareAndSet(V, V), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/atomic/AtomicReferenceArray.html">AtomicReferenceArray</a></dt>
  <dd>AtomicReferenceArray(V[]), AtomicReferenceArray(int), compareAndSet(int, V, V), get(int), getAndSet(int, V), lazySet(int, V), length(), set(int, V), weakCompareAndSet(int, V, V), toString()</dd>
</dl>

<h2 id="Package_java_util_function">Package java.util.function</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/BiConsumer.html">BiConsumer</a></dt>
  <dd>accept(T, U), andThen(BiConsumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/BiFunction.html">BiFunction</a></dt>
  <dd>apply(T, U), andThen(Function)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/BiPredicate.html">BiPredicate</a></dt>
  <dd>test(T, U), negate(), and(BiPredicate), or(BiPredicate)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/BinaryOperator.html">BinaryOperator</a></dt>
  <dd>maxBy(Comparator), minBy(Comparator)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/BooleanSupplier.html">BooleanSupplier</a></dt>
  <dd>getAsBoolean()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Consumer.html">Consumer</a></dt>
  <dd>accept(T), andThen(Consumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleBinaryOperator.html">DoubleBinaryOperator</a></dt>
  <dd>applyAsDouble(double, double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleConsumer.html">DoubleConsumer</a></dt>
  <dd>accept(double), andThen(DoubleConsumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleFunction.html">DoubleFunction</a></dt>
  <dd>apply(double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoublePredicate.html">DoublePredicate</a></dt>
  <dd>test(double), negate(), and(DoublePredicate), or(DoublePredicate)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleSupplier.html">DoubleSupplier</a></dt>
  <dd>getAsDouble()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleToIntFunction.html">DoubleToIntFunction</a></dt>
  <dd>applyAsInt(double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleToLongFunction.html">DoubleToLongFunction</a></dt>
  <dd>applyAsLong(double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/DoubleUnaryOperator.html">DoubleUnaryOperator</a></dt>
  <dd>identity(), applyAsDouble(double), andThen(DoubleUnaryOperator), compose(DoubleUnaryOperator)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Function.html">Function</a></dt>
  <dd>identity(), apply(T), andThen(Function), compose(Function)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntBinaryOperator.html">IntBinaryOperator</a></dt>
  <dd>applyAsInt(int, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntConsumer.html">IntConsumer</a></dt>
  <dd>accept(int), andThen(IntConsumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntFunction.html">IntFunction</a></dt>
  <dd>apply(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntPredicate.html">IntPredicate</a></dt>
  <dd>test(int), negate(), and(IntPredicate), or(IntPredicate)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntSupplier.html">IntSupplier</a></dt>
  <dd>getAsInt()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntToDoubleFunction.html">IntToDoubleFunction</a></dt>
  <dd>applyAsDouble(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntToLongFunction.html">IntToLongFunction</a></dt>
  <dd>applyAsLong(int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/IntUnaryOperator.html">IntUnaryOperator</a></dt>
  <dd>identity(), applyAsInt(int), andThen(IntUnaryOperator), compose(IntUnaryOperator)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongBinaryOperator.html">LongBinaryOperator</a></dt>
  <dd>applyAsLong(long, long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongConsumer.html">LongConsumer</a></dt>
  <dd>accept(long), andThen(LongConsumer)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongFunction.html">LongFunction</a></dt>
  <dd>apply(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongPredicate.html">LongPredicate</a></dt>
  <dd>test(long), negate(), and(LongPredicate), or(LongPredicate)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongSupplier.html">LongSupplier</a></dt>
  <dd>getAsLong()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongToDoubleFunction.html">LongToDoubleFunction</a></dt>
  <dd>applyAsDouble(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongToIntFunction.html">LongToIntFunction</a></dt>
  <dd>applyAsInt(long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/LongUnaryOperator.html">LongUnaryOperator</a></dt>
  <dd>identity(), applyAsLong(long), andThen(LongUnaryOperator), compose(LongUnaryOperator)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ObjDoubleConsumer.html">ObjDoubleConsumer</a></dt>
  <dd>accept(T, double)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ObjIntConsumer.html">ObjIntConsumer</a></dt>
  <dd>accept(T, int)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ObjLongConsumer.html">ObjLongConsumer</a></dt>
  <dd>accept(T, long)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Predicate.html">Predicate</a></dt>
  <dd>isEqual(Object), test(T), negate(), and(Predicate), or(Predicate), not(Predicate)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Supplier.html">Supplier</a></dt>
  <dd>get()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ToDoubleBiFunction.html">ToDoubleBiFunction</a></dt>
  <dd>applyAsDouble(T, U)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ToDoubleFunction.html">ToDoubleFunction</a></dt>
  <dd>applyAsDouble(T)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ToIntBiFunction.html">ToIntBiFunction</a></dt>
  <dd>applyAsInt(T, U)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ToIntFunction.html">ToIntFunction</a></dt>
  <dd>applyAsInt(T)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ToLongBiFunction.html">ToLongBiFunction</a></dt>
  <dd>applyAsLong(T, U)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/ToLongFunction.html">ToLongFunction</a></dt>
  <dd>applyAsLong(T)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/UnaryOperator.html">UnaryOperator</a></dt>
  <dd>identity()</dd>
</dl>

<h2 id="Package_java_util_logging">Package java.util.logging</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.logging/java/util/logging/Formatter.html">Formatter</a></dt>
  <dd>Formatter(), format(LogRecord), formatMessage(LogRecord)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.logging/java/util/logging/Handler.html">Handler</a></dt>
  <dd>Handler(), close(), flush(), getFormatter(), getLevel(), isLoggable(LogRecord), publish(LogRecord), setFormatter(Formatter), setLevel(Level)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.logging/java/util/logging/Level.html">Level</a></dt>
  <dd style='margin-bottom: 0.5em;'>ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING</dd>
  <dd>parse(String), getName(), intValue(), toString()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.logging/java/util/logging/LogManager.html">LogManager</a></dt>
  <dd>getLogManager(), addLogger(Logger), getLogger(String), getLoggerNames()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.logging/java/util/logging/LogRecord.html">LogRecord</a></dt>
  <dd>LogRecord(Level, String), getLevel(), getLoggerName(), getMessage(), getMillis(), getThrown(), setLevel(Level), setLoggerName(String), setMessage(String), setMillis(long), setThrown(Throwable)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.logging/java/util/logging/Logger.html">Logger</a></dt>
  <dd style='margin-bottom: 0.5em;'>GLOBAL_LOGGER_NAME</dd>
  <dd>getGlobal(), getLogger(String), addHandler(Handler), config(String), config(Supplier), fine(String), fine(Supplier), finer(String), finer(Supplier), finest(String), finest(Supplier), info(String), info(Supplier), warning(String), warning(Supplier), severe(String), severe(Supplier), getHandlers(), getLevel(), getName(), getParent(), getUseParentHandlers(), isLoggable(Level), log(Level, String), log(Level, Supplier), log(Level, String, Throwable), log(Level, Throwable, Supplier), log(LogRecord), removeHandler(Handler), setLevel(Level), setParent(Logger), setUseParentHandlers(boolean)</dd>
</dl>

<h2 id="Package_java_util_stream">Package java.util.stream</h2>
<dl>
  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/BaseStream.html">BaseStream</a></dt>
  <dd>iterator(), spliterator(), isParallel(), sequential(), parallel(), unordered(), onClose(Runnable), close()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Collector.html">Collector</a></dt>
  <dd>of(Supplier, BiConsumer, BinaryOperator, Function, Collector.Characteristics[]), of(Supplier, BiConsumer, BinaryOperator, Collector.Characteristics[]), supplier(), accumulator(), characteristics(), combiner(), finisher()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Collector.Characteristics.html">Collector.Characteristics</a></dt>
  <dd style='margin-bottom: 0.5em;'>CONCURRENT, IDENTITY_FINISH, UNORDERED</dd>
  <dd>values(), valueOf(String)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Collectors.html">Collectors</a></dt>
  <dd>averagingDouble(ToDoubleFunction), averagingInt(ToIntFunction), averagingLong(ToLongFunction), collectingAndThen(Collector, Function), counting(), groupingBy(Function), groupingBy(Function, Collector), groupingBy(Function, Supplier, Collector), joining(), joining(CharSequence), joining(CharSequence, CharSequence, CharSequence), mapping(Function, Collector), flatMapping(Function, Collector), filtering(Predicate, Collector), maxBy(Comparator), minBy(Comparator), partitioningBy(Predicate), partitioningBy(Predicate, Collector), reducing(BinaryOperator), reducing(T, BinaryOperator), reducing(U, Function, BinaryOperator), summarizingDouble(ToDoubleFunction), summarizingInt(ToIntFunction), summarizingLong(ToLongFunction), summingDouble(ToDoubleFunction), summingInt(ToIntFunction), summingLong(ToLongFunction), toCollection(Supplier), toList(), toUnmodifiableList(), toMap(Function, Function), toMap(Function, Function, BinaryOperator), toUnmodifiableMap(Function, Function), toUnmodifiableMap(Function, Function, BinaryOperator), toMap(Function, Function, BinaryOperator, Supplier), toSet(), toUnmodifiableSet()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/DoubleStream.html">DoubleStream</a></dt>
  <dd>builder(), concat(DoubleStream, DoubleStream), empty(), generate(DoubleSupplier), iterate(double, DoubleUnaryOperator), iterate(double, DoublePredicate, DoubleUnaryOperator), of(double[]), of(double), allMatch(DoublePredicate), anyMatch(DoublePredicate), average(), boxed(), collect(Supplier, ObjDoubleConsumer, BiConsumer), count(), distinct(), dropWhile(DoublePredicate), filter(DoublePredicate), findAny(), findFirst(), flatMap(DoubleFunction), forEach(DoubleConsumer), forEachOrdered(DoubleConsumer), iterator(), limit(long), map(DoubleUnaryOperator), mapToInt(DoubleToIntFunction), mapToLong(DoubleToLongFunction), mapToObj(DoubleFunction), max(), min(), noneMatch(DoublePredicate), parallel(), peek(DoubleConsumer), reduce(DoubleBinaryOperator), reduce(double, DoubleBinaryOperator), sequential(), skip(long), sorted(), spliterator(), sum(), summaryStatistics(), takeWhile(DoublePredicate), toArray()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/DoubleStream.Builder.html">DoubleStream.Builder</a></dt>
  <dd>accept(double), add(double), build()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/IntStream.html">IntStream</a></dt>
  <dd>builder(), concat(IntStream, IntStream), empty(), generate(IntSupplier), iterate(int, IntUnaryOperator), iterate(int, IntPredicate, IntUnaryOperator), of(int[]), of(int), range(int, int), rangeClosed(int, int), allMatch(IntPredicate), anyMatch(IntPredicate), asDoubleStream(), asLongStream(), average(), boxed(), collect(Supplier, ObjIntConsumer, BiConsumer), count(), distinct(), dropWhile(IntPredicate), filter(IntPredicate), findAny(), findFirst(), flatMap(IntFunction), forEach(IntConsumer), forEachOrdered(IntConsumer), iterator(), limit(long), map(IntUnaryOperator), mapToDouble(IntToDoubleFunction), mapToLong(IntToLongFunction), mapToObj(IntFunction), max(), min(), noneMatch(IntPredicate), parallel(), peek(IntConsumer), reduce(IntBinaryOperator), reduce(int, IntBinaryOperator), sequential(), skip(long), sorted(), spliterator(), sum(), summaryStatistics(), takeWhile(IntPredicate), toArray()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/IntStream.Builder.html">IntStream.Builder</a></dt>
  <dd>accept(int), add(int), build()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/LongStream.html">LongStream</a></dt>
  <dd>builder(), concat(LongStream, LongStream), empty(), generate(LongSupplier), iterate(long, LongUnaryOperator), iterate(long, LongPredicate, LongUnaryOperator), of(long[]), of(long), range(long, long), rangeClosed(long, long), allMatch(LongPredicate), anyMatch(LongPredicate), asDoubleStream(), average(), boxed(), collect(Supplier, ObjLongConsumer, BiConsumer), count(), distinct(), dropWhile(LongPredicate), filter(LongPredicate), findAny(), findFirst(), flatMap(LongFunction), forEach(LongConsumer), forEachOrdered(LongConsumer), iterator(), limit(long), map(LongUnaryOperator), mapToDouble(LongToDoubleFunction), mapToInt(LongToIntFunction), mapToObj(LongFunction), max(), min(), noneMatch(LongPredicate), parallel(), peek(LongConsumer), reduce(LongBinaryOperator), reduce(long, LongBinaryOperator), sequential(), skip(long), sorted(), spliterator(), sum(), summaryStatistics(), takeWhile(LongPredicate), toArray()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/LongStream.Builder.html">LongStream.Builder</a></dt>
  <dd>accept(long), add(long), build()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.html">Stream</a></dt>
  <dd>builder(), concat(Stream, Stream), empty(), generate(Supplier), iterate(T, UnaryOperator), iterate(T, Predicate, UnaryOperator), of(T), of(T[]), ofNullable(T), allMatch(Predicate), anyMatch(Predicate), collect(Collector), collect(Supplier, BiConsumer, BiConsumer), count(), distinct(), dropWhile(Predicate), filter(Predicate), findAny(), findFirst(), flatMap(Function), flatMapToDouble(Function), flatMapToInt(Function), flatMapToLong(Function), forEach(Consumer), forEachOrdered(Consumer), limit(long), map(Function), mapToDouble(ToDoubleFunction), mapToInt(ToIntFunction), mapToLong(ToLongFunction), max(Comparator), min(Comparator), noneMatch(Predicate), peek(Consumer), reduce(BinaryOperator), reduce(T, BinaryOperator), reduce(U, BiFunction, BinaryOperator), skip(long), sorted(), sorted(Comparator), takeWhile(Predicate), toArray(), toArray(IntFunction)</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.Builder.html">Stream.Builder</a></dt>
  <dd>accept(T), add(T), build()</dd>

  <dt><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/StreamSupport.html">StreamSupport</a></dt>
  <dd>doubleStream(Spliterator.OfDouble, boolean), doubleStream(Supplier, int, boolean), intStream(Spliterator.OfInt, boolean), intStream(Supplier, int, boolean), longStream(Spliterator.OfLong, boolean), longStream(Supplier, int, boolean), stream(Spliterator, boolean), stream(Supplier, int, boolean)</dd>
</dl>

