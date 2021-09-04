package fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public class NotesListActionBarViewMvcImpl extends BaseActionBarViewMvc implements NotesListActionBarViewMvc {

    private static final String ARG_DELETE_MI_VISIBLE = "notesList.DELETE_MI_VISIBLE";
    private static final String ARG_SELECT_ALL_MI_VISIBLE = "notesList.SELECT_ALL_MI_VISIBLE";
    private static final String ARG_COLOR_PALETTE_MI_VISIBLE = "notesList.COLOR_PALETTE_MI_VISIBLE";
    private static final String ARG_REORDER_MI_VISIBLE = "notesList.REORDER_MI_VISIBLE";
    private static final String ARG_TITLE = "notesList.ACTIONBAR_TITLE";
    private static final String ARG_BACK_BUTTON_VISIBLE = "notesList.BACK_BUTTON_VISIBLE";


    private MenuItem deleteMI;
    private MenuItem selectAllMI;
    private MenuItem colorPaletteMI;
    private MenuItem reorderMI;

    public NotesListActionBarViewMvcImpl(ActionBar actionBar) {
        super(actionBar);
    }

    /* **********************************************************
     *                NotesListActionBarViewMvc methods
     * **********************************************************/

    @Override
    public void setSelectAllMenuItemVisible(boolean visible) {
        selectAllMI.setVisible(visible);
    }

    @Override
    public void setDeleteMenuItemVisible(boolean visible) {
        deleteMI.setVisible(visible);
    }

    @Override
    public void setColorPaletteItemVisible(boolean visible) {
        colorPaletteMI.setVisible(visible);
    }

    @Override
    public void setReorderItemVisible(boolean visible) {
        reorderMI.setVisible(visible);
    }

    @Override
    public void getState(Bundle bundle) {
        String title = getTitle();
        bundle.putString(ARG_TITLE, title);
        bundle.putBoolean(ARG_BACK_BUTTON_VISIBLE, isBackButtonVisible());
        bundle.putBoolean(ARG_COLOR_PALETTE_MI_VISIBLE, colorPaletteMI.isVisible());
        bundle.putBoolean(ARG_DELETE_MI_VISIBLE, deleteMI.isVisible());
        bundle.putBoolean(ARG_REORDER_MI_VISIBLE, reorderMI.isVisible());
        bundle.putBoolean(ARG_SELECT_ALL_MI_VISIBLE, selectAllMI.isVisible());
    }

    @Override
    public void initializeFromSavedState(Bundle savedState) {
        if (savedState != null) {
            deleteMI.setVisible(savedState.getBoolean(ARG_DELETE_MI_VISIBLE));
            selectAllMI.setVisible(savedState.getBoolean(ARG_SELECT_ALL_MI_VISIBLE));
            colorPaletteMI.setVisible(savedState.getBoolean(ARG_COLOR_PALETTE_MI_VISIBLE));
            reorderMI.setVisible(savedState.getBoolean(ARG_REORDER_MI_VISIBLE));
            setHomeButtonVisible(savedState.getBoolean(ARG_BACK_BUTTON_VISIBLE));
            setTitle(savedState.getString(ARG_TITLE, ""));
        }
    }

    /* **********************************************************
     *                ActionBarViewMvc methods
     * **********************************************************/


    @Override
    public void setMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_notes_list, menu);
        setMenu(menu);

        initializeMenuItems();
        deleteMI.setVisible(false);
    }

    private void initializeMenuItems() {
        deleteMI = menu.findItem(R.id.mi_delete);
        selectAllMI = menu.findItem(R.id.mi_select_all);
        colorPaletteMI = menu.findItem(R.id.mi_color_picker);
        reorderMI = menu.findItem(R.id.mi_reorder);
    }

    @Override
    public void hideAllIcons() {
        deleteMI.setVisible(false);
        selectAllMI.setVisible(false);
        reorderMI.setVisible(false);
        colorPaletteMI.setVisible(false);
    }
}
