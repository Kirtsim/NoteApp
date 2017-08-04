package fm.kirtsim.kharos.noteapp.ui.notelist;

import fm.kirtsim.kharos.noteapp.ui.main.MenuViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public interface NotesListMenuViewMvc extends MenuViewMvc {

    void hideDeleteMenuItem();
    void showDeleteMenuItem();
}
