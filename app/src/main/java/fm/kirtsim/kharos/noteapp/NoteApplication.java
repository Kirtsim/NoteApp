package fm.kirtsim.kharos.noteapp;

import android.app.Application;

import fm.kirtsim.kharos.noteapp.dependencyinjection.application.ApplicationComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.application.ApplicationModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.application.DaggerApplicationComponent;

/**
 * Created by kharos on 27/07/2017
 */

public class NoteApplication extends Application {

    private ApplicationComponent applicationComponent;

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }
}
