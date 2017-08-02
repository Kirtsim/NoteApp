package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import dagger.Subcomponent;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListFragment;

/**
 * Created by kharos on 27/07/2017
 */

@Subcomponent(modules={ControllerModule.class, ViewMvcModule.class})
public interface ControllerComponent {

    public void inject(NotesListFragment notesListFragment);

    public void inject(NoteDetailFragment noteDetailFragment);
}
