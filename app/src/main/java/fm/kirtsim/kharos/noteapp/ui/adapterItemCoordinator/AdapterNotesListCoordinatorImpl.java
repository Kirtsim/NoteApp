package fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.utils.ListUtils;

/**
 * Created by kharos on 30/08/2017
 */

public class AdapterNotesListCoordinatorImpl implements AdapterNotesListCoordinator {

    private final List<Note> notes;
    private final Map<Integer, Note> highlighted;
    private final Map<Integer, Integer> noteIdsMappingToIndexes;

    public AdapterNotesListCoordinatorImpl() {
        notes = Lists.newArrayList();
        highlighted = Maps.newConcurrentMap();
        noteIdsMappingToIndexes = Maps.newConcurrentMap();
    }

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
    public List<Note> getListOfHighlightedNotes() {
        return Lists.newArrayList(highlighted.values());
    }

    @Override
    public Note getNoteAt(int index) {
        if (index < notes.size() && index >= 0)
            return notes.get(index);
        return null;
    }

    @Override
    public int getIndexOfNote(Note note) {
        return noteIdsMappingToIndexes.get(note.getId() - 1);
    }

    @Override
    public boolean addNote(Note note) {
        if (note != null) {
            if (noteIdsMappingToIndexes.get(note.getId()) != null)
                throw new IllegalArgumentException("note with id "+ note.getId() + " already exists");
            noteIdsMappingToIndexes.put(note.getId(), notes.size());
            return notes.add(note);
        }
        return false;
    }

    @Override
    public boolean addNotes(List<Note> newNotes) {
        if (newNotes != null) {
            newNotes.forEach(this::addNote);
            return true;
        }
        return false;
    }

    @Override
    public boolean addNoteToHighlighted(Note note) {
        if (getNoteIndex(note.getId()) != -1) {
            highlighted.put(note.getId(), note);
            return true;
        }
        return false;
    }

    @Override
    public void addAllNotesToHighlighted() {
        notes.forEach(note -> highlighted.put(note.getId(), note));
    }

    @Override
    public boolean removeNote(Note note) {
        if (note != null) {
            nullifyNoteFromNotesListAndIdsMapping(note, true);
            ListUtils.removeNullObjects(notes);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeNotes(List<Note> _notes) {
        if (_notes != null && !_notes.isEmpty()) {
            _notes.forEach(note -> nullifyNoteFromNotesListAndIdsMapping(note, true));
            ListUtils.removeNullObjects(notes);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeHighlightedNotes() {
        highlighted.forEach((id, note) -> nullifyNoteFromNotesListAndIdsMapping(note, false));
        ListUtils.removeNullObjects(notes);
        removeAllNotesFromHighlighted();
        return true;
    }

    private boolean nullifyNoteFromNotesListAndIdsMapping(Note note, boolean remFromHighlighted) {
        final int id = note.getId();
        final int index = getNoteIndex(id);
        if (index != -1) {
            notes.set(index, null);
            noteIdsMappingToIndexes.remove(id);
            if (remFromHighlighted)
                removeNoteFromHighlighted(note);
            return true;
        }
        return false;
    }

    private int getNoteIndex(int id) {
        Integer index = noteIdsMappingToIndexes.get(id);
        if (index == null)
            return -1;
        return index;
    }

    @Override
    public void removeNoteFromHighlighted(@NonNull Note note) {
        highlighted.remove(note.getId());
    }

    @Override
    public void removeAllNotesFromHighlighted() {
        highlighted.clear();
    }

    @Override
    public boolean updateNote(Note old, Note new_) {
        if (old == null || new_ == null) return false;
        final int index = getNoteIndex(old.getId());
        if (index != -1) {
            notes.set(index, new_);
            if (old.getId() != new_.getId()) {
                noteIdsMappingToIndexes.remove(old.getId());
                noteIdsMappingToIndexes.put(new_.getId(), index);
            }
            return true;
        }
        return false;
    }

    @Override
    public void swapNotesAt(int indexOfFirst, int indexOfSecond) {
        Collections.swap(notes, indexOfFirst, indexOfSecond);
        noteIdsMappingToIndexes.put(notes.get(indexOfFirst).getId(), indexOfFirst);
        noteIdsMappingToIndexes.put(notes.get(indexOfSecond).getId(), indexOfSecond);
    }

    @Override
    public Note getNoteWithIdOrDefault(int id, Note _default) {
        final int index = getNoteIndex(id);
        if (index != -1)
            return notes.get(index);
        return _default;
    }

    @Override
    public boolean isNoteHighlighted(Note note) {
        return highlighted.containsKey(note.getId());
    }

    @Override
    public boolean containsHighlightedNotes() {
        return !highlighted.isEmpty();
    }

    @Override
    public int getHighlightedCount() {
        return highlighted.size();
    }

    @Override
    public int getNoteCount() {
        return notes.size();
    }
}
