package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.NotesListViewHolder;
import fm.kirtsim.kharos.noteapp.utils.ListUtils;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListAdapter extends RecyclerView.Adapter<NotesListViewHolder> implements
        NotesListViewHolder.NotesListViewHolderListener {

    public interface NotesListAdapterListener {
        void onNoteItemSingleClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemLongClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView);
    }

    private LayoutInflater layoutInflater;
    private List<Note> notes;
    private final SparseIntArray noteIdsMappingToIndexes;
    private final List<NotesListViewHolder> viewHolders;
    private final Set<NotesListAdapterListener> listeners;

    public NotesListAdapter(LayoutInflater layoutInflater) {
        notes = new ArrayList<>();
        noteIdsMappingToIndexes = new SparseIntArray(1);
        viewHolders = new ArrayList<>();
        listeners = new HashSet<>(1);
        this.layoutInflater = layoutInflater;
    }


    /* **********************************
     *            Inherited  methods
     * **********************************/
    @Override
    public NotesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotesListItemViewMvc mvcView = new NotesListItemViewMvcImpl(layoutInflater, parent);
        NotesListViewHolder viewHolder = new NotesListViewHolder(mvcView);
        if (!listeners.isEmpty())
            viewHolder.setListener(this);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int position) {
        final Note note = notes.get(position);
        final NotesListItemViewMvc listItemView = holder.getItemViewMvc();
        listeners.forEach(l -> l.onNoteItemVisible(note, listItemView));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    /* ********************************************************
     *            NotesListViewHolderListener methods
     * ********************************************************/
    @Override
    public void onNoteSingleClicked(int position, NotesListItemViewMvc noteItemView) {
        final Note clickedNote = notes.get(position);
        for (NotesListAdapterListener listener : listeners) {
            listener.onNoteItemSingleClicked(clickedNote, noteItemView, position);
        }
    }

    @Override
    public void onNoteLongClicked(int position, NotesListItemViewMvc noteItemView) {
        final Note clickedNote = notes.get(position);
        for (NotesListAdapterListener listener : listeners) {
            listener.onNoteItemLongClicked(clickedNote, noteItemView, position);
        }
    }

    // ************** ADAPTER's METHODS ***************************

    public void setNewNotesList(List<Note> newNotes) {
        notes.clear();
        addNotes(newNotes);
    }

    public boolean addNotes(List<Note> newNotes) {
        if (newNotes != null) {
            final int noteCount = notes.size();
            int index = noteCount;
            for (Note note : newNotes) {
                notes.add(note);
                noteIdsMappingToIndexes.put(note.getId(), index++);
            }
            return index > noteCount;
        }
        return false;
    }

    public boolean removeNote(Note note) {
        if (note != null) {
            final int id = note.getId();
            final int index = noteIdsMappingToIndexes.get(id);
            if (index != -1) {
                notes.remove(index);
                noteIdsMappingToIndexes.delete(id);
                return true;
            }
        }
        return false;
    }

    public boolean removeNotes(List<Note> _notes) {
        boolean dataChanged = false;
        if (_notes != null && !_notes.isEmpty()) {
            for (Note note : _notes) {
                final int id = note.getId();
                final int index = noteIdsMappingToIndexes.get(id);
                if (index != -1) {
                    notes.set(index, null);
                    noteIdsMappingToIndexes.delete(id);
                    dataChanged = true;
                }
            }
            ListUtils.removeNullObjects(notes);
        }
        return dataChanged;
    }

    public Note getNoteWithIdOrDefault(int id, Note _default) {
        final int index = noteIdsMappingToIndexes.get(id);
        if (index != -1)
            return notes.get(index);
        return _default;
    }

    public void notifyNoteRemoved(Note note) {
        final int index = noteIdsMappingToIndexes.get(note.getId());
        if (index != -1) {
            notifyItemRemoved(index);
        }
    }

    public void registerListener(NotesListAdapterListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener " +
                    NotesListAdapterListener.class.getSimpleName() + " cannot be null");
        if (listeners.isEmpty())
            startListeningToTouches();
        listeners.add(listener);
    }

    public void removeListener(NotesListAdapterListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("trying to remove listener that is null");
        if (listeners.size() == 1)
            stopListeningToTouches();
        listeners.remove(listener);
    }

    private void startListeningToTouches() {
        for (NotesListViewHolder viewHolder : viewHolders) {
            viewHolder.setListener(this);
        }
    }

    private void stopListeningToTouches() {
        for (NotesListViewHolder viewHolder : viewHolders) {
            viewHolder.removeListener();
        }
    }
}
