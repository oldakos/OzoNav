import javafx.application.Platform;

import java.util.TimerTask;

/**
 *  Used instead of a normal TimerTask to automatically execute the desired operation on the JavaFX Application thread.
 */
public class GUITimerTask extends TimerTask {

    private Runnable r;

    /**
     * Create a TimerTask that will be run by the JavaFX Application thread to ensure proper GUI updates.
     * @param runnable A Runnable doing what you would normally put in a TimerTask's `run()` method.
     */
    public GUITimerTask(Runnable runnable){
        r = runnable;
    }

    @Override
    public void run() {
        Platform.runLater(r);
    }
}
