package fm.kirtsim.kharos.noteapp.ui.notelist;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public interface NotesListViewMvc extends ViewMvc
{
    interface NotesListViewMvcListener {
        void onNoteSingleClicked(Note note);

        void onNoteLongClicked(Note note);
    }

//    void setRecyclerViewAdapter()
}
