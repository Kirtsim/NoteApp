package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;

/**
 * Created by kharos on 27/07/2017
 */

@Module
public class ControllerModule {

    private final BaseActivity activity;

    public ControllerModule(BaseActivity activity) {
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
}
