<p>
GWT includes a library that emulates a subset of the Java runtime library. 
The list below shows the set of JRE packages, types and methods that GWT can translate automatically. 
Note that in some cases, only a subset of methods is supported for a given type.
</p>

<ol class="toc" id="pageToc">
  <li><a href="#Package_java_lang">java.lang</a></li>
  <li><a href="#Package_java_lang_annotation">java.lang.annotation</a></li>
  <li><a href="#Package_java_math">java.math</a></li>
  <li><a href="#Package_java_io">java.io</a></li>
  <li><a href="#Package_java_sql">java.sql</a></li>
  <li><a href="#Package_java_util">java.util</a></li>
  <li><a href="#Package_java_util_logging">java.util.logging</a></li>
</ol>

<h1 id="Package_java_lang">Package java.lang</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Appendable.html">Appendable</a></dt>
  <dd>append(char), append(CharSequence), append(CharSequence, int, int)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/ArithmeticException.html">ArithmeticException</a></dt>
  <dd>ArithmeticException(String), ArithmeticException()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/ArrayIndexOutOfBoundsException.html">ArrayIndexOutOfBoundsException</a></dt>
  <dd>ArrayIndexOutOfBoundsException(), ArrayIndexOutOfBoundsException(int), ArrayIndexOutOfBoundsException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/ArrayStoreException.html">ArrayStoreException</a></dt>
  <dd>ArrayStoreException(), ArrayStoreException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/AssertionError.html">AssertionError</a></dt>
  <dd>AssertionError(), AssertionError(boolean), AssertionError(char), AssertionError(double), AssertionError(float), AssertionError(int), AssertionError(long), AssertionError(Object), AssertionError(String, Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html">AutoCloseable</a></dt>
  <dd>close()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Boolean.html">Boolean</a></dt>
  <dd style='margin-bottom: 0.5em;'>FALSE, TRUE, TYPE</dd>
  <dd>Boolean(boolean), Boolean(String), compare(boolean, boolean), parseBoolean(String), toString(boolean), valueOf(boolean), valueOf(String), booleanValue(), compareTo(Boolean), equals(Object), hashCode(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Byte.html">Byte</a></dt>
  <dd style='margin-bottom: 0.5em;'>MIN_VALUE, MAX_VALUE, SIZE, TYPE</dd>
  <dd>Byte(byte), Byte(String), compare(byte, byte), decode(String), hashCode(byte), parseByte(String), parseByte(String, int), toString(byte), valueOf(byte), valueOf(String), valueOf(String, int), byteValue(), compareTo(Byte), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/CharSequence.html">CharSequence</a></dt>
  <dd>charAt(int), length(), subSequence(int, int), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Character.html">Character</a></dt>
  <dd style='margin-bottom: 0.5em;'>TYPE, MIN_RADIX, MAX_RADIX, MIN_VALUE, MAX_VALUE, MIN_SURROGATE, MAX_SURROGATE, MIN_LOW_SURROGATE, MAX_LOW_SURROGATE, MIN_HIGH_SURROGATE, MAX_HIGH_SURROGATE, MIN_SUPPLEMENTARY_CODE_POINT, MIN_CODE_POINT, MAX_CODE_POINT, SIZE</dd>
  <dd>Character(char), charCount(int), codePointAt(char[], int), codePointAt(char[], int, int), codePointAt(CharSequence, int), codePointBefore(char[], int), codePointBefore(char[], int, int), codePointBefore(CharSequence, int), codePointCount(char[], int, int), codePointCount(CharSequence, int, int), compare(char, char), digit(char, int), forDigit(int, int), hashCode(char), isDigit(char), isHighSurrogate(char), isLetter(char), isLetterOrDigit(char), isLowerCase(char), isLowSurrogate(char), isSpace(char), isSupplementaryCodePoint(int), isSurrogatePair(char, char), isUpperCase(char), isValidCodePoint(int), offsetByCodePoints(char[], int, int, int, int), offsetByCodePoints(CharSequence, int, int), toChars(int), toChars(int, char[], int), toCodePoint(char, char), toLowerCase(char), toString(char), toUpperCase(char), valueOf(char), charValue(), compareTo(Character), equals(Object), hashCode(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Class.html">Class</a></dt>
  <dd>isClassMetadataEnabled(), desiredAssertionStatus(), getCanonicalName(), getComponentType(), getEnumConstants(), getName(), getSimpleName(), getSuperclass(), isArray(), isEnum(), isInterface(), isPrimitive(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/ClassCastException.html">ClassCastException</a></dt>
  <dd>ClassCastException(), ClassCastException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Cloneable.html">Cloneable</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Comparable.html">Comparable</a></dt>
  <dd>compareTo(T)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Deprecated.html">Deprecated</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Double.html">Double</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, MIN_NORMAL, MAX_EXPONENT, MIN_EXPONENT, NaN, NEGATIVE_INFINITY, POSITIVE_INFINITY, SIZE, TYPE</dd>
  <dd>Double(double), Double(String), compare(double, double), doubleToLongBits(double), hashCode(double), isInfinite(double), isNaN(double), longBitsToDouble(long), parseDouble(String), toString(double), valueOf(double), valueOf(String), byteValue(), compareTo(Double), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), isInfinite(), isNaN(), longValue(), shortValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Enum.html">Enum</a></dt>
  <dd>valueOf(Class, String), compareTo(E), equals(Object), getDeclaringClass(), hashCode(), name(), ordinal(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Error.html">Error</a></dt>
  <dd>Error(), Error(String, Throwable), Error(String), Error(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Exception.html">Exception</a></dt>
  <dd>Exception(), Exception(String), Exception(String, Throwable), Exception(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Float.html">Float</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, MAX_EXPONENT, MIN_EXPONENT, MIN_NORMAL, NaN, NEGATIVE_INFINITY, POSITIVE_INFINITY, SIZE, TYPE</dd>
  <dd>Float(double), Float(float), Float(String), compare(float, float), floatToIntBits(float), hashCode(float), intBitsToFloat(int), isInfinite(float), isNaN(float), parseFloat(String), toString(float), valueOf(float), valueOf(String), byteValue(), compareTo(Float), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), isInfinite(), isNaN(), longValue(), shortValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a></dt>
  <dd>IllegalArgumentException(), IllegalArgumentException(String), IllegalArgumentException(String, Throwable), IllegalArgumentException(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalStateException.html">IllegalStateException</a></dt>
  <dd>IllegalStateException(), IllegalStateException(String), IllegalStateException(String, Throwable), IllegalStateException(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IndexOutOfBoundsException.html">IndexOutOfBoundsException</a></dt>
  <dd>IndexOutOfBoundsException(), IndexOutOfBoundsException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html">Integer</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, SIZE, TYPE</dd>
  <dd>Integer(int), Integer(String), bitCount(int), compare(int, int), decode(String), hashCode(int), highestOneBit(int), lowestOneBit(int), numberOfLeadingZeros(int), numberOfTrailingZeros(int), parseInt(String), parseInt(String, int), reverse(int), reverseBytes(int), rotateLeft(int, int), rotateRight(int, int), signum(int), toBinaryString(int), toHexString(int), toOctalString(int), toString(int), toString(int, int), valueOf(int), valueOf(String), valueOf(String, int), byteValue(), compareTo(Integer), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Iterable.html">Iterable</a></dt>
  <dd>iterator()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Long.html">Long</a></dt>
  <dd style='margin-bottom: 0.5em;'>MAX_VALUE, MIN_VALUE, SIZE, TYPE</dd>
  <dd>Long(long), Long(String), bitCount(long), compare(long, long), decode(String), hashCode(long), highestOneBit(long), lowestOneBit(long), numberOfLeadingZeros(long), numberOfTrailingZeros(long), parseLong(String), parseLong(String, int), reverse(long), reverseBytes(long), rotateLeft(long, int), rotateRight(long, int), signum(long), toBinaryString(long), toHexString(long), toOctalString(long), toString(long), toString(long, int), valueOf(long), valueOf(String), valueOf(String, int), byteValue(), compareTo(Long), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Math.html">Math</a></dt>
  <dd style='margin-bottom: 0.5em;'>E, PI</dd>
  <dd>Math(), abs(double), abs(float), abs(int), abs(long), acos(double), asin(double), atan(double), atan2(double, double), cbrt(double), ceil(double), copySign(double, double), copySign(float, float), cos(double), cosh(double), exp(double), expm1(double), floor(double), hypot(double, double), log(double), log10(double), log1p(double), max(double, double), max(float, float), max(int, int), max(long, long), min(double, double), min(float, float), min(int, int), min(long, long), pow(double, double), random(), rint(double), round(double), round(float), scalb(double, int), scalb(float, int), signum(double), signum(float), sin(double), sinh(double), sqrt(double), tan(double), tanh(double), toDegrees(double), toRadians(double)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/NegativeArraySizeException.html">NegativeArraySizeException</a></dt>
  <dd>NegativeArraySizeException(), NegativeArraySizeException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/NoSuchMethodException.html">NoSuchMethodException</a></dt>
  <dd>NoSuchMethodException(), NoSuchMethodException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/NullPointerException.html">NullPointerException</a></dt>
  <dd>NullPointerException(), NullPointerException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Number.html">Number</a></dt>
  <dd>Number(), byteValue(), doubleValue(), floatValue(), intValue(), longValue(), shortValue()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/NumberFormatException.html">NumberFormatException</a></dt>
  <dd>NumberFormatException(), NumberFormatException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Object.html">Object</a></dt>
  <dd>Object(), equals(Object), getClass(), hashCode(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Override.html">Override</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html">Runnable</a></dt>
  <dd>run()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/RuntimeException.html">RuntimeException</a></dt>
  <dd>RuntimeException(), RuntimeException(String), RuntimeException(String, Throwable), RuntimeException(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Short.html">Short</a></dt>
  <dd style='margin-bottom: 0.5em;'>MIN_VALUE, MAX_VALUE, SIZE, TYPE</dd>
  <dd>Short(short), Short(String), compare(short, short), decode(String), hashCode(short), parseShort(String), parseShort(String, int), reverseBytes(short), toString(short), valueOf(short), valueOf(String), valueOf(String, int), byteValue(), compareTo(Short), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), longValue(), shortValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/StackTraceElement.html">StackTraceElement</a></dt>
  <dd>StackTraceElement(), StackTraceElement(String, String, String, int), getClassName(), getFileName(), getLineNumber(), getMethodName(), equals(Object), hashCode(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/String.html">String</a></dt>
  <dd style='margin-bottom: 0.5em;'>CASE_INSENSITIVE_ORDER</dd>
  <dd>String(), String(byte[]), String(byte[], int, int), String(byte[], int, int, String), String(byte[], String), String(char[]), String(char[], int, int), String(int[], int, int), String(String), String(StringBuffer), String(StringBuilder), copyValueOf(char[]), copyValueOf(char[], int, int), valueOf(boolean), valueOf(char), valueOf(char[], int, int), valueOf(char[]), valueOf(double), valueOf(float), valueOf(int), valueOf(long), valueOf(Object), charAt(int), codePointAt(int), codePointBefore(int), codePointCount(int, int), compareTo(String), compareToIgnoreCase(String), concat(String), contains(CharSequence), contentEquals(CharSequence), contentEquals(StringBuffer), endsWith(String), equals(Object), equalsIgnoreCase(String), getBytes(), getBytes(String), getChars(int, int, char[], int), getClass(), hashCode(), indexOf(int), indexOf(int, int), indexOf(String), indexOf(String, int), intern(), isEmpty(), lastIndexOf(int), lastIndexOf(int, int), lastIndexOf(String), lastIndexOf(String, int), length(), matches(String), offsetByCodePoints(int, int), regionMatches(boolean, int, String, int, int), regionMatches(int, String, int, int), replace(char, char), replace(CharSequence, CharSequence), replaceAll(String, String), replaceFirst(String, String), split(String), split(String, int), startsWith(String), startsWith(String, int), subSequence(int, int), substring(int), substring(int, int), toCharArray(), toLowerCase(), toLowerCase(Locale), toUpperCase(), toUpperCase(Locale), toString(), trim()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/StringBuffer.html">StringBuffer</a></dt>
  <dd>StringBuffer(), StringBuffer(CharSequence), StringBuffer(int), StringBuffer(String), append(boolean), append(char), append(char[]), append(char[], int, int), append(CharSequence), append(CharSequence, int, int), append(double), append(float), append(int), append(long), append(Object), append(String), append(StringBuffer), appendCodePoint(int), delete(int, int), deleteCharAt(int), insert(int, boolean), insert(int, char), insert(int, char[]), insert(int, char[], int, int), insert(int, CharSequence), insert(int, CharSequence, int, int), insert(int, double), insert(int, float), insert(int, int), insert(int, long), insert(int, Object), insert(int, String), replace(int, int, String), reverse()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/StringBuilder.html">StringBuilder</a></dt>
  <dd>StringBuilder(), StringBuilder(CharSequence), StringBuilder(int), StringBuilder(String), append(boolean), append(char), append(char[]), append(char[], int, int), append(CharSequence), append(CharSequence, int, int), append(double), append(float), append(int), append(long), append(Object), append(String), append(StringBuffer), appendCodePoint(int), delete(int, int), deleteCharAt(int), insert(int, boolean), insert(int, char), insert(int, char[]), insert(int, char[], int, int), insert(int, CharSequence), insert(int, CharSequence, int, int), insert(int, double), insert(int, float), insert(int, int), insert(int, long), insert(int, Object), insert(int, String), replace(int, int, String), reverse()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/StringIndexOutOfBoundsException.html">StringIndexOutOfBoundsException</a></dt>
  <dd>StringIndexOutOfBoundsException(), StringIndexOutOfBoundsException(String), StringIndexOutOfBoundsException(int)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/SuppressWarnings.html">SuppressWarnings</a></dt>
  <dd>value()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/System.html">System</a></dt>
  <dd style='margin-bottom: 0.5em;'>err, out</dd>
  <dd>System(), arraycopy(Object, int, Object, int, int), currentTimeMillis(), gc(), getProperty(String, String), identityHashCode(Object), setErr(PrintStream), setOut(PrintStream)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Throwable.html">Throwable</a></dt>
  <dd>Throwable(), Throwable(String), Throwable(String, Throwable), Throwable(Throwable), addSuppressed(Throwable), fillInStackTrace(), getCause(), getLocalizedMessage(), getMessage(), getStackTrace(), getSuppressed(), initCause(Throwable), printStackTrace(), printStackTrace(PrintStream), setStackTrace(StackTraceElement[]), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/UnsupportedOperationException.html">UnsupportedOperationException</a></dt>
  <dd>UnsupportedOperationException(), UnsupportedOperationException(String), UnsupportedOperationException(String, Throwable), UnsupportedOperationException(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Void.html">Void</a></dt>
  <dd style='margin-bottom: 0.5em;'>TYPE</dd>
</dl>

<h1 id="Package_java_lang_annotation">Package java.lang.annotation</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/Annotation.html">Annotation</a></dt>
  <dd>annotationType(), equals(Object), hashCode(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/AnnotationFormatError.html">AnnotationFormatError</a></dt>
  <dd>AnnotationFormatError()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/AnnotationTypeMismatchException.html">AnnotationTypeMismatchException</a></dt>
  <dd>AnnotationTypeMismatchException()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/Documented.html">Documented</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/ElementType.html">ElementType</a></dt>
  <dd style='margin-bottom: 0.5em;'>ANNOTATION_TYPE, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE</dd>
  <dd>values(), valueOf(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/IncompleteAnnotationException.html">IncompleteAnnotationException</a></dt>
  <dd style='margin-bottom: 0.5em;'>annotationType, elementName</dd>
  <dd>IncompleteAnnotationException(Class, String), annotationType(), elementName()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/Inherited.html">Inherited</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/Retention.html">Retention</a></dt>
  <dd>value()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/RetentionPolicy.html">RetentionPolicy</a></dt>
  <dd style='margin-bottom: 0.5em;'>CLASS, RUNTIME, SOURCE</dd>
  <dd>values(), valueOf(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/Target.html">Target</a></dt>
  <dd>value()</dd>
</dl>

<h1 id="Package_java_math">Package java.math</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html">BigDecimal</a></dt>
  <dd style='margin-bottom: 0.5em;'>ONE, ROUND_CEILING, ROUND_DOWN, ROUND_FLOOR, ROUND_HALF_DOWN, ROUND_HALF_EVEN, ROUND_HALF_UP, ROUND_UNNECESSARY, ROUND_UP, TEN, ZERO</dd>
  <dd>BigDecimal(BigInteger), BigDecimal(BigInteger, int), BigDecimal(BigInteger, int, MathContext), BigDecimal(BigInteger, MathContext), BigDecimal(char[]), BigDecimal(char[], int, int), BigDecimal(char[], int, int, MathContext), BigDecimal(char[], MathContext), BigDecimal(double), BigDecimal(double, MathContext), BigDecimal(int), BigDecimal(int, MathContext), BigDecimal(long), BigDecimal(long, MathContext), BigDecimal(String), BigDecimal(String, MathContext), valueOf(double), valueOf(long), valueOf(long, int), abs(), abs(MathContext), add(BigDecimal), add(BigDecimal, MathContext), byteValueExact(), compareTo(BigDecimal), divide(BigDecimal), divide(BigDecimal, int), divide(BigDecimal, int, int), divide(BigDecimal, int, RoundingMode), divide(BigDecimal, MathContext), divide(BigDecimal, RoundingMode), divideAndRemainder(BigDecimal), divideAndRemainder(BigDecimal, MathContext), divideToIntegralValue(BigDecimal), divideToIntegralValue(BigDecimal, MathContext), doubleValue(), equals(Object), floatValue(), hashCode(), intValue(), intValueExact(), longValue(), longValueExact(), max(BigDecimal), min(BigDecimal), movePointLeft(int), movePointRight(int), multiply(BigDecimal), multiply(BigDecimal, MathContext), negate(), negate(MathContext), plus(), plus(MathContext), pow(int), pow(int, MathContext), precision(), remainder(BigDecimal), remainder(BigDecimal, MathContext), round(MathContext), scale(), scaleByPowerOfTen(int), setScale(int), setScale(int, int), setScale(int, RoundingMode), shortValueExact(), signum(), stripTrailingZeros(), subtract(BigDecimal), subtract(BigDecimal, MathContext), toBigInteger(), toBigIntegerExact(), toEngineeringString(), toPlainString(), toString(), ulp(), unscaledValue()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/math/BigInteger.html">BigInteger</a></dt>
  <dd style='margin-bottom: 0.5em;'>ONE, TEN, ZERO</dd>
  <dd>BigInteger(byte[]), BigInteger(int, byte[]), BigInteger(int, int, Random), BigInteger(int, Random), BigInteger(String), BigInteger(String, int), probablePrime(int, Random), valueOf(long), abs(), add(BigInteger), and(BigInteger), andNot(BigInteger), bitCount(), bitLength(), clearBit(int), compareTo(BigInteger), divide(BigInteger), divideAndRemainder(BigInteger), doubleValue(), equals(Object), flipBit(int), floatValue(), gcd(BigInteger), getLowestSetBit(), hashCode(), intValue(), isProbablePrime(int), longValue(), max(BigInteger), min(BigInteger), mod(BigInteger), modInverse(BigInteger), modPow(BigInteger, BigInteger), multiply(BigInteger), negate(), nextProbablePrime(), not(), or(BigInteger), pow(int), remainder(BigInteger), setBit(int), shiftLeft(int), shiftRight(int), signum(), subtract(BigInteger), testBit(int), toByteArray(), toString(), toString(int), xor(BigInteger)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/math/MathContext.html">MathContext</a></dt>
  <dd style='margin-bottom: 0.5em;'>DECIMAL128, DECIMAL32, DECIMAL64, UNLIMITED</dd>
  <dd>MathContext(int), MathContext(int, RoundingMode), MathContext(String), equals(Object), getPrecision(), getRoundingMode(), hashCode(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/math/RoundingMode.html">RoundingMode</a></dt>
  <dd style='margin-bottom: 0.5em;'>UP, DOWN, CEILING, FLOOR, HALF_UP, HALF_DOWN, HALF_EVEN, UNNECESSARY</dd>
  <dd>values(), valueOf(String), valueOf(int)</dd>
</dl>

<h1 id="Package_java_io">Package java.io</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/io/FilterOutputStream.html">FilterOutputStream</a></dt>
  <dd>FilterOutputStream(OutputStream)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/io/IOException.html">IOException</a></dt>
  <dd>IOException(), IOException(String), IOException(String, Throwable), IOException(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/io/OutputStream.html">OutputStream</a></dt>
  <dd>OutputStream()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/io/PrintStream.html">PrintStream</a></dt>
  <dd>PrintStream(OutputStream), print(boolean), print(char), print(char[]), print(double), print(float), print(int), print(long), print(Object), print(String), println(), println(boolean), println(char), println(char[]), println(double), println(float), println(int), println(long), println(Object), println(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">Serializable</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/io/UnsupportedEncodingException.html">UnsupportedEncodingException</a></dt>
  <dd>UnsupportedEncodingException(), UnsupportedEncodingException(String)</dd>
</dl>

<h1 id="Package_java_sql">Package java.sql</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/sql/Date.html">Date</a></dt>
  <dd>Date(int, int, int), Date(long), valueOf(String), getHours(), getMinutes(), getSeconds(), setHours(int), setMinutes(int), setSeconds(int), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/sql/Time.html">Time</a></dt>
  <dd>Time(int, int, int), Time(long), valueOf(String), getDate(), getDay(), getMonth(), getYear(), setDate(int), setMonth(int), setYear(int), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/sql/Timestamp.html">Timestamp</a></dt>
  <dd>Timestamp(int, int, int, int, int, int, int), Timestamp(long), valueOf(String), after(Timestamp), before(Timestamp), compareTo(Date), compareTo(Timestamp), equals(Object), equals(Timestamp), getNanos(), getTime(), hashCode(), setNanos(int), setTime(long), toString()</dd>
</dl>

<h1 id="Package_java_util">Package java.util</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractCollection.html">AbstractCollection</a></dt>
  <dd>add(E), addAll(Collection), clear(), contains(Object), containsAll(Collection), isEmpty(), iterator(), remove(Object), removeAll(Collection), retainAll(Collection), size(), toArray(), toArray(T[]), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractList.html">AbstractList</a></dt>
  <dd>add(E), add(int, E), addAll(int, Collection), clear(), equals(Object), get(int), hashCode(), indexOf(Object), iterator(), lastIndexOf(Object), listIterator(), listIterator(int), remove(int), set(int, E), subList(int, int)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractMap.html">AbstractMap</a></dt>
  <dd>clear(), containsKey(Object), containsValue(Object), entrySet(), equals(Object), get(Object), hashCode(), isEmpty(), keySet(), put(K, V), putAll(Map), remove(Object), size(), toString(), values()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractMap.SimpleEntry.html">AbstractMap.SimpleEntry</a></dt>
  <dd>AbstractMap.SimpleEntry(K, V), AbstractMap.SimpleEntry(Map.Entry)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractMap.SimpleImmutableEntry.html">AbstractMap.SimpleImmutableEntry</a></dt>
  <dd>AbstractMap.SimpleImmutableEntry(K, V), AbstractMap.SimpleImmutableEntry(Map.Entry), setValue(V)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractQueue.html">AbstractQueue</a></dt>
  <dd>add(E), addAll(Collection), clear(), element(), offer(E), peek(), poll(), remove()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractSequentialList.html">AbstractSequentialList</a></dt>
  <dd>add(int, E), addAll(int, Collection), get(int), iterator(), listIterator(int), remove(int), set(int, E), size()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/AbstractSet.html">AbstractSet</a></dt>
  <dd>AbstractSet(), equals(Object), hashCode(), removeAll(Collection)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html">ArrayList</a></dt>
  <dd>ArrayList(), ArrayList(Collection), ArrayList(int), add(E), add(int, E), addAll(Collection), addAll(int, Collection), clear(), clone(), contains(Object), ensureCapacity(int), get(int), indexOf(Object), isEmpty(), lastIndexOf(Object), remove(int), remove(Object), set(int, E), size(), toArray(), toArray(T[]), trimToSize()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html">Arrays</a></dt>
  <dd>Arrays(), asList(T[]), binarySearch(byte[], byte), binarySearch(char[], char), binarySearch(double[], double), binarySearch(float[], float), binarySearch(int[], int), binarySearch(long[], long), binarySearch(Object[], Object), binarySearch(short[], short), binarySearch(T[], T, Comparator), copyOf(boolean[], int), copyOf(byte[], int), copyOf(char[], int), copyOf(double[], int), copyOf(float[], int), copyOf(int[], int), copyOf(long[], int), copyOf(short[], int), copyOf(T[], int), copyOfRange(boolean[], int, int), copyOfRange(byte[], int, int), copyOfRange(char[], int, int), copyOfRange(double[], int, int), copyOfRange(float[], int, int), copyOfRange(int[], int, int), copyOfRange(long[], int, int), copyOfRange(short[], int, int), copyOfRange(T[], int, int), deepEquals(Object[], Object[]), deepHashCode(Object[]), deepToString(Object[]), equals(boolean[], boolean[]), equals(byte[], byte[]), equals(char[], char[]), equals(double[], double[]), equals(float[], float[]), equals(int[], int[]), equals(long[], long[]), equals(Object[], Object[]), equals(short[], short[]), fill(boolean[], boolean), fill(boolean[], int, int, boolean), fill(byte[], byte), fill(byte[], int, int, byte), fill(char[], char), fill(char[], int, int, char), fill(double[], double), fill(double[], int, int, double), fill(float[], float), fill(float[], int, int, float), fill(int[], int), fill(int[], int, int, int), fill(long[], int, int, long), fill(long[], long), fill(Object[], int, int, Object), fill(Object[], Object), fill(short[], int, int, short), fill(short[], short), hashCode(boolean[]), hashCode(byte[]), hashCode(char[]), hashCode(double[]), hashCode(float[]), hashCode(int[]), hashCode(long[]), hashCode(Object[]), hashCode(short[]), sort(byte[]), sort(byte[], int, int), sort(char[]), sort(char[], int, int), sort(double[]), sort(double[], int, int), sort(float[]), sort(float[], int, int), sort(int[]), sort(int[], int, int), sort(long[]), sort(long[], int, int), sort(Object[]), sort(Object[], int, int), sort(short[]), sort(short[], int, int), sort(T[], Comparator), sort(T[], int, int, Comparator), toString(boolean[]), toString(byte[]), toString(char[]), toString(double[]), toString(float[]), toString(int[]), toString(long[]), toString(Object[]), toString(short[])</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Collection.html">Collection</a></dt>
  <dd>add(E), addAll(Collection), clear(), contains(Object), containsAll(Collection), equals(Object), hashCode(), isEmpty(), iterator(), remove(Object), removeAll(Collection), retainAll(Collection), size(), toArray(), toArray(T[])</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Collections.html">Collections</a></dt>
  <dd style='margin-bottom: 0.5em;'>EMPTY_LIST, EMPTY_MAP, EMPTY_SET</dd>
  <dd>Collections(), addAll(Collection, T[]), asLifoQueue(Deque), binarySearch(List, T), binarySearch(List, T, Comparator), copy(List, List), disjoint(Collection, Collection), emptyIterator(), emptyList(), emptyListIterator(), emptyMap(), emptySet(), enumeration(Collection), fill(List, T), frequency(Collection, Object), list(Enumeration), max(Collection), max(Collection, Comparator), min(Collection), min(Collection, Comparator), newSetFromMap(Map), nCopies(int, T), replaceAll(List, T, T), reverse(List), reverseOrder(), reverseOrder(Comparator), singleton(T), singletonList(T), singletonMap(K, V), sort(List), sort(List, Comparator), swap(List, int, int), unmodifiableCollection(Collection), unmodifiableList(List), unmodifiableMap(Map), unmodifiableSet(Set), unmodifiableSortedMap(SortedMap), unmodifiableSortedSet(SortedSet)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Comparator.html">Comparator</a></dt>
  <dd>compare(T, T), equals(Object)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/ConcurrentModificationException.html">ConcurrentModificationException</a></dt>
  <dd>ConcurrentModificationException(), ConcurrentModificationException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Date.html">Date</a></dt>
  <dd>Date(), Date(int, int, int), Date(int, int, int, int, int), Date(int, int, int, int, int, int), Date(long), Date(String), parse(String), UTC(int, int, int, int, int, int), after(Date), before(Date), clone(), compareTo(Date), equals(Object), getDate(), getDay(), getHours(), getMinutes(), getMonth(), getSeconds(), getTime(), getTimezoneOffset(), getYear(), hashCode(), setDate(int), setHours(int), setMinutes(int), setMonth(int), setSeconds(int), setTime(long), setYear(int), toGMTString(), toLocaleString(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Deque.html">Deque</a></dt>
  <dd>addFirst(E), addLast(E), descendingIterator(), getFirst(), getLast(), offerFirst(E), offerLast(E), peekFirst(), peekLast(), pollFirst(), pollLast(), pop(), push(E), removeFirst(), removeFirstOccurrence(Object), removeLast(), removeLastOccurrence(Object)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/EmptyStackException.html">EmptyStackException</a></dt>
  <dd>EmptyStackException()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html">EnumMap</a></dt>
  <dd>EnumMap(Class), EnumMap(EnumMap), EnumMap(Map), clear(), clone(), containsKey(Object), containsValue(Object), entrySet(), get(Object), put(K, V), remove(Object), size()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/EnumSet.html">EnumSet</a></dt>
  <dd>allOf(Class), complementOf(EnumSet), copyOf(Collection), copyOf(EnumSet), noneOf(Class), of(E), of(E, E[]), range(E, E), clone()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Enumeration.html">Enumeration</a></dt>
  <dd>hasMoreElements(), nextElement()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/EventListener.html">EventListener</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/EventObject.html">EventObject</a></dt>
  <dd>EventObject(Object), getSource()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html">HashMap</a></dt>
  <dd>HashMap(), HashMap(int), HashMap(int, float), HashMap(Map), clone()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/HashSet.html">HashSet</a></dt>
  <dd>HashSet(), HashSet(Collection), HashSet(int), HashSet(int, float), add(E), clear(), clone(), contains(Object), isEmpty(), iterator(), remove(Object), size(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/IdentityHashMap.html">IdentityHashMap</a></dt>
  <dd>IdentityHashMap(), IdentityHashMap(int), IdentityHashMap(Map), clone(), equals(Object), hashCode()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html">Iterator</a></dt>
  <dd>hasNext(), next(), remove()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/LinkedHashMap.html">LinkedHashMap</a></dt>
  <dd>LinkedHashMap(), LinkedHashMap(int), LinkedHashMap(int, float), LinkedHashMap(int, float, boolean), LinkedHashMap(Map), clear(), clone(), containsKey(Object), containsValue(Object), entrySet(), get(Object), put(K, V), remove(Object), size()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/LinkedHashSet.html">LinkedHashSet</a></dt>
  <dd>LinkedHashSet(), LinkedHashSet(Collection), LinkedHashSet(int), LinkedHashSet(int, float), clone()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html">LinkedList</a></dt>
  <dd>LinkedList(), LinkedList(Collection), add(E), addFirst(E), addLast(E), clear(), clone(), descendingIterator(), element(), getFirst(), getLast(), listIterator(int), offer(E), offerFirst(E), offerLast(E), peek(), peekFirst(), peekLast(), poll(), pollFirst(), pollLast(), pop(), push(E), remove(), removeFirst(), removeFirstOccurrence(Object), removeLast(), removeLastOccurrence(Object), size()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/List.html">List</a></dt>
  <dd>add(E), add(int, E), addAll(Collection), addAll(int, Collection), clear(), contains(Object), containsAll(Collection), equals(Object), get(int), hashCode(), indexOf(Object), isEmpty(), iterator(), lastIndexOf(Object), listIterator(), listIterator(int), remove(int), remove(Object), removeAll(Collection), retainAll(Collection), set(int, E), size(), subList(int, int), toArray(), toArray(T[])</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/ListIterator.html">ListIterator</a></dt>
  <dd>add(E), hasNext(), hasPrevious(), next(), nextIndex(), previous(), previousIndex(), remove(), set(E)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Locale.html">Locale</a></dt>
  <dd style='margin-bottom: 0.5em;'>ROOT, ENGLISH, US</dd>
  <dd>getDefault()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Map.html">Map</a></dt>
  <dd>clear(), containsKey(Object), containsValue(Object), entrySet(), equals(Object), get(Object), hashCode(), isEmpty(), keySet(), put(K, V), putAll(Map), remove(Object), size(), values()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Map.Entry.html">Map.Entry</a></dt>
  <dd>equals(Object), getKey(), getValue(), hashCode(), setValue(V)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/MissingResourceException.html">MissingResourceException</a></dt>
  <dd>MissingResourceException(String, String, String), getClassName(), getKey()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/NavigableMap.html">NavigableMap</a></dt>
  <dd>ceilingEntry(K), ceilingKey(K), descendingKeySet(), descendingMap(), firstEntry(), floorEntry(K), floorKey(K), headMap(K, boolean), higherEntry(K), higherKey(K), lastEntry(), lowerEntry(K), lowerKey(K), navigableKeySet(), pollFirstEntry(), pollLastEntry(), subMap(K, boolean, K, boolean), tailMap(K, boolean)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/NavigableSet.html">NavigableSet</a></dt>
  <dd>ceiling(E), descendingIterator(), descendingSet(), floor(E), headSet(E, boolean), higher(E), lower(E), pollFirst(), pollLast(), subSet(E, boolean, E, boolean), tailSet(E, boolean)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/NoSuchElementException.html">NoSuchElementException</a></dt>
  <dd>NoSuchElementException(), NoSuchElementException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Objects.html">Objects</a></dt>
  <dd>compare(T, T, Comparator), deepEquals(Object, Object), equals(Object, Object), hashCode(Object), hash(Object[]), requireNonNull(T), requireNonNull(T, String), toString(Object), toString(Object, String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html">PriorityQueue</a></dt>
  <dd>PriorityQueue(), PriorityQueue(Collection), PriorityQueue(int), PriorityQueue(int, Comparator), PriorityQueue(PriorityQueue), PriorityQueue(SortedSet), addAll(Collection), clear(), comparator(), contains(Object), containsAll(Collection), isEmpty(), iterator(), offer(E), peek(), poll(), remove(Object), removeAll(Collection), retainAll(Collection), size(), toArray(), toArray(T[]), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Queue.html">Queue</a></dt>
  <dd>element(), offer(E), peek(), poll(), remove()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Random.html">Random</a></dt>
  <dd>Random(), Random(long), nextBoolean(), nextBytes(byte[]), nextDouble(), nextFloat(), nextGaussian(), nextInt(), nextInt(int), nextLong(), setSeed(long)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/RandomAccess.html">RandomAccess</a></dt>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Set.html">Set</a></dt>
  <dd>add(E), addAll(Collection), clear(), contains(Object), containsAll(Collection), equals(Object), hashCode(), isEmpty(), iterator(), remove(Object), removeAll(Collection), retainAll(Collection), size(), toArray(), toArray(T[])</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/SortedMap.html">SortedMap</a></dt>
  <dd>comparator(), firstKey(), headMap(K), lastKey(), subMap(K, K), tailMap(K)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/SortedSet.html">SortedSet</a></dt>
  <dd>comparator(), first(), headSet(E), last(), subSet(E, E), tailSet(E)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Stack.html">Stack</a></dt>
  <dd>Stack(), clone(), empty(), peek(), pop(), push(E), search(Object)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/TooManyListenersException.html">TooManyListenersException</a></dt>
  <dd>TooManyListenersException(), TooManyListenersException(String)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/TreeMap.html">TreeMap</a></dt>
  <dd>TreeMap(), TreeMap(Comparator), TreeMap(Map), TreeMap(SortedMap), clear(), comparator(), entrySet(), headMap(K, boolean), put(K, V), remove(Object), size(), subMap(K, boolean, K, boolean), tailMap(K, boolean)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/TreeSet.html">TreeSet</a></dt>
  <dd>TreeSet(), TreeSet(Collection), TreeSet(Comparator), TreeSet(SortedSet), add(E), ceiling(E), clear(), comparator(), contains(Object), descendingIterator(), descendingSet(), first(), floor(E), headSet(E), headSet(E, boolean), higher(E), iterator(), last(), lower(E), pollFirst(), pollLast(), remove(Object), size(), subSet(E, boolean, E, boolean), subSet(E, E), tailSet(E), tailSet(E, boolean)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/Vector.html">Vector</a></dt>
  <dd>Vector(), Vector(Collection), Vector(int), Vector(int, int), add(E), add(int, E), addAll(Collection), addAll(int, Collection), addElement(E), capacity(), clear(), clone(), contains(Object), containsAll(Collection), copyInto(Object[]), elementAt(int), elements(), ensureCapacity(int), firstElement(), get(int), indexOf(Object), indexOf(Object, int), insertElementAt(E, int), isEmpty(), iterator(), lastElement(), lastIndexOf(Object), lastIndexOf(Object, int), remove(int), removeAll(Collection), removeAllElements(), removeElement(Object), removeElementAt(int), set(int, E), setElementAt(E, int), setSize(int), size(), subList(int, int), toArray(), toArray(T[]), toString(), trimToSize()</dd>
</dl>

<h1 id="Package_java_util_logging">Package java.util.logging</h1>
<dl>
  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/Formatter.html">Formatter</a></dt>
  <dd>Formatter(), format(LogRecord), formatMessage(LogRecord)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/Handler.html">Handler</a></dt>
  <dd>Handler(), close(), flush(), getFormatter(), getLevel(), isLoggable(LogRecord), publish(LogRecord), setFormatter(Formatter), setLevel(Level)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/Level.html">Level</a></dt>
  <dd style='margin-bottom: 0.5em;'>ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING</dd>
  <dd>parse(String), getName(), intValue(), toString()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/LogManager.html">LogManager</a></dt>
  <dd>getLogManager(), addLogger(Logger), getLogger(String), getLoggerNames()</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/LogRecord.html">LogRecord</a></dt>
  <dd>LogRecord(Level, String), getLevel(), getLoggerName(), getMessage(), getMillis(), getThrown(), setLevel(Level), setLoggerName(String), setMessage(String), setMillis(long), setThrown(Throwable)</dd>

  <dt><a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html">Logger</a></dt>
  <dd style='margin-bottom: 0.5em;'>GLOBAL_LOGGER_NAME</dd>
  <dd>getGlobal(), getLogger(String), addHandler(Handler), config(String), fine(String), finer(String), finest(String), getHandlers(), getLevel(), getName(), getParent(), getUseParentHandlers(), info(String), isLoggable(Level), log(Level, String), log(Level, String, Throwable), log(LogRecord), removeHandler(Handler), setLevel(Level), setParent(Logger), setUseParentHandlers(boolean), severe(String), warning(String)</dd>
</dl>

