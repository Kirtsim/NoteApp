package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import dagger.Subcomponent;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListFragment;

/**
 * Created by kharos on 27/07/2017
 */

@Subcomponent(modules={ControllerModule.class, ViewMvcModule.class})
public interface ControllerComponent {

    public void inject(BaseActivity baseActivity);

    public void inject(BaseFragment baseActivity);

    public void inject(NotesListFragment notesListFragment);

    public void inject(NoteDetailFragment noteDetailFragment);
}
