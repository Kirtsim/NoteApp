package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.v7.widget.RecyclerView;

import fm.kirtsim.kharos.noteapp.ui.base.ObservableViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public interface NotesListViewMvc extends
        ObservableViewMvc<NotesListViewMvc.NotesListViewMvcListener>
{
    interface NotesListViewMvcListener {
        void onNewNoteRequested();
    }

    @SuppressWarnings("unused")
    RecyclerView.Adapter getRecyclerViewAdapter();
}
