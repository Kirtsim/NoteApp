package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public class NotesListActionBarViewMvcImpl extends BaseActionBarViewMvc implements NotesListActionBarViewMvc {

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
}
