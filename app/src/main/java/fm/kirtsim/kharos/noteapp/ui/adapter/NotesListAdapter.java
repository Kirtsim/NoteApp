package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.NotesListViewHolder;

/**
 * Created by kharos on 07/08/2017
 */

public abstract class NotesListAdapter extends
        BaseListAdapter<NotesListAdapter.NotesListAdapterListener, NotesListViewHolder>
        implements NotesListViewHolder.NotesListViewHolderListener{

    public interface NotesListAdapterListener {
        void onNoteItemSingleClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemLongClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView);

        void onNoteItemPositionChanged(Note note, int posFrom, int posTo);
    }

    NotesListAdapter(LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    public abstract void setNewNotesList(List<Note> newNotes);

    public abstract void replaceNotesStartingFrom(List<Note> newNotes, int from);

    public abstract List<Note> getListOfAllNotes();

    public abstract boolean addNotes(List<Note> newNotes);

    public abstract boolean removeNote(Note note);

    public abstract boolean removeNotes(List<Note> _notes);

    public abstract boolean updateNote(Note old, Note new_);

    @SuppressWarnings("unused")
    public abstract Note getNoteWithIdOrDefault(int id, Note _default);

    public abstract void notifyNoteChanged(Note note);

    public abstract void notifyNoteRemoved(Note note);

    public abstract void setAnimateDragAndDrop(boolean animate);
}
