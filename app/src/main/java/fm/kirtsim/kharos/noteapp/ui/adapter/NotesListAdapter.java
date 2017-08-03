package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
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

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListAdapter extends RecyclerView.Adapter<NotesListViewHolder> implements
        NotesListViewHolder.NotesListViewHolderListener {

    public interface NotesListAdapterListener {
        void onNoteItemSingleClicked(Note note);
        void onNoteItemLongClicked(Note note);
    }

    private LayoutInflater layoutInflater;
    private List<Note> notes;
    private final List<NotesListViewHolder> viewHolders;
    private final Set<NotesListAdapterListener> listeners;

    public NotesListAdapter(LayoutInflater layoutInflater) {
        notes = new ArrayList<>();
        viewHolders = new ArrayList<>();
        listeners = new HashSet<>(1);
        this.layoutInflater = layoutInflater;
    }


    /* **********************************
     *            ViewHolder methods
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
        holder.applyDataFromNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    /* ********************************************************
     *            NotesListViewHolderListener methods
     * ********************************************************/
    @Override
    public void onNoteSingleClicked(int position) {
        final Note clickedNote = notes.get(position);
        for (NotesListAdapterListener listener : listeners) {
            listener.onNoteItemSingleClicked(clickedNote);
        }
    }

    @Override
    public void onNoteLongClicked(int position) {
        final Note clickedNote = notes.get(position);
        for (NotesListAdapterListener listener : listeners) {
            listener.onNoteItemLongClicked(clickedNote);
        }
    }


    // ************** ADAPTER's METHODS ***************************

    public void setNewNotesList(List<Note> newNotes) {
        notes.clear();
        addNotes(newNotes);
    }

    public boolean addNotes(List<Note> newNotes) {
        boolean listChanged = false;
        if (newNotes != null && !newNotes.isEmpty()) {
            listChanged = notes.addAll(newNotes);
            notifyDataSetChanged();
        }
        return listChanged;
    }

    public boolean changeNoteAt(int index, Note note) {
        boolean changed = false;
        if (note != null && index > -1 && index < notes.size()) {
            notes.set(index, note);
            notifyItemChanged(index);
            changed = true;
        }
        return changed;
    }

    public boolean deleteNote(Note note, boolean updateUi) {
        boolean removed = false;
        if (note != null) {
            removed = notes.remove(note);
            if (updateUi)
                notifyDataSetChanged();
        }
        return removed;
    }

    public boolean deleteNotes(List<Note> notes) {
        boolean removed = false;
        if (notes != null && !notes.isEmpty()) {
            removed = notes.removeAll(notes);
            notifyDataSetChanged();
        }
        return removed;
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
