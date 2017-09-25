package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Bundle;

import fm.kirtsim.kharos.noteapp.ui.main.ActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public interface NotesListActionBarViewMvc extends ActionBarViewMvc {

    void setSelectAllMenuItemVisible(boolean visible);

    void setDeleteMenuItemVisible(boolean visible);

    void setColorPaletteItemVisible(boolean visible);

    void setReorderItemVisible(boolean visible);
}
