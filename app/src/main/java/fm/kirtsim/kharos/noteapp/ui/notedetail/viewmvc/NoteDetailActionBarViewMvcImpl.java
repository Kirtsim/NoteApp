package fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActionBarViewMvc;

/**
 * Created by kharos on 17/08/2017
 */

public class NoteDetailActionBarViewMvcImpl extends BaseActionBarViewMvc implements
        NoteDetailActionBarViewMvc {

    private static final String ARG_DELETE_MI_VISIBLE = "noteDetail.DELETE_MI_VISIBLE";
    private static final String ARG_SAVE_MI_VISIBLE = "noteDetail.SAVE_MI_VISIBLE";
    private static final String ARG_BACK_BUTTON_VISIBLE = "noteDetail.BACK_BTN_VISIBLE";
    private static final String ARG_TITLE = "noteDetail.TITLE";

    private MenuItem saveMenuItem;
    private MenuItem deleteMenuItem;

    public NoteDetailActionBarViewMvcImpl(ActionBar actionBar) {
        super(actionBar);
    }

    @Override
    public void setMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_note_detail, menu);
        saveMenuItem = menu.findItem(R.id.mi_save);
        deleteMenuItem = menu.findItem(R.id.mi_delete);
    }

    @Override
    public void hideAllIcons() {
        saveMenuItem.setVisible(false);
        deleteMenuItem.setVisible(false);
    }

    @Override
    public void getState(Bundle bundle) {
        bundle.putString(ARG_TITLE, getTitle());
        bundle.putBoolean(ARG_DELETE_MI_VISIBLE, deleteMenuItem.isVisible());
        bundle.putBoolean(ARG_SAVE_MI_VISIBLE, saveMenuItem.isVisible());
        bundle.putBoolean(ARG_BACK_BUTTON_VISIBLE, isBackButtonVisible());
    }

    @Override
    public void initializeFromSavedState(Bundle savedState) {
        if (savedState != null) {
            setTitle(savedState.getString(ARG_TITLE, ""));
            setHomeButtonVisible(savedState.getBoolean(ARG_BACK_BUTTON_VISIBLE));
            saveMenuItem.setVisible(savedState.getBoolean(ARG_SAVE_MI_VISIBLE));
            deleteMenuItem.setVisible(savedState.getBoolean(ARG_DELETE_MI_VISIBLE));
        }
    }
}
