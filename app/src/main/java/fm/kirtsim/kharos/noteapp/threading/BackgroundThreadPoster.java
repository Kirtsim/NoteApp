package fm.kirtsim.kharos.noteapp.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kharos on 11/08/2017
 */

public class BackgroundThreadPoster {

    private ExecutorService executorService;

    public BackgroundThreadPoster() {
        executorService = Executors.newCachedThreadPool();
    }

    public void post(Runnable task) {
        executorService.execute(task);
    }
}
