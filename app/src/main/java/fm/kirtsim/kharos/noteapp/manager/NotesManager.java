package fm.kirtsim.kharos.noteapp.manager;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.db.NoteDbHelper;
import fm.kirtsim.kharos.noteapp.threading.BackgroundThreadPoster;
import fm.kirtsim.kharos.noteapp.threading.MainThreadPoster;

/**
 * Created by kharos on 31/07/2017
 */

public class NotesManager {

    public interface NotesManagerListener {
        void onNotesFetched(@NonNull List<Note> notes);
        void onNewNoteAdded(@NonNull Note note);
        void onNoteUpdated(@NonNull Note note);
        void onMultipleNotesUpdated(@NonNull List<Note> notes);
        void onNoteDeleted(@NonNull Note note);
        void onMultipleNotesDeleted(@NonNull List<Note> notes);
    }

    private final NoteDbHelper notesDatabase;
    private final Set<NotesManagerListener> listeners;
    private final MainThreadPoster mainThreadPoster;
    private final BackgroundThreadPoster backgroundThreadPoster;

    public NotesManager(NoteDbHelper notesDbHelper,
                        MainThreadPoster mainThreadPoster,
                        BackgroundThreadPoster backgroundThreadPoster) {
        listeners = Collections.newSetFromMap(new ConcurrentHashMap<>(1));
        notesDatabase = notesDbHelper;
        this.mainThreadPoster = mainThreadPoster;
        this.backgroundThreadPoster = backgroundThreadPoster;
    }

    /**
     * Inserts a new Note into the database
     * @param note note that has been inserted also containing the Id it has been assigned
     */
    public void addNewNote(Note note) {
        backgroundThreadPoster.post(() -> insertNote(note));
    }

    private void insertNote(Note note) {
        if (note != null && notesDatabase.insertNote(note) != -1) {
            final Note inserted = notesDatabase.selectNote(note);
            if (inserted != null) {
                mainThreadPoster.post(() ->
                        listeners.forEach(listener -> listener.onNewNoteAdded(inserted)));
            }
        }
    }

    public void fetchNotes() {
        backgroundThreadPoster.post(this::selectAllNotes);
    }

    private void selectAllNotes() {
        List<Note> notes = notesDatabase.selectAllNotes();
        mainThreadPoster.post(() ->
                        listeners.forEach(listener -> listener.onNotesFetched(notes)));
    }

    public void updateNote(Note note) {
        backgroundThreadPoster.post(() -> updateSingleNote(note));
    }

    private void updateSingleNote(Note note) {
        if (note != null) {
            if (notesDatabase.updateNote(note) != 0) {
                final Note noteCopy = new Note(note);
                mainThreadPoster.post(() ->
                        listeners.forEach(listener -> listener.onNoteUpdated(noteCopy)));
            }
        }
    }

    public void updateNotes(List<Note> notes) {
        backgroundThreadPoster.post(() -> updateMultipleNotes(notes));
    }

    private void updateMultipleNotes(List<Note> notes) {
        if (notes != null && !notes.isEmpty()) {
            notesDatabase.updateNotes(notes);
            mainThreadPoster.post(() -> listeners.forEach(l -> l.onMultipleNotesUpdated(notes)));
        }
    }

    public void removeNote(Note note) {
        backgroundThreadPoster.post(() -> deleteNote(note));
    }

    private void deleteNote(Note note) {
        if (note != null) {
            if (notesDatabase.deleteNote(note.getId()) != 0) {
                final Note noteCopy = new Note(note);
                mainThreadPoster.post(() ->
                        listeners.forEach(listener -> listener.onNoteDeleted(noteCopy)));
            }
        }
    }

    public void removeNotes(List<Note> notes) {
        backgroundThreadPoster.post(() -> deleteMultipleNotes(notes));
    }

    private void deleteMultipleNotes(List<Note> notes) {
        if (notes != null && !notes.isEmpty()) {
            final List<Integer> ids = new ArrayList<>(notes.size());
            notes.forEach(note -> ids.add(note.getId()));
            notesDatabase.deleteNotes(ids);
            mainThreadPoster.post(() ->
                    listeners.forEach(listener -> listener.onMultipleNotesDeleted(notes)));
        }
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
        if (listeners.isEmpty()) {
            notesDatabase.close();
        }
    }
}
