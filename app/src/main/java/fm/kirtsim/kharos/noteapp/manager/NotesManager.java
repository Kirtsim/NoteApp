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
    }

    private final NoteDbHelper notesDatabase;
    private final Set<NotesManagerListener> listeners;

    public NotesManager(NoteDbHelper notesDbHelper) {
        listeners = Collections.newSetFromMap(new ConcurrentHashMap<>(1));
        notesDatabase = notesDbHelper;
    }

    public void fetchNotes() {
        // TODO: fetch notes from the background thread and update listeners
        List<Note> notes = getDummyNotes();
        for (NotesManagerListener listener : listeners) {
            listener.onNotesFetched(notes);
        }
    }

    private List<Note> getDummyNotes() {
        final int COUNT = 20;
        List<Note> notes = new ArrayList<>(COUNT);
        for (int i = 1; i <= COUNT; ++i) {
            notes.add(new Note("Title " + i, "Text text text text text " + i, i));
        }
        return notes;
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
