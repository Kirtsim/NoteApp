package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.kirtsim.kharos.noteapp.ui.adapter.ColorPickerAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.ColorPickerAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator.AdapterNotesListCoordinatorImpl;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;
import fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc.NoteDetailActionBarViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc.NoteDetailActionBarViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListActionBarViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListActionBarViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.notelist.manager.NotesListActionbarManager;

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
        return new NotesListAdapterImpl(activity.getLayoutInflater(),
                new AdapterNotesListCoordinatorImpl());
    }

    @Provides
    public NotesListActionBarViewMvc notesListActionBarViewMvc() {
        return new NotesListActionBarViewMvcImpl(activity.getSupportActionBar());
    }

    @Provides
    public NotesListActionbarManager notesListActionbarManager(NotesListActionBarViewMvc viewMvc) {
        return new NotesListActionbarManager(viewMvc);
    }

    @Provides
    public ColorPickerAdapter colorPickerAdapter() {
        return new ColorPickerAdapterImpl(activity.getLayoutInflater());
    }

    @Provides
    public NoteDetailActionBarViewMvc noteDetailActionBarViewMvc() {
        return new NoteDetailActionBarViewMvcImpl(activity.getSupportActionBar());
    }
}
