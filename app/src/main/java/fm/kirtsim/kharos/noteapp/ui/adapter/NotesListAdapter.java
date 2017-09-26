package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.view.LayoutInflater;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator.AdapterNotesListCoordinator;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.NotesListViewHolder;

/**
 * Created by kharos on 07/08/2017
 */

public abstract class NotesListAdapter extends
        BaseListAdapter<NotesListAdapter.NotesListAdapterListener, NotesListViewHolder>
        implements NotesListViewHolder.NotesListViewHolderListener{

    @SuppressWarnings("WeakerAccess")
    protected AdapterNotesListCoordinator notesCoordinator;

    public interface NotesListAdapterListener {
        void onNoteItemSingleClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemLongClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView);

        void onNoteItemPositionChanged(Note note, int posFrom, int posTo);
    }

    public void setNotesCoordinator(AdapterNotesListCoordinator coordinator) {
        notesCoordinator = coordinator;
    }

    NotesListAdapter(LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    public abstract void notifyNoteChanged(Note note);

    public abstract void notifyNoteRemoved(Note note);

    public AdapterNotesListCoordinator getNotesCoordinator() {
        return notesCoordinator;
    }
}
