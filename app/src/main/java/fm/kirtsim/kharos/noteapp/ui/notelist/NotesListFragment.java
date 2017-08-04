package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListFragment extends BaseFragment implements
        NotesListAdapter.NotesListAdapterListener,
        NotesListViewMvc.NotesListViewMvcListener,
        NotesManager.NotesManagerListener {

    private NotesListViewMvc mvcView;
    @Inject NotesListAdapter notesListAdapter;
    @Inject NotesManager notesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        notesListAdapter.registerListener(this);
        notesManager.registerListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mvcView = new NotesListViewMvcImpl(inflater, container,
                notesListAdapter, new LinearLayoutManager(inflater.getContext()));
        mvcView.registerListener(this);
        return mvcView.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        notesManager.fetchNotes();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notesListAdapter = null;
        notesManager.removeListener(this);
        notesManager = null;

        mvcView.unregisterListener(this);
        mvcView = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save_delete, menu);
        MenuItem item = menu.findItem(R.id.save);
        if (item != null) {
            item.setEnabled(false);
            item.setVisible(false);
        }
        item = menu.findItem(R.id.delete);
        if (item != null) {
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    /* ************************************************************************
     *                          BaseFragment methods
     * ************************************************************************/

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    // ###################### BaseFragment ########################


    /* ************************************************************************
     *                NoteListAdapterListener methods
     * ************************************************************************/

    @Override
    public void onNoteItemSingleClicked(Note note) {
        Bundle arguments = new Bundle(4);
        arguments.putInt(NoteDetailFragment.ARG_NOTE_ID, note.getId());
        arguments.putString(NoteDetailFragment.ARG_NOTE_TITLE, note.getTitle());
        arguments.putString(NoteDetailFragment.ARG_NOTE_TEXT, note.getText());
        arguments.putLong(NoteDetailFragment.ARG_NOTE_TIME, note.getTimestamp());
        startNewFragment(NoteDetailFragment.class, arguments, true);
    }

    @Override
    public void onNoteItemLongClicked(Note note) {
        Toast.makeText(getContext(), "long: " + note.getTitle(), Toast.LENGTH_LONG).show();
    }
    // ###################### NoteListAdapterListener ########################


    /* ************************************************************************
     *                 NotesManagerListener methods
     * ************************************************************************/

    @Override
    public void onNotesFetched(@NonNull List<Note> notes) {
        notesListAdapter.setNewNotesList(notes);
    }

    @Override public void onNewNoteAdded(@NonNull Note note) {}

    @Override public void onNoteUpdated(@NonNull Note note) {}

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        if (notesListAdapter.deleteNote(note, true)) {
            Toast.makeText(getContext(), R.string.note_deleted_message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        if (notesListAdapter.deleteNotes(notes)) {
            Toast.makeText(getContext(), "Notes deleted", Toast.LENGTH_LONG).show();
        }
    }
    // ###################### NotesManagerListener ########################


    /* ************************************************************************
     *                 NotesListViewMvcListener methods
     * ************************************************************************/

    @Override
    public void onNewNoteRequested() {
        startNewFragment(NoteDetailFragment.class, null, true);
    }
}
