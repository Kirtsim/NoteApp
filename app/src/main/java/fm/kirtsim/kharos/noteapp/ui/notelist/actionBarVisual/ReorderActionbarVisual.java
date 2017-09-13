package fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual;

import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListActionBarViewMvc;

/**
 * Created by kharos on 08/09/2017
 */

public class ReorderActionbarVisual implements ActionbarVisual {

    @Override
    public void accept(NotesListActionBarViewMvc actionBarViewMvc) {
        actionBarViewMvc.setHomeButtonVisible(true);
        actionBarViewMvc.hideAllIcons();
        actionBarViewMvc.setHomeButtonVisible(true);
    }
}
