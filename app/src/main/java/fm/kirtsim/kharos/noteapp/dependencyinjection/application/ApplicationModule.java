package fm.kirtsim.kharos.noteapp.dependencyinjection.application;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.db.NoteDbHelper;

/**
 * Created by kharos on 27/07/2017
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application getApplication() {
        return application;
    }

    @Provides
    @Singleton
    public NoteDbHelper noteDbHelper() {
        return new NoteDbHelper(application);
    }
}
