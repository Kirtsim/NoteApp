package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.NotesListViewHolder;
import fm.kirtsim.kharos.noteapp.utils.ListUtils;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListAdapterImpl extends
        BaseListAdapter<NotesListAdapter.NotesListAdapterListener, NotesListViewHolder>
        implements NotesListAdapter, NotesListViewHolder.NotesListViewHolderListener {

    private List<Note> notes;
    private final SparseIntArray noteIdsMappingToIndexes;

    public NotesListAdapterImpl(LayoutInflater layoutInflater) {
        super(layoutInflater);
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
        viewHolders.add(viewHolder);
        if (listener != null) {
            registerTouchListener(viewHolder);
        }
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
}
