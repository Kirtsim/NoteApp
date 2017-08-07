package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private NotesListMenuViewMvc menuMvc;
    private final Set<Integer> highlightedNotes = new HashSet<>(1);

    private int COLOR_HIGHLIGHTED_BACKGROUND;
    private int COLOR_HIGHLIGHTED_FRAME;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        initializeColors(getResources());
        setHasOptionsMenu(true);
        notesListAdapter.registerListener(this);
        notesManager.registerListener(this);
    }

    private void initializeColors(Resources resources) {
        COLOR_HIGHLIGHTED_BACKGROUND = ResourcesCompat.getColor(resources,
                R.color.note_detail_selected_background, null);
        COLOR_HIGHLIGHTED_FRAME = ResourcesCompat.getColor(resources,
                R.color.note_detail_selected_frame, null);
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
        menuMvc = new NotesListMenuViewMvcImpl(menu, inflater);
        menuMvc.hideDeleteMenuItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteHighlightedNotes();
                break;
            case android.R.id.home:
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    void deleteHighlightedNotes() {
        boolean deleted = false;
        List<Note> notes = Lists.newArrayListWithCapacity(highlightedNotes.size());
        final Note defaultNote = new Note(-1, null, null, 0);
        highlightedNotes.forEach(id ->
                notes.add(notesListAdapter.getNoteWithIdOrDefault(id, defaultNote)));
        notesManager.removeNotes(notes);
    }

    void clearNoteHighlighting() {
        highlightedNotes.clear();
        menuMvc.hideDeleteMenuItem();
        notesListAdapter.notifyDataSetChanged();
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
    public void onNoteItemSingleClicked(Note note, NotesListItemViewMvc noteItemView,
                                        int notePosition) {
        if (!highlightedNotes.isEmpty())
            onNoteItemLongClicked(note, noteItemView, notePosition);
        else {
            displayDetailsOfNote(note);
        }
    }

    private void displayDetailsOfNote(Note note) {
        Bundle arguments = new Bundle(4);
        arguments.putInt(NoteDetailFragment.ARG_NOTE_ID, note.getId());
        arguments.putString(NoteDetailFragment.ARG_NOTE_TITLE, note.getTitle());
        arguments.putString(NoteDetailFragment.ARG_NOTE_TEXT, note.getText());
        arguments.putLong(NoteDetailFragment.ARG_NOTE_TIME, note.getTimestamp());
        startNewFragment(NoteDetailFragment.class, arguments, true);
    }

    @Override
    public void onNoteItemLongClicked(Note note, NotesListItemViewMvc noteItemView, int notePosition) {
        final int noteId = note.getId();
        final boolean isHighlighted = highlightedNotes.contains(noteId);
        if (isHighlighted) {
            removeNoteIdFromHighlighted(noteId);
        } else {
            addNoteIdToHighlighted(noteId);
        }
        setNoteItemBackground(noteItemView, isHighlighted);
        notesListAdapter.notifyItemChanged(notePosition);
    }

    private void addNoteIdToHighlighted(int noteId) {
        highlightedNotes.add(noteId);
        if (highlightedNotes.size() == 1)
            menuMvc.showDeleteMenuItem();
    }

    private void removeNoteIdFromHighlighted(int noteId) {
        highlightedNotes.remove(noteId);
        if (highlightedNotes.isEmpty())
            menuMvc.hideDeleteMenuItem();
    }

    @Override
    public void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView) {
        noteItemView.setText(note.getText());
        noteItemView.setTitle(note.getTitle());
        setNoteItemBackground(noteItemView, highlightedNotes.contains(note.getId()));
    }

    private void setNoteItemBackground(NotesListItemViewMvc noteItemView, boolean isHighlighted) {
        if (isHighlighted) {
            noteItemView.setBackgroundColors(COLOR_HIGHLIGHTED_FRAME, COLOR_HIGHLIGHTED_BACKGROUND);
        } else {
            noteItemView.setBackgroundColors(Color.WHITE, Color.WHITE);
        }
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
        if (notesListAdapter.removeNote(note)) {
            notesListAdapter.notifyNoteRemoved(note);
            clearNoteHighlighting();
            Toast.makeText(getContext(), R.string.note_deleted_message, Toast.LENGTH_LONG).show();
            return;
        }
        // TODO: refetch all the notes and update the adapter
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        if (notesListAdapter.removeNotes(notes)) {
            notesListAdapter.notifyDataSetChanged();
            clearNoteHighlighting();
            Toast.makeText(getContext(), getResources().
                    getString(R.string.note_deleted_message, "s"), Toast.LENGTH_LONG).show();
            return;
        }
        // TODO: refetch all the notes and update the adapter
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
