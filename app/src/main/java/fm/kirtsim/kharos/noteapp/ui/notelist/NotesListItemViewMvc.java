package fm.kirtsim.kharos.noteapp.ui.notelist;

import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 30/07/2017
 */

public interface NotesListItemViewMvc extends ViewMvc{

    void setTitle(String title);
    void setText(String text);
}
