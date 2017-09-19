package fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator;

import android.util.Pair;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;

/**
 * Created by kharos on 30/08/2017
 */

public interface AdapterNotesListCoordinator {
    void setNewNotesList(List<Note> newNotes);

    void replaceNotes(int fromInc, List<Note> newNotes);

    List<Note> getListOfNotes();

    List<Note> getListOfNotes(int from, int count);

    List<Note> getListOfHighlightedNotes();

    Note getNoteAt(int index);

    int getIndexOfNote(Note note);

    boolean addNote(Note note);

    boolean addNote(Note note, int index);

    boolean addNotes(List<Note> newNotes);

    boolean addNoteToHighlighted(Note note);

    void addAllNotesToHighlighted();

    boolean removeNote(Note note);

    boolean removeNotes(List<Note> _notes);

    boolean removeNotes(int fromIncl, int toExcl);

    @SuppressWarnings("unused")
    boolean removeHighlightedNotes();

    void removeNoteFromHighlighted(Note note);

    void removeAllNotesFromHighlighted();

    boolean updateNote(Note old, Note new_);

    boolean setNoteAt(int index, Note note);

    void swapNotesAt(int indexOfFirst, int indexOfSecond);

    @SuppressWarnings("unused")
    Note getNoteWithIdOrDefault(int id, Note _default);

    boolean isNoteHighlighted(Note note);

    boolean containsHighlightedNotes();

    int getHighlightedCount();

    int getNoteCount();
}
