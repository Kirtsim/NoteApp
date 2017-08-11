package fm.kirtsim.kharos.noteapp.dependencyinjection.application;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.db.NoteDbHelper;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.threading.BackgroundThreadPoster;
import fm.kirtsim.kharos.noteapp.threading.MainThreadPoster;

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

    @Singleton
    @Provides
    public NotesManager notesManager(NoteDbHelper noteDbHelper) {
        return new NotesManager(noteDbHelper);
    }

    @Singleton
    @Provides
    public MainThreadPoster mainThreadPoster() {
        return new MainThreadPoster();
    }

    @Singleton
    @Provides
    public BackgroundThreadPoster backgroundThreadPoster() {
        return new BackgroundThreadPoster();
    }
}
