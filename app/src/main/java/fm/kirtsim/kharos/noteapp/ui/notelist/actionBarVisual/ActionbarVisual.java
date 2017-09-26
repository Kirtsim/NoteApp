package fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual;

import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListActionBarViewMvc;

/**
 * Created by kharos on 08/09/2017
 */

@FunctionalInterface
public interface ActionbarVisual {
    void accept(NotesListActionBarViewMvc actionBarViewMvc);
}
