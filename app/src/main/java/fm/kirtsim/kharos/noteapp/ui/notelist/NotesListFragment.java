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

import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListFragment extends BaseFragment implements
        NotesListAdapterImpl.NotesListAdapterListener,
        NotesListViewMvc.NotesListViewMvcListener,
        NotesManager.NotesManagerListener {

    @Inject NotesListAdapter listAdapter;
    @Inject NotesManager notesManager;
    private NotesListViewMvc mvcView;
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
        listAdapter.setListener(this);
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
        mvcView = new NotesListViewMvcImpl(inflater, container, listAdapter,
                new LinearLayoutManager(inflater.getContext()));
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
        listAdapter = null;
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
        List<Note> notes = Lists.newArrayListWithCapacity(highlightedNotes.size());
        final Note defaultNote = new Note(-1, null, null, 0);
        highlightedNotes.forEach(id ->
                notes.add(listAdapter.getNoteWithIdOrDefault(id, defaultNote)));
        notesManager.removeNotes(notes);
    }

    void clearNoteHighlighting() {
        highlightedNotes.clear();
        menuMvc.hideDeleteMenuItem();
        listAdapter.updateDataSet();
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
        listAdapter.updateItemAtPosition(notePosition);
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
        listAdapter.setNewNotesList(notes);
    }

    @Override public void onNewNoteAdded(@NonNull Note note) {
        showToast(R.string.note_saved_message, "");
    }

    @Override public void onNoteUpdated(@NonNull Note note) {
        onNewNoteAdded(note);
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        if (listAdapter.removeNote(note)) {
            listAdapter.notifyNoteRemoved(note);
            clearNoteHighlighting();
            showToast(R.string.note_deleted_message, "");
//            return;
        }
        // TODO: refetch all the notes and update the adapter
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        if (listAdapter.removeNotes(notes)) {
            listAdapter.updateDataSet();
            clearNoteHighlighting();
            showToast(R.string.note_deleted_message, "s");
//            return;
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
