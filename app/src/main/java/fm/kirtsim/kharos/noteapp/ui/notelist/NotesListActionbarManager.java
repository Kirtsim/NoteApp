package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.view.Menu;
import android.view.MenuInflater;

import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ActionbarVisual;

/**
 * Created by kharos on 08/09/2017
 */

public class NotesListActionbarManager {

    public static final int STATE_HOME = 0;
    public static final int STATE_COLOR_NON_SELECTION = 1;
    public static final int STATE_COLOR_SELECTION = 2;
    public static final int STATE_REORDER = 3;

    private final NotesListActionBarViewMvc actionBarViewMvc;
    private int actionbarVisualState;

    public NotesListActionbarManager(NotesListActionBarViewMvc actionBarViewMvc) {
        this.actionBarViewMvc = actionBarViewMvc;
    }

    public void inflateWithMenu(Menu menu, MenuInflater inflater) {
        actionBarViewMvc.setMenu(menu, inflater);
    }

    public void applyVisual(ActionbarVisual actionbarVisual) {
        actionbarVisual.accept(actionBarViewMvc);
    }

    public void applyVisual(ActionbarVisual actionbarVisual, String title) {
        actionbarVisual.accept(actionBarViewMvc);
        setTitle(title);
    }

    public void setTitle(String title) {
        actionBarViewMvc.setTitle(title);
    }
}
