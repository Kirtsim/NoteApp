package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public interface NotesListViewMvc extends ViewMvc
{
    interface NotesListViewMvcListener {
    }

    RecyclerView.Adapter getRecyclerViewAdapter();
}
