package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kharos on 27/07/2017
 */

@Module
public class ControllerModule {

    private final Activity activity;

    public ControllerModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    public Context getContext() {
        return activity;
    }
}
