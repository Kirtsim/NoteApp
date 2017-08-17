package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.Animations;
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
    @Inject NotesListActionBarViewMvc actionBarMvc;

    private NotesListViewMvc mvcView;
    private final Set<Integer> highlightedNotes = Sets.newHashSet();

    private Animations noteDetailAnimations;

    private int COLOR_HIGHLIGHTED_BACKGROUND;
    private int COLOR_HIGHLIGHTED_FRAME;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeColors(getResources());
        setHasOptionsMenu(true);

        listAdapter.setListener(this);
        notesManager.registerListener(this);
        actionBarMvc.setShowHomeButton(false);
        noteDetailAnimations = getAnimationsForNoteDetailFragment();
    }

    @Override
    protected boolean performInjection(ControllerComponent component) {
        component.inject(this);
        return true;
    }

    private void initializeColors(Resources resources) {
        COLOR_HIGHLIGHTED_BACKGROUND = ResourcesCompat.getColor(resources,
                R.color.note_detail_selected_background, null);
        COLOR_HIGHLIGHTED_FRAME = ResourcesCompat.getColor(resources,
                R.color.note_detail_selected_frame, null);
    }

    private Animations getAnimationsForNoteDetailFragment() {
        Animations.Builder builder = new Animations.Builder();
        builder.setEnterAnimation(R.anim.slide_enter)
                .setExitAnimation(R.anim.slide_exit)
                .setPopEnterAnimation(R.anim.pop_slide_enter)
                .setPopExitAnimation(R.anim.pop_slide_exit);
        return builder.build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mvcView = new NotesListViewMvcImpl(inflater, container, listAdapter,
                new LinearLayoutManager(inflater.getContext()));
        mvcView.registerListener(this);
        actionBarMvc.setTitle(getString(R.string.your_notes_title));
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
        actionBarMvc.setMenu(menu, inflater);
        actionBarMvc.setDeleteMenuItemVisible(false);
        actionBarMvc.setSelectAllMenuItemVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_delete: deleteHighlightedNotes();break;
            case R.id.mi_select_all: addAllNotesToHighlighted(); break;
            case android.R.id.home: clearNoteHighlighting(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void deleteHighlightedNotes() {
        List<Note> notes = Lists.newArrayListWithCapacity(highlightedNotes.size());
        final Note defaultNote = new Note(-1, null, null, 0);
        highlightedNotes.forEach(id ->
                notes.add(listAdapter.getNoteWithIdOrDefault(id, defaultNote)));
        notesManager.removeNotes(notes);
    }

    private void addAllNotesToHighlighted() {
        List<Note> notes = listAdapter.getListOfAllNotes();
        notes.forEach(note -> highlightedNotes.add(note.getId()));
        onNoteIdAddedToHighlighted();
        listAdapter.updateDataSet();
    }

    private void clearNoteHighlighting() {
        highlightedNotes.clear();
        onNoteIdRemovedFromHighlighted();
        listAdapter.updateDataSet();
    }

    /* ************************************************************************
     *                          BaseFragment methods
     * ************************************************************************/

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected boolean onBackPressed() {
        if (!highlightedNotes.isEmpty()) {
            clearNoteHighlighting();
            return true;
        }
        return false;
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
        startNewFragment(NoteDetailFragment.class, arguments, noteDetailAnimations, true);
    }

    @MainThread
    @Override
    public void onNoteItemLongClicked(Note note, NotesListItemViewMvc noteItemView, int notePosition) {
        final boolean isHighlighted = highlightedNotes.contains(note.getId());
        if (isHighlighted) {
            highlightedNotes.remove(note.getId());
            onNoteIdRemovedFromHighlighted();
        } else {
            highlightedNotes.add(note.getId());
            onNoteIdAddedToHighlighted();
        }
        setNoteItemBackground(noteItemView, isHighlighted);
        listAdapter.updateItemAtPosition(notePosition);
    }

    private void onNoteIdRemovedFromHighlighted() {
        if (highlightedNotes.isEmpty()) {
            actionBarMvc.setDeleteMenuItemVisible(false);
            actionBarMvc.setSelectAllMenuItemVisible(false);
            actionBarMvc.setDisplayHomeAsUp(false);
            actionBarMvc.setTitle(getString(R.string.your_notes_title));
            mvcView.showAddButton();
        } else {
            actionBarMvc.setTitle(String.valueOf(highlightedNotes.size()));
        }
    }

    private void onNoteIdAddedToHighlighted() {
        if (highlightedNotes.size() == 1) {
            actionBarMvc.setDeleteMenuItemVisible(true);
            actionBarMvc.setDisplayHomeAsUp(true);
            actionBarMvc.setSelectAllMenuItemVisible(true);
            mvcView.hideAddButton();
        }
        actionBarMvc.setTitle(String.valueOf(highlightedNotes.size()));
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
        listAdapter.updateDataSet();
    }

    @Override public void onNewNoteAdded(@NonNull Note note) {
        showToast(R.string.note_saved_message, "");
    }

    @Override public void onNoteUpdated(@NonNull Note note) {
        onNewNoteAdded(note);
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        clearNoteHighlighting();
        if (listAdapter.removeNote(note)) {
            listAdapter.notifyNoteRemoved(note);
            showToast(R.string.note_deleted_message, "");
        } else
            notesManager.fetchNotes();
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        clearNoteHighlighting();
        if (listAdapter.removeNotes(notes)) {
            listAdapter.updateDataSet();
            showToast(R.string.note_deleted_message, "s");
        } else
            notesManager.fetchNotes();
    }
    // ###################### NotesManagerListener ########################


    /* ************************************************************************
     *                 NotesListViewMvcListener methods
     * ************************************************************************/

    @Override
    public void onNewNoteRequested() {
        startNewFragment(NoteDetailFragment.class, null, noteDetailAnimations, true);
    }
}
