package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.db.NoteDbHelper;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;

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

    @Provides
    public NotesListAdapter notesListAdapter() {
        return new NotesListAdapter(activity.getLayoutInflater());
    }

    @Provides
    public NotesManager notesManager(NoteDbHelper noteDbHelper) {
        return new NotesManager(noteDbHelper);
    }
}
