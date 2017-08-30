package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.NotesListViewHolder;
import fm.kirtsim.kharos.noteapp.utils.ListUtils;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListAdapterImpl extends NotesListAdapter {

    private List<Note> notes;
    private final SparseIntArray noteIdsMappingToIndexes;

    public NotesListAdapterImpl(LayoutInflater layoutInflater) {
        super(layoutInflater);
        setHasStableIds(true);
        notes = Lists.newArrayList();
        noteIdsMappingToIndexes = new SparseIntArray(1);
    }


    /* ****************************************************
     *            Adapter Inherited  methods
     * ****************************************************/
    @Override
    public NotesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotesListItemViewMvc mvcView = new NotesListItemViewMvcImpl(layoutInflater, parent);
        NotesListViewHolder viewHolder = new NotesListViewHolder(mvcView);
        addViewHolder(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int position) {
        final Note note = notes.get(position);
        final NotesListItemViewMvc listItemView = holder.getItemViewMvc();
        if (listener != null) {
            listener.onNoteItemVisible(note, listItemView);
        }
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    /* ****************************************************
     *            BaseAdapter Inherited  methods
     * ****************************************************/

    @Override
    protected void registerTouchListener(NotesListViewHolder viewHolder) {
        viewHolder.setListener(this);
    }

    /* ********************************************************
     *            NotesListViewHolderListener methods
     * ********************************************************/
    @Override
    public void onNoteSingleClicked(int position, NotesListItemViewMvc noteItemView) {
        final Note clickedNote = notes.get(position);
        if (listener != null) {
            listener.onNoteItemSingleClicked(clickedNote, noteItemView, position);
        }
    }

    @Override
    public void onNoteLongClicked(int position, NotesListItemViewMvc noteItemView) {
        final Note clickedNote = notes.get(position);
        if (listener != null) {
            listener.onNoteItemLongClicked(clickedNote, noteItemView, position);
        }
    }

    // ************** NotesListAdapter ***************************

    @Override
    public void setNewNotesList(List<Note> newNotes) {
        notes.clear();
        addNotes(newNotes);
    }

    @Override
    public void replaceNotesStartingFrom(List<Note> newNotes, int from) {
        int index = from;
        int currentSize = notes.size();
        for (Note note : newNotes) {
            if (index < currentSize) {
                notes.set(index, note);
                index++;
            } else {
                notes.add(note);
            }
        }
    }

    @Override
    public List<Note> getListOfAllNotes() {
        return Lists.newArrayList(notes);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean updateNote(Note old, Note new_) {
        if (old == null || new_ == null) return false;
        final int index = noteIdsMappingToIndexes.get(old.getId());
        if (index != -1) {
            notes.set(index, new_);
            if (old.getId() != new_.getId()) {
                noteIdsMappingToIndexes.delete(old.getId());
                noteIdsMappingToIndexes.put(new_.getId(), index);
            }
            return true;
        }
        return false;
    }

    @Override
    public Note getNoteWithIdOrDefault(int id, Note _default) {
        final int index = noteIdsMappingToIndexes.get(id);
        if (index != -1)
            return notes.get(index);
        return _default;
    }

    @Override
    public void notifyNoteChanged(Note note) {
        final int index = noteIdsMappingToIndexes.get(note.getId());
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    @Override
    public void notifyNoteRemoved(Note note) {
        final int index = noteIdsMappingToIndexes.get(note.getId());
        if (index != -1) {
            notifyItemRemoved(index);
        }
    }

    @Override
    public boolean onItemMove(int posFrom, int posTo) {
        int lower = Math.min(posFrom, posTo);
        int higher = Math.max(posFrom, posTo);
        while (lower < higher) {
            Collections.swap(notes, lower, lower + 1);
            lower++;
        }
        notifyItemMoved(posFrom, posTo);
        return true;
    }

    @Override
    public void dragFinished(int startedFrom, int finishedAt) {
        listener.onNoteItemPositionChanged(notes.get(finishedAt), startedFrom, finishedAt);
    }

    @Override
    public void setAnimateDragAndDrop(boolean animate) {
    }
}
