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

    public NotesListActionBarViewMvcImpl(ActionBar actionBar) {
        super(actionBar);
    }

    /* **********************************************************
     *                NotesListActionBarViewMvc methods
     * **********************************************************/

    @Override
    public void hideDeleteMenuItem() {
        deleteMI.setVisible(false);
    }

    @Override
    public void showDeleteMenuItem() {
        deleteMI.setVisible(true);
    }

    /* **********************************************************
     *                ActionBarViewMvc methods
     * **********************************************************/


    @Override
    public void setMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_save_delete, menu);
        setMenu(menu);

        initializeMenuItems();
        removeRedundantMenuItems();
        deleteMI.setVisible(false);
    }

    private void initializeMenuItems() {
        deleteMI = menu.findItem(R.id.delete);
    }

    private void removeRedundantMenuItems() {
        menu.removeItem(R.id.save);
    }
}
