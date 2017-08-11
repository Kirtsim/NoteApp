package fm.kirtsim.kharos.noteapp.ui.notelist;

import fm.kirtsim.kharos.noteapp.ui.main.ActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public interface NotesListActionBarViewMvc extends ActionBarViewMvc {

    void hideDeleteMenuItem();
    void showDeleteMenuItem();
}
