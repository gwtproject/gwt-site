
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
If you have a web server (Apache, IIS, etc.) installed locally and PHP installed, you can write a PHP script to generate stock data and make the call to your local server. What's important for this example is that the stock data is JSON-encoded and that the server is local.
</p>

<ol class="instructions">
    <li>
        <div class="header">Create a PHP script.</div>
        <div class="details">In the Eclipse Package Explorer, select the <code>StockWatcher/war</code> folder.</div>
                <div class="details">From the Eclipse menu bar, select File &gt; New &gt; File.</div>
                                <div class="details">In the New File window, enter the file name <code>stockPrices.php</code></div>


        <div class="details"><pre class="code">
&lt;?php

  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');

  define("MAX_PRICE", 100.0); // $100.00
  define("MAX_PRICE_CHANGE", 0.02); // +/- 2%

  echo '[';

  $q = trim($_GET['q']);
  if ($q) {
    $symbols = explode(' ', $q);

    for ($i=0; $i&lt;count($symbols); $i++) {
      $price = lcg_value() * MAX_PRICE;
      $change = $price * MAX_PRICE_CHANGE * (lcg_value() * 2.0 - 1.0);

      echo '{';
      echo "\"symbol\":\"$symbols[$i]\",";
      echo "\"price\":$price,";
      echo "\"change\":$change";
      echo '}';

      if ($i &lt; (count($symbols) - 1)) {
        echo ',';
      }
    }
  }

  echo ']';
?&gt;</pre></div>
    </li>
    <li>
        <div class="header">Compile StockWatcher.</div>
        <div class="details">Click the GWT Compile Project button in the toolbar <img src="images/GWTCompileProject.png" alt="icon"/> or run the <code>ant build</code> script to create the production mode files for the application (which will now include stockPrices.php). </div>
        <div class="details"><img src="images/JSONstockPricesphp.png" alt="screenshot: Package Explorer php file" /></div>

    </li>
    <li>
        <div class="header">Move the compiled StockWatcher files in the StockWatcher/war directory to a /StockWatcher directory in whatever web server (Apache, IIS, etc.) you have installed locally which supports PHP.  If you are not using Java servlets (e.g. GWT RPC), you will not have to move over the files in StockWatcher/war/WEB-INF</div>
    </li>
    <li>
        <div class="header">Test the stock quote server.</div>
        <div class="details">In a web browser, navigate to <code>http://localhost/StockWatcher/stockPrices.php?q=ABC+DEF</code></div>
<div class="details">StockPrice data is returned in JSON format.</code></div>
        <div class="details"><pre class="code">
[{"symbol":"ABC","price":40.485578668179,"change":-0.53944918844604},
 {"symbol":"DEF","price":1.3606576154209,"change":0.0051755221198266}]</pre></div>
    </li>
</ol>

<p>
Now that you can retrieve JSON-encoded stock data from the server, continue with the next step in the JSON tutorial, <a href="JSON.html#client">Manipulating JSON data in the client-side code.</a>
</p>


