package fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator;

import android.support.annotation.NonNull;
import android.util.Pair;

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
        resetNoteData();
        addNotes(newNotes);
    }

    private void resetNoteData() {
        notes.clear();
        highlighted.clear();
        noteIdsMappingToIndexes.clear();
    }

    @Override
    public void replaceNotes(int from, List<Note> newNotes) {
        int indexToExc = Math.min(from + newNotes.size(), newNotes.size());
        for (int i = from; i < indexToExc; ++i) {
            if (getNoteAt(i) == null)
                System.out.println("blah");
            nullifyNoteFromNotesListAndIdsMapping(getNoteAt(i), true);
        }
        int currentSize = notes.size();
        int index = from;

        for (Note note : newNotes) {
            if (index < currentSize) {
                setNoteAt(index++, note);
            } else
                addNote(note);
        }
    }

    @Override
    public List<Note> getListOfNotes() {
        return getListOfNotes(0, notes.size());
    }

    @Override
    public List<Note> getListOfNotes(int indexFromIncl, int count) {
        if (indexFromIncl < 0 || count < 0)
            throw new IllegalArgumentException("from index or count is less than 0");
        final List<Note> retNotes = Lists.newArrayListWithCapacity(count);
        final int endIndexExc = Math.min(indexFromIncl + count, notes.size());
        for (int i = indexFromIncl; i < endIndexExc; ++i)
            retNotes.add(notes.get(i));
        return retNotes;
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
    public Pair<Note, Integer> popLastDeletedNoteAndItsIndex() {
        return null;
    }

    @Override
    public List<Pair<Note, Integer>> popLastDeletedNotesAndTheirIndexes(int count) {
        return null;
    }

    @Override
    public int getIndexOfNote(Note note) {
        return getNoteIndex(note.getId());
    }

    @Override
    public boolean addNote(Note note) {
        if (note != null) {
            if (isNoteIdTaken(note.getId()))
                throw new IllegalArgumentException("note with id "+ note.getId() + " already exists");
            noteIdsMappingToIndexes.put(note.getId(), notes.size());
            return notes.add(note);
        }
        return false;
    }

    @Override
    public boolean addNote(Note note, int index) {
        if (index < 0 || index >= notes.size())
            throw new IndexOutOfBoundsException("index: " + index + "  size: " + notes.size());
        boolean success = addNote(note);
        if (success) {
            for (int i = notes.size() -1; i > index; --i)
                swapNotesAt(i-1, i);
        }
        return success;
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

    private boolean isNoteIdTaken(int id) {
        return noteIdsMappingToIndexes.get(id) != null;
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
    public boolean removeNotes(int fromInc, int toExcl) {
        if (fromInc >= 0 && fromInc <= toExcl) {
            final int toIndexExcl = Math.min(toExcl, notes.size());
            for (int index = fromInc; index < toIndexExcl; ++index)
                nullifyNoteFromNotesListAndIdsMapping(getNoteAt(index), true);
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
        boolean wasOldHighlighted = isNoteHighlighted(old);
        boolean noteSet = setNoteAt(getIndexOfNote(old), new_);
        if (noteSet && wasOldHighlighted) {
            addNoteToHighlighted(new_);
        }
        return noteSet;
    }

    @Override
    public boolean setNoteAt(int index, Note note) {
        if (index < 0 || index > notes.size()) return false;
        boolean oldNoteExists = getNoteAt(index) != null;
        final int currentId = oldNoteExists ? getNoteAt(index).getId() : -1;
        if (oldNoteExists && currentId != note.getId() && isNoteIdTaken(note.getId()))
            throw new IllegalArgumentException("note with id " + note.getId() + "already exists");
        if (oldNoteExists)
            nullifyNoteFromNotesListAndIdsMapping(getNoteAt(index), true);
        notes.set(index, note);
        noteIdsMappingToIndexes.put(note.getId(), index);
        return true;
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
