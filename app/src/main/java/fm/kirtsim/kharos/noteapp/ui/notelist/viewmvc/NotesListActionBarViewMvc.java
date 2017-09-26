package fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc;

import fm.kirtsim.kharos.noteapp.ui.main.viewmvc.ActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public interface NotesListActionBarViewMvc extends ActionBarViewMvc {

    void setSelectAllMenuItemVisible(boolean visible);

    void setDeleteMenuItemVisible(boolean visible);

    void setColorPaletteItemVisible(boolean visible);

    void setReorderItemVisible(boolean visible);
}
