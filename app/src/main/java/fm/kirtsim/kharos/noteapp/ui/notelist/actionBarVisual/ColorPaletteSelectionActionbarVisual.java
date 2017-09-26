package fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual;

import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListActionBarViewMvc;

/**
 * Created by kharos on 08/09/2017
 */

public class ColorPaletteSelectionActionbarVisual implements ActionbarVisual {

    @Override
    public void accept(NotesListActionBarViewMvc actionBarViewMvc) {
        actionBarViewMvc.hideAllIcons();
        actionBarViewMvc.setSelectAllMenuItemVisible(true);
        actionBarViewMvc.setHomeButtonVisible(true);
    }
}
