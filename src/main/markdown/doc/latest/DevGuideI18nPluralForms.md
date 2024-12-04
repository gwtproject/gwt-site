i18n Plural Forms
===

1.  [Overview](#PluralOverview)
2.  [Example](#PluralExample)
3.  [Exact Values](#ExactValues)
4.  [Offsets](#Offsets)
5.  [Lists](#Lists)

## Overview<a id="PluralOverview"></a>

Most languages alter the form of a word being counted based on the count.
For example, in English:

```text
You have 1 tree.
You have 2 trees.
```

Other languages have different rules:

*   In French, the singular form is used for 0 as well as 1
*   Arabic has 5 special plural forms in addition to the default
*   Some languages don't have plural forms at all

GWT provides a way to choose different messages based on the count of
something at runtime, using the [`Messages`](DevGuideI18nMessages.html) interface, and
 provides plural rules for hundreds of languages by default.

## Example<a id="PluralExample"></a>

First, an example `Messages` interface:

```java
@DefaultLocale("en") // not required since this is the default
public interface MyMessages extends Messages {
  @DefaultMessage("There are {0,number} items in your cart.")
  @AlternateMessage({"one", "There is 1 item in your cart."})
  String cartItems(@PluralCount int itemCount);
}
```

Note that the parameter which controls which plural form is used is marked
with the [`@PluralCount`](/javadoc/latest/com/google/gwt/i18n/client/Messages.PluralCount.html)
annotation, and that the plural forms for the default language (`en`
unless specified with [`@DefaultLocale`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.DefaultLocale.html)
are defined in the [`@AlternateMessage`](/javadoc/latest/com/google/gwt/i18n/client/Messages.AlternateMessage.html)
annotation.  If your default language is not English, you may have a different
set of plural forms here.

Let's assume you have [added](DevGuideI18nLocale.html#LocaleModule) the `en`,
`fr` and `ar` locales to your module.  Now you need translations
for each of these locales (except `en`, which will be picked up from the
annotations).  _Note: I am using English in these "translations" for
 clarity &mdash; you would actually want to use real translations._

`MyMessages_fr.properties`

```properties
cartItems=There are {0,number} items in your cart.
cartItems[one]=There is {0,number} item in your cart.
```

Note that the `"one"` plural form in French is used for
both 0 and 1, so you can't hard-code the count in the string like you can
for English.

`MyMessages_ar.properties`

```properties
cartItems=There are {0,number} items in your cart.
cartItems[none]=There are no items in your cart.
cartItems[one]=There is one item in your cart.
cartItems[two]=There are two items in your cart.
cartItems[few]=There are {0,number} items in your cart, which are few.
cartItems[many]=There are {0,number} items in your cart, which are many.
```

The Arabic plural rules that GWT uses are:

*   none - the count is 0
*   one - the count is 1
*   two - the count is 2
*   few - the last two digits are from 03-10
*   many - the last two digits are from 11-99
*   The default form is used for everything else, ie. 101, 202, etc.

The standards for how to represent plural forms in translations is still
a work in progress.  Properties files don't have any particular support, so
we invented the `[_plural_form_]` syntax to specify them.
Hopefully this will improve over time, and we can support more standard
approaches to getting translated messages with plural forms back into GWT.

## Exact Values<a id="ExactValues"></a>

Sometimes you need to provide special messages, even if the grammar of the
language doesn't require it.  For example, it is generally better to say
something like `"You have no messages"` rather than `"You have 0
messages"`.  You can specify that using a plural form `"=N"`, such
as:

```java
public interface MyMessages extends Messages {
  @DefaultMessage("There are {0,number} items in your cart.")
  @AlternateMessage({
      "one", "There is 1 item in your cart.",
      "=0", "Your cart is empty."
  })
  String cartItems(@PluralCount int itemCount);
}
```

and the properties file entry would look like:

```properties
cartItems[\=0]=Your cart is empty.
```

Note the escaping of the equals sign, since that separates the key from the
value in a properties file.

See the next item for another use of Exact Values.

## Offsets<a id="Offsets"></a>

In some cases, you may want to alter the count before applying the plural
rules to it.  For example, if you are saying `"Bob, Joe, and 3 others ate
pizza"`, you probably have a list of 5 people.  You could specifically
code subtracting that and choosing different messages based on the number of
people, but it is much easier and likely to get better translations by keeping
all the different messages together.  You can do it like this:

```java
public interface MyMessages extends Messages {
  @DefaultMessage("{1}, {2} and {0} others are here.")
  @AlternateMessage({
      "=0", "Nobody is here.",
      "=1", "{1} is  here.",
      "=2", "{1} and {2} are here.",
      "one", "{1}, {2}, and one other are here."
  })
  String peopleHere(@PluralCount @Offset(2) String[] names, String name1,
      String name2);
}

...

String[] names;
alert(peopleHere(names, names.length > 0 ? names[0] : null,
    names.length > 1 ? names[1] : null));
```

Note that you can pass in an array for a `@PluralCount` parameter &mdash;
its length is used for the count (`java.util.List` implementations work
similarly).  The `@Offset` annotation indicates that the supplied offset
should be applied before looking up the correct plural rule.  However, note
that exact value matches are compared before the offset is applied.  So, when
the count is 0, `"Nobody is here"` is chosen; if the count is 3,
`"{1}, {2}, and one other are here"` is chosen because 2 is subtracted
from the count before looking up the plural form to use.

BTW, we know it is somewhat klunky to have to pass in the names this way.
In the future, we will add a way of referencing elements in the list/array
from the placeholders, where you could simply call `peopleHere(names)`.

## Lists<a id="Lists"></a>

This is slightly off-topic for plurals, but it is related.  GWT supports
formatting lists, using the locale-appropriate separators.  For example:

```java
public interface MyMessages extends Messages {
  @DefaultMessage("Orders {0,list,number} are ready for pickup.")
  @AlternateMessage({
      "=0", "No orders are ready for pickup.",
      "one", "Order {0,list,number} is ready for pickup."
  })
  String ordersReady(@PluralCount List<Integer> orders);
}
```

The format specifier `{0,list,number}` says that argument 0 is to be
formatted as a list, with each element formatted as a number.  The same format
options are available as if it weren't an element in a list, so
`{0,list,number:curcode=USD,currency}` would work too.  As before,
either arrays or `java.util.List` instances work fine, and the
requirements of types for formatting remain the same as if it weren't a list.

In English, the results would be:

*   `ordersReady(Arrays.asList())` => `"No orders are ready for pickup."`
*   `ordersReady(Arrays.asList(14)})` => `"Order 14 is ready for pickup."`
*   `ordersReady(Arrays.asList(14, 17))` => `"Orders 14 and 17 are ready for pickup."`
*   `ordersReady(Arrays.asList(14, 17, 21))` => `"Orders 14, 17, and 21 are ready for pickup."`

Note that GWT only knows about the default list separators used for a
language, and that while you might want to say something like `"a, b, or
c"`, there is currently no way to express that.
