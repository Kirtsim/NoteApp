package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;

import fm.kirtsim.kharos.noteapp.ui.base.ObservableViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

interface NotesListViewMvc extends
        ObservableViewMvc<NotesListViewMvc.NotesListViewMvcListener>
{
    interface NotesListViewMvcListener {
        void onNewNoteRequested();
    }

    @MainThread void showAddButton();

    @MainThread void hideAddButton();

    void addNoteItemDecoration(NotesListItemDecoration decoration);

    @SuppressWarnings("unused")
    RecyclerView.Adapter getRecyclerViewAdapter();
}
