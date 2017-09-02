package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;

/**
 * Created by kharos on 31/08/2017
 */

public class NotesReorderer {

    private final Note.NoteBuilder noteBuilder;
    private final List<Note> notes;
    private final ArrayList<Note> updatedNotes;

    private int indexStart, indexEnd;

    public NotesReorderer(List<Note> notesList) {
        noteBuilder = new Note.NoteBuilder();
        notes = Lists.newArrayList(notesList);
        updatedNotes = Lists.newArrayList();
    }

    public List<Note> getNotes() {
        return Lists.newArrayList(notes);
    }

    public List<Note> getUpdatedNotes() {
        return Lists.newArrayList(updatedNotes);
    }

    public void changeOrderNumbersDueToPositionChange(int posFrom, int posTo) {
        reset();
        initializeIndexRange(posFrom, posTo);

        updateNotesOrdering(indexStart + 1);
        Note oldNote = notes.get(posTo);
        Note updatedNote = changeNoteOrderNumberTo(oldNote, posTo + 1);
        notes.set(posTo, updatedNote);
        updatedNotes.set(posTo < posFrom ? 0 : updatedNotes.size() -1, updatedNote);
    }

    private void initializeIndexRange(int posFrom, int posTo) {
        indexStart = Math.min(posFrom, posTo);
        indexEnd = Math.max(posFrom, posTo);
        throwIfInvalidIndexes();
    }

    public void changeOrderNumberOfNotesFromIndexStartingWithNumber(int index, int orderNumber) {
        reset();
        initializeIndexRange(index, notes.size() - 1);
        updateNotesOrdering(orderNumber);
    }

    private void updateNotesOrdering(int startOrderNumber) {
        int orderNumber = startOrderNumber;
        resizeUpdatedNotesListCapacity(indexEnd - indexStart + 1);
        for (int i = indexStart; i <= indexEnd; ++i) {
            Note oldNote = notes.get(i);
            Note newNote = changeNoteOrderNumberTo(oldNote, orderNumber++);
            notes.set(i, newNote);
            updatedNotes.add(newNote);
        }
    }

    private void throwIfInvalidIndexes() {
        if (indexStart < 0 || indexEnd >= notes.size())
            throw new IllegalArgumentException("indexes out of bounds");
    }

    private void resizeUpdatedNotesListCapacity(int capacity) {
        updatedNotes.ensureCapacity(capacity);
    }

    private Note changeNoteOrderNumberTo(@NonNull Note note, int orderNumber) {
        noteBuilder.copyValuesFrom(note);
        return noteBuilder.orderNumber(orderNumber).build();
    }

    @SuppressWarnings("WeakerAccess")
    public void reset() {
        updatedNotes.clear();
    }
}
