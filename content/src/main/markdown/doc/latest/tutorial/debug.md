Debugging
===

At this point, you've finished implementing the StockWatcher UI and all its client-side functionality. However, you've noticed that there is an error in the Change field. The percentage of change is not calculating correctly.

In this section, you'll use Eclipse to debug your Java code while running StockWatcher in development mode.

1.  [Find the bug.](#findBug)
2.  [Fix the bug.](#fixBug)
3.  [Test the bug fix by running StockWatcher in development mode.](#testFix)

#### Benefits

You can debug the Java source code before you compile it into JavaScript. This GWT development process help you take advantage of the debugging tools in your Java IDE. You can:

*   Set break points.
*   Step through the code line by line.
*   Drill down in the code.
*   Inspect the values of variables.
*   Display the stack frame for suspended threads.

One of attractions of developing in JavaScript is that you can make changes and see them immediately by refreshing the browser&mdash;without having to do a slow compilation step. GWT development mode provides the exact same development cycle. You do **not** have to recompile for every change you make; that's the whole point of development mode. Just click "Refresh" to see your updated Java code in action.

##  Finding the bug <a id="findBug"></a>

### Analyzing the problem

![screenshot StockWatcher Bug](images/CodeClientBug.png)

Looking at the values in the Price and Change fields, you can see that, for some reason, all of the change percentages are only 1/10 the size of the correct values.

The values for the Change field are loaded by the updateTable(StockPrice) method.

```java
  /**
   * Update a single row in the stock table.
   *
   * @param price Stock data for a single row.
   */
  private void updateTable(StockPrice price) {
    // Make sure the stock is still in the stock table.
    if (!stocks.contains(price.getSymbol())) {
      return;
    }
    
    int row = stocks.indexOf(price.getSymbol()) + 1;
    
    // Format the data in the Price and Change fields.
    String priceText = NumberFormat.getFormat("#,##0.00").format(
        price.getPrice());
    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
    String changeText = changeFormat.format(price.getChange());
    String changePercentText = changeFormat.format(price.getChangePercent());
    
    // Populate the Price and Change fields with new data.
    stocksFlexTable.setText(row, 1, priceText);
    stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
        + "%)");
  }
```

Just glancing at the code, you can see that the value of the changePercentText variable is being set elsewhere, in price.getChangePercent. So, first set a breakpoint on that line and then drill down to determine where the error in calculating the change percentage is.

### Setting break points

1.  Set a breakpoint on the lines of code you want to step into and where you want to examine variable values.
    *  In StockWatcher.java, in the updateTable(StockPrice price) method, set a breakpoints on these two lines

    ```java
    String changePercentText = changeFormat.format(price.getChangePercent());
    ```

    ```java
    stocksFlexTable.setText(row, 1, priceText);
    ```

    *  Run the code that has the error.
    *  To run the code in the updateTable method where you suspect the error, just add a stock to the stock list in the browser running in development mode.
    *  Execution will stop at the first breakpoint.
    *  Eclipse switches to Debug perspective.

2.  Check the values of the variables priceText and changeText.
    *  In the Eclipse Debug perspective, look at the Variables pane.

3.  Run the code to the next break point, where priceText is set.
    *  In the Debug pane, press the Resume icon.

4.  Check the values of the variables priceText, changeText, changePercentText.
    *  In the Eclipse Debug perspective, look at the Variables pane. If you like, double-check the math to see the error.
    *  ![StockWatcher variables](images/DebugVariablesBug.png)

5.  Loop back to the first break point, where changePercentText is set.
    *  In the Debug pane, press the Resume icon.

### Stepping through the code

Now step into the code to see where and how the changePercentText is being calculated.

1.  Step into the getChangePercent method to see how it's calculating the value of changePercentText.

```java
public double getChangePercent() {
  return 10.0 * this.change / this.price;
}
```

Looking at the getChangePercent method, you can see the problem: it's multiplying the change percentage by 10 instead of 100. That corresponds exactly with the output you saw before: all of the change percentages were only 1/10 the size of the correct values.

##  Fixing the bug <a id="fixBug"></a>

1.  Fix the error in calculating the percentage of the price change.
    *  In StockPrice.java, edit the getChangePercent method.

```java
public double getChangePercent() {
  return 100.0 * this.change / this.price;
}
```

**Tip:** In Eclipse, if you find it easier to edit in the Java perspective rather than the Debug perspective, you can switch back and forth while running StockWatcher in development mode.


##  Testing the bug fix in development mode <a id="testFix"></a>

At this point when you enter a stock code, the calculation of the Change field should be accurate. Try it and see.

1.  In Eclipse, toggle all the breakpoints off and press Resume.
2.  In the browser running in development mode, press Refresh.
3.  Add a stock.
4.  Check the calculation of the value in the Change field.

## What's Next

At this point you've implemented all your functional requirements. StockWatcher is running and you've found and fixed a bug.

Now you're ready to enhance StockWatcher's visual design. You'll apply CSS style rules to the GWT widgets and add a static element (a logo) to the page.

[Step 7: Applying Style](style.html)
