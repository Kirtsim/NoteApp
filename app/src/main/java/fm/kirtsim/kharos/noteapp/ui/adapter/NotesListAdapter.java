package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

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
        Toast.makeText(layoutInflater.getContext(), "position:" + position, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onNoteLongClicked(int position) {
        final Note clickedNote = notes.get(position);
        for (NotesListAdapterListener listener : listeners) {
            listener.onNoteItemLongClicked(clickedNote);
        }
        Toast.makeText(layoutInflater.getContext(), "position:" + position, Toast.LENGTH_LONG)
                .show();
    }


    // ************** ADAPTER's METHODS ***************************

    public void setNewNotesList(List<Note> newNotes) {
        notes.clear();
        addNotes(newNotes);
    }

    public void addNotes(List<Note> newNotes) {
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    public void registerListener(NotesListAdapterListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener " +
                    NotesListAdapterListener.class.getSimpleName() + " cannot be null");
        if (listeners.size() == 0)
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
