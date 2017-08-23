package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.content.res.Resources;
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
import fm.kirtsim.kharos.noteapp.threading.BackgroundThreadPoster;
import fm.kirtsim.kharos.noteapp.ui.Animations;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerViewMvc;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;
import fm.kirtsim.kharos.noteapp.utils.Units;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListFragment extends BaseFragment implements
        NotesListAdapterImpl.NotesListAdapterListener,
        NotesListViewMvc.NotesListViewMvcListener,
        NotesManager.NotesManagerListener {

    @Inject NotesListAdapter notesListAdapter;
    @Inject NotesManager notesManager;
    @Inject NotesListActionBarViewMvc actionBarMvc;
    @Inject BackgroundThreadPoster backgroundPoster;

    private NotesListViewMvc viewMvc;
    private ColorPickerViewMvc colorPickerViewMvc;
    private final Set<Integer> highlightedNotes = Sets.newHashSet();

    private Animations noteDetailAnimations;

    private int COLOR_HIGHLIGHTED_BACKGROUND;
    private int COLOR_HIGHLIGHTED_FRAME;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeColors(getResources());
        setHasOptionsMenu(true);

        notesListAdapter.setListener(this);
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
        viewMvc = new NotesListViewMvcImpl(inflater, container, notesListAdapter,
                new LinearLayoutManager(inflater.getContext()));
        viewMvc.registerListener(this);
        viewMvc.addNoteItemDecoration(new NotesListItemDecorationImpl(
                Units.dp2px(3, getResources().getDisplayMetrics())));

        colorPickerViewMvc = new ColorPickerViewMvcImpl(inflater, null);
        viewMvc.addViewToRightSideContainer(colorPickerViewMvc.getRootView());

        actionBarMvc.setTitle(getString(R.string.your_notes_title));
        return viewMvc.getRootView();
    }

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

        viewMvc.unregisterListener(this);
        viewMvc = null;
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
            case R.id.mi_color_picker: showOrHideColorPicker(); break;
            case android.R.id.home: clearNoteHighlighting(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showOrHideColorPicker() {
        if (viewMvc.isRightSideContainerVisible())
            viewMvc.hideColorPicker();
        else
            viewMvc.showColorPicker();
    }

    private void deleteHighlightedNotes() {
        List<Note> notes = Lists.newArrayListWithCapacity(highlightedNotes.size());
        final Note defaultNote = new Note(-1, -1, -1, false, null, null, 0);
        highlightedNotes.forEach(id ->
                notes.add(notesListAdapter.getNoteWithIdOrDefault(id, defaultNote)));
        notesManager.removeNotes(notes);
    }

    private void addAllNotesToHighlighted() {
        List<Note> notes = notesListAdapter.getListOfAllNotes();
        notes.forEach(note -> highlightedNotes.add(note.getId()));
        onNoteIdAddedToHighlighted();
        notesListAdapter.updateDataSet();
    }

    private void clearNoteHighlighting() {
        highlightedNotes.clear();
        onNoteIdRemovedFromHighlighted();
        notesListAdapter.updateDataSet();
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
        arguments.putInt(NoteDetailFragment.ARG_NOTE_ORDER_NO, note.getOrderNo());
        arguments.putInt(NoteDetailFragment.ARG_NOTE_COLOR, note.getColor());
        arguments.putBoolean(NoteDetailFragment.ARG_NOTE_PINNED, note.isPinned());
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
        setNoteItemBackground(note, noteItemView);
        notesListAdapter.updateItemAtPosition(notePosition);
    }

    private void onNoteIdRemovedFromHighlighted() {
        if (highlightedNotes.isEmpty()) {
            actionBarMvc.setDeleteMenuItemVisible(false);
            actionBarMvc.setSelectAllMenuItemVisible(false);
            actionBarMvc.setDisplayHomeAsUp(false);
            actionBarMvc.setTitle(getString(R.string.your_notes_title));
            viewMvc.showAddButton();
        } else {
            actionBarMvc.setTitle(String.valueOf(highlightedNotes.size()));
        }
    }

    private void onNoteIdAddedToHighlighted() {
        if (highlightedNotes.size() == 1) {
            actionBarMvc.setDeleteMenuItemVisible(true);
            actionBarMvc.setDisplayHomeAsUp(true);
            actionBarMvc.setSelectAllMenuItemVisible(true);
            viewMvc.hideAddButton();
        }
        actionBarMvc.setTitle(String.valueOf(highlightedNotes.size()));
    }

    @Override
    public void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView) {
        noteItemView.setText(note.getText());
        noteItemView.setTitle(note.getTitle());
        setNoteItemBackground(note, noteItemView);
    }

    private void setNoteItemBackground(Note note, NotesListItemViewMvc noteItemView) {
        if (highlightedNotes.contains(note.getId())) {
            noteItemView.setBackgroundColors(COLOR_HIGHLIGHTED_FRAME, COLOR_HIGHLIGHTED_BACKGROUND);
        } else {
            final int color = note.getColor();
            noteItemView.setBackgroundColors(color, color);
        }
    }
    // ###################### NoteListAdapterListener ########################


    /* ************************************************************************
     *                 NotesManagerListener methods
     * ************************************************************************/

    @Override
    public void onNotesFetched(@NonNull List<Note> notes) {
        notesListAdapter.setNewNotesList(notes);
        notesListAdapter.updateDataSet();
    }

    @Override public void onNewNoteAdded(@NonNull Note note) {
        if (note.getId() != -1)
            backgroundPoster.post(() -> updateOrderNumbersOfNotesStartingWithNote(note));
        else
            notesManager.fetchNotes();
    }

    private void updateOrderNumbersOfNotesStartingWithNote(Note newNote) {
        List<Note> notes = notesListAdapter.getListOfAllNotes();
        List<Note> updatedNotes = Lists.newArrayListWithCapacity(notes.size() + 1);
        if (notes.isEmpty() || !notes.get(0).equals(newNote))
            updatedNotes.add(incrementNoteOrderNumber(newNote));
        notes.forEach(note -> updatedNotes.add(incrementNoteOrderNumber(note)));
//        updatedNotes.forEach(note -> Log.d(getClassName(), "" + note.getOrderNo()));
        notesManager.updateNotes(updatedNotes);
    }

    private Note incrementNoteOrderNumber(@NonNull Note note) {
        return new Note(
                note.getId(),
                note.getOrderNo() + 1,
                note.getColor(),
                note.isPinned(),
                note.getTitle(),
                note.getText(),
                note.getTimestamp()
        );
    }

    @Override public void onNoteUpdated(@NonNull Note note) {
        notesListAdapter.notifyNoteChanged(note);
    }

    @Override
    public void onMultipleNotesUpdated(@NonNull List<Note> notes) {
        showToast(R.string.note_saved_message, "");
        notesListAdapter.setNewNotesList(notes);
        notesListAdapter.updateDataSet();
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        clearNoteHighlighting();
        if (notesListAdapter.removeNote(note)) {
            notesListAdapter.notifyNoteRemoved(note);
            showToast(R.string.note_deleted_message, "");
        } else
            notesManager.fetchNotes();
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        clearNoteHighlighting();
        if (notesListAdapter.removeNotes(notes)) {
            notesListAdapter.updateDataSet();
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
