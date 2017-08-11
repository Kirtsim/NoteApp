package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListActionBarViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListActionBarViewMvcImpl;

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
        return new NotesListAdapterImpl(activity.getLayoutInflater());
    }

    @Provides
    public NotesListActionBarViewMvc notesListMenuViewMvc() {
        return new NotesListActionBarViewMvcImpl(activity.getSupportActionBar());
    }
}
