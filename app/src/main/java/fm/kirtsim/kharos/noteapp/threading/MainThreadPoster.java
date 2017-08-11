package fm.kirtsim.kharos.noteapp.threading;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by kharos on 11/08/2017
 */

public class MainThreadPoster {

    private Handler handler;

    public MainThreadPoster() {
        handler = getMainHandler();
    }

    protected Handler getMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public void post(Runnable task) {
        handler.post(task);
    }
}
