package fm.kirtsim.kharos.noteapp.ui.adapter;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.utils.ListUtils;

/**
 * Created by kharos on 07/08/2017
 */

public interface NotesListAdapter extends ListAdapter<NotesListAdapter.NotesListAdapterListener> {

    public interface NotesListAdapterListener {
        void onNoteItemSingleClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemLongClicked(Note note, NotesListItemViewMvc noteItemView, int listPosition);
        void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView);
    }

    void setNewNotesList(List<Note> newNotes);

    List<Note> getListOfAllNotes();

    boolean addNotes(List<Note> newNotes);

    boolean removeNote(Note note);

    boolean removeNotes(List<Note> _notes);

    Note getNoteWithIdOrDefault(int id, Note _default);

    void notifyNoteRemoved(Note note);
}
