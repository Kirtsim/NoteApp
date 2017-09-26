package fm.kirtsim.kharos.noteapp.ui.notelist.manager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListActionBarViewMvc;

/**
 * Created by kharos on 08/09/2017
 */

public class NotesListActionbarManager {

    private final NotesListActionBarViewMvc actionBarViewMvc;

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

    public Bundle getActionBarState() {
        return actionBarViewMvc.getState();
    }

    public void initializeActionBarFromSavedState(Bundle savedState) {
        actionBarViewMvc.initializeFromSavedState(savedState);
    }
}
