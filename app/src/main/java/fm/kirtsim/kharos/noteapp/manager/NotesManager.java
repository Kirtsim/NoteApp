package fm.kirtsim.kharos.noteapp.manager;

import android.support.annotation.NonNull;

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
        void onNotesFetched(@NonNull List<Note> notes);
        void onNewNoteAdded(@NonNull Note note);
        void onNoteUpdated(@NonNull Note note);
        void onNoteDeleted(@NonNull Note note);
        void onMultipleNotesDeleted(@NonNull List<Note> notes);
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

    public void updateNote(Note note) {
        if (note != null) {
            if (notesDatabase.updateNote(note) != 0) {
                final Note noteCopy = new Note(note);
                listeners.forEach(listener -> listener.onNoteUpdated(noteCopy));
            }
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

    public void removeNotes(List<Note> notes) {
        // TODO: perform on the background thread;
        if (notes != null && !notes.isEmpty()) {
            final List<Integer> ids = new ArrayList<>(notes.size());
            notes.forEach(note -> ids.add(note.getId()));
            notesDatabase.deleteNotes(ids);
            listeners.forEach(listener -> listener.onMultipleNotesDeleted(notes));
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
