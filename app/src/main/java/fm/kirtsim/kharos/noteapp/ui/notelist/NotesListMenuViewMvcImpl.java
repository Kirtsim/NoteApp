package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseMenuViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public class NotesListMenuViewMvcImpl extends BaseMenuViewMvc implements NotesListMenuViewMvc{

    private MenuItem deleteMI;

    public NotesListMenuViewMvcImpl(Menu menu, MenuInflater menuInflater) {
        setMenu(menu);
        menuInflater.inflate(R.menu.menu_save_delete, menu);
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

    /* **********************************************************
     *                NotesListMenuViewMvc methods
     * **********************************************************/

    @Override
    public void hideDeleteMenuItem() {
        deleteMI.setVisible(false);
    }

    @Override
    public void showDeleteMenuItem() {
        deleteMI.setVisible(true);
    }
}
