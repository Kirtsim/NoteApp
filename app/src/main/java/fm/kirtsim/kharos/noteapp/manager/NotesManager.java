package fm.kirtsim.kharos.noteapp.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.db.NoteDbHelper;

/**
 * Created by kharos on 31/07/2017
 */

public class NotesManager {

    public interface NotesManagerListener {
        void onNotesFetched(List<Note> notes);
        void onNewNoteAdded(Note note);
        void onNoteDeleted(Note note);
    }

    private final NoteDbHelper notesDatabase;
    private final Set<NotesManagerListener> listeners;

    public NotesManager(NoteDbHelper notesDbHelper) {
        listeners = Collections.newSetFromMap(new ConcurrentHashMap<>(1));
        notesDatabase = notesDbHelper;
//        populateDatabase();
    }

    public void addNewNote(Note note) {
        // TODO: perform on the background thread;
        if (note != null && notesDatabase.insertNote(note) != -1) {
            final Note inserted = notesDatabase.selectNote(note);
            if (inserted != null) {
                listeners.forEach(listener ->  listener.onNewNoteAdded(inserted));
            }
        }
    }

    public void fetchNotes() {
        // TODO: perform on the background thread;
        List<Note> notes = notesDatabase.selectAllNotes();
        for (NotesManagerListener listener : listeners) {
            listener.onNotesFetched(notes);
        }
    }

    public void removeNote(Note note) {
        // TODO: perform on the background thread;
        if (note != null) {
            if (notesDatabase.deleteNote(note.getId()) != 0) {
                final Note noteCopy = new Note(note);
                listeners.forEach(listener -> listener.onNoteDeleted(noteCopy));
            }
        }
    }

    private void populateDatabase() {
        final int COUNT = 20;
        List<Note> notes = new ArrayList<>(COUNT);
        for (int i = 1; i <= COUNT; ++i) {
            notes.add(new Note("Title " + i, "Text text text text text " + i, i));
        }
        notes.forEach(notesDatabase::insertNote);
    }

    public void registerListener(NotesManagerListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        listeners.add(listener);
    }

    public void removeListener(NotesManagerListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        listeners.remove(listener);
    }
}
