package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.ui.adapter.ColorPickerAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.ColorPickerAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.adapter.itemTouchHelper.NotesListItemTouchHelper;
import fm.kirtsim.kharos.noteapp.ui.adapter.touchCallback.NotesListItemTouchCallback;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailActionBarViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailActionBarViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListActionBarViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListActionBarViewMvcImpl;

/**
 * Created by kharos on 27/07/2017
 */

@SuppressWarnings("WeakerAccess")
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
    public ColorPickerAdapter colorPickerAdapter() {
        return new ColorPickerAdapterImpl(activity.getLayoutInflater());
    }

    @Provides
    public NotesListActionBarViewMvc notesListActionBarViewMvc() {
        return new NotesListActionBarViewMvcImpl(activity.getSupportActionBar());
    }

    @Provides
    public NoteDetailActionBarViewMvc noteDetailActionBarViewMvc() {
        return new NoteDetailActionBarViewMvcImpl(activity.getSupportActionBar());
    }
}
