package fm.kirtsim.kharos.noteapp.ui.notedetail;

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

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private MenuItem saveMenuItem;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
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
}
