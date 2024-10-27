Delayed
===

Do you need to do any of the following?

*   schedule an activity for some time in the future
*   periodically query the server or update the interface
*   queue up work to do that must wait for other initialization to finish
*   perform a large amount of computation

GWT provides three classes that you can use to defer running code until a later point in time: Timer, DeferredCommand, and IncrementalCommand.

1.  [Scheduling work: the Timer class](#timer)
    *   [Creating Timeout Logic](#timeout)
    *   [Periodically Running Logic](#running)
2.  [Deferring some logic into the immediate future: the DeferredCommand class](#deferred)
3.  [Avoiding Slow Script Warnings: the IncrementalCommand class](#incremental)

## Scheduling work: the Timer class<a id="timer"></a>

Use the [Timer](/javadoc/latest/com/google/gwt/user/client/Timer.html) class to schedule work to be done in
the future.

To create a timer, create a new instance of the Timer class and then override the run() method entry point.

```java
Timer timer = new Timer() {
      public void run() {
        Window.alert ("Timer expired!");
      }
    };

    // Execute the timer to expire 2 seconds in the future
    timer.schedule(2000);
```

Notice that the timer will not have a chance to execute the run() method until after control returns to the JavaScript event loop.

### Creating Timeout Logic<a id="timeout"></a>

One typical use for a timer is to timeout a long running command. There are a few rules of thumb to remember in this situation:

*   Store the timer in an instance variable.
*   Always check to see that the timer is not currently running before starting a new one. (Check the instance variable to see that it is null.)
*   Remember to cancel the timer when the command completes successfully.
*   Always set the instance variable to null when the command completes or the timer expires.

Below is a an example of using a timeout with a [Remote Procedure Call](DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls) (RPC).

```java
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Foo {

  // A keeper of the timer instance in case we need to cancel it
  private Timer timeoutTimer = null;

  // An indicator when the computation should quit
  private boolean abortFlag = false;

  static final int TIMEOUT = 30; // 30 second timeout

  void startWork () {

    // ...

    // Check to make sure the timer isn't already running.
    if (timeoutTimer != null) {
        Window.alert("Command is already running!");
        return;
    }

    // Create a timer to abort if the RPC takes too long
    timeoutTimer = new Timer() {
      public void run() {
        Window.alert("Timeout expired.");
        timeoutTimer = null;
        abortFlag = true;
      }
    };

    // (re)Initialize the abort flag and start the timer.
    abortFlag = false;
    timeoutTimer.schedule(TIMEOUT * 1000); // timeout is in milliseconds

    // Kick off an RPC
    myService.myRpcMethod(arg, new AsyncCallback() {

      public void onFailure(Throwable caught) {
         Window.alert("RPC Failed:" + caught);
         cancelTimer();
      }

      public void onSuccess(Object result) {
         cancelTimer();
         if (abortFlag) {
           // Timeout already occurred. discard result
           return;
         }
         Window.alert ("RPC returned: "+ (String)result);
      }
    }
  }

  // Stop the timeout timer if it is running
  private void cancelTimer() {
    if (timeoutTimer != null) {
       timeoutTimer.cancel();
       timeoutTimer = null;
    }
  }
}
```

### Periodically Running Logic<a id="running"></a>

In order to keep a user interface up to date, you sometimes want to perform an update periodically. You might want to run a poll to the server to check for new data, or update
some sort of animation on the screen. In this case, use the Timer class
[scheduleRepeating()](/javadoc/latest/com/google/gwt/user/client/Timer.html#scheduleRepeating-int-) method:

```java
public class Foo {

  // A timer to update the elapsed time count
  private Timer elapsedTimer;
  private Label elapsedLabel = new Label();
  private long startTime;

  public Foo () {

    // ... Add elapsedLabel to a Panel ...

    // Create a new timer
    elapsedTimer = new Timer () {
      public void run() {
        showElapsed();
      }
    };

    startTime = System.currentTimeMillis();

    // Schedule the timer for every 1/2 second (500 milliseconds)
    elapsedTimer.scheduleRepeating(500);

    // ... The elapsed timer has started ...
  }

  /**
   * Show the current elapsed time in the elapsedLabel widget.
   */
  private void showElapsed () {
    double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
    NumberFormat n = NumberFormat.getFormat("#,##0.000");
    elapsedLabel.setText("Elapsed: " + n.format(elapsedTime));
  }
}
```

## Deferring some logic into the immediate future: the Scheduler class<a id="deferred"></a>

Sometimes you want to break up your logic loop so that the JavaScript event
loop gets a chance to run between two pieces of code. The [Scheduler](/javadoc/latest/com/google/gwt/core/client/Scheduler.html) class will allow you to do that.
The logic that you pass to `Scheduler` will run at some point in the future, after control has been returned to the JavaScript event loop. This little delay may give the
interface a chance to process some user events or initialize other code. To use the `Scheduler` class in its simplest form, you create a subclass of the [Command](/javadoc/latest/com/google/gwt/user/client/Command.html) class, overriding the execute() method and pass
it to [Scheduler.scheduleDeferred](/javadoc/latest/com/google/gwt/core/client/Scheduler.html#scheduleDeferred-com.google.gwt.core.client.Scheduler.ScheduledCommand-)

```java
TextBox dataEntry;

  // Set the focus on the widget after setup completes.
  Scheduler.get().scheduleDeferred(new Command() {
    public void execute () {
      dataEntry.setFocus();
    }
  });

  dataEntry = new TextBox();
```

## Avoiding Slow Script Warnings: the IncrementalCommand class<a id="incremental"></a>

AJAX developers need to be aware of keeping the browser responsive to the user. When JavaScript code is running, user interface components like buttons and text areas will not
respond to user input. If the browser were to allow this to continue, the user might think the browser is "hung" and be tempted to restart it. But browsers have a built-in defense
mechanism, the _unresponsive script warning_.

![img](images/UnresponsiveScriptDialog.png)

Any script that runs without returning control to the JavaScript main event loop for more than 10 seconds or so runs the risk of having the browser popup this dialog to the
user. The dialog is there because a poorly written script might have an infinite loop or some other bug that is keeping the browser from responding. But in AJAX applications, the
script may be doing legitimate work.

GWT provides an [IncrementalCommand](/javadoc/latest/com/google/gwt/user/client/IncrementalCommand.html)
class that helps perform long running calculations. It works by repeatedly calling an 'execute()' entry point until the computation is complete.

The following example is an outline of how to use the IncrementalCommand class to do some computation in a way that allows the browser's user interface to be responsive:

```java
public class IncrementalCommandTest implements EntryPoint {

  // Number of times doWork() is called
  static final int MAX_LOOPS = 10000;

  // Tight inner loop in doWork()
  static final int WORK_LOOP_COUNT = 50;

  // Number of times doWork() is called in IncrementalCommand before
  // returning control to the event loop
  static final int WORK_CHUNK = 100;

  // A button to kick off the computation
  Button button;

  public void onModuleLoad() {
    button = new Button("Start Computation");

    button.addClickHandler(new ClickHandler () {
      public void onClick(ClickEvent event) {
       doWorkIncremental();
      }
    }
  }

  /**
   * Create a IncrementalCommand instance that gets called back every so often
   * until all the work it has to do is complete.
   */
  private void doWorkIncremental () {

    // Turn off the button so it won't start processing again.
    button.setEnabled(false);

    IncrementalCommand ic = new IncrementalCommand(){
      int counter = 0;

      public boolean execute() {
        for (int i=0;i<WORK_CHUNK;i++) {
          counter++;

          result += doWork();

          // If we have done all the work, exit with a 'false'
          // return value to terminate further execution.
          if (counter == MAX_LOOPS) {

            // Re-enable button
            button.setEnabled(true);

            // ... other end of computation processing ...

            return false;
          }
        }
        // Call the execute function again.
        return true;
      }
    };

    // Schedule the IncrementalCommand instance to run when
    // control returns to the event loop by returning 'true'
    Scheduler.get().scheduleIncremental(ic);
  }

  /**
   * Routine that keeps the CPU busy for a while.
   * @return an integer result of the calculation
   */
  private int doWork() {
    int result;

    // ... computation...

    return result;
  }
```

