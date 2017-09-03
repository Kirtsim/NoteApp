package fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;

/**
 * Created by kharos on 30/08/2017
 */

public interface AdapterNotesListCoordinator {
    void setNewNotesList(List<Note> newNotes);

    void replaceNotesStartingFrom(List<Note> newNotes, int from);

    List<Note> getListOfAllNotes();

    List<Note> getListOfHighlightedNotes();

    Note getNoteAt(int index);

    int getIndexOfNote(Note note);

    boolean addNote(Note note);

    boolean addNotes(List<Note> newNotes);

    void addNoteToHighlighted(Note note);

    void addAllNotesToHighlighted();

    boolean removeNote(Note note);

    boolean removeNotes(List<Note> _notes);

    boolean removeHighlightedNotes();

    void removeNoteFromHighlighted(Note note);

    void removeAllNotesFromHighlighted();

    boolean updateNote(Note old, Note new_);

    void swapNotesAt(int indexOfFirst, int indexOfSecond);

    @SuppressWarnings("unused")
    Note getNoteWithIdOrDefault(int id, Note _default);

    boolean isNoteHighlighted(Note note);

    boolean containsHighlightedNotes();

    int getHighlightedCount();

    int getNoteCount();
}
