package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.threading.BackgroundThreadPoster;
import fm.kirtsim.kharos.noteapp.threading.MainThreadPoster;
import fm.kirtsim.kharos.noteapp.ui.Animations;
import fm.kirtsim.kharos.noteapp.ui.adapter.ColorPickerAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapterImpl;
import fm.kirtsim.kharos.noteapp.ui.adapter.itemTouchHelper.NotesListItemTouchHelper;
import fm.kirtsim.kharos.noteapp.ui.adapter.touchCallback.NotesListItemTouchCallback;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerViewMvc;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.ColorPickerItemDecoration;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.NotesListItemDecorationImpl;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;
import fm.kirtsim.kharos.noteapp.utils.Units;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListFragment extends BaseFragment implements
        NotesListAdapterImpl.NotesListAdapterListener,
        NotesListViewMvc.NotesListViewMvcListener,
        NotesManager.NotesManagerListener,
        ColorPickerAdapter.ColorPickerAdapterListener {

    @Inject NotesListAdapter notesListAdapter;
    @Inject ColorPickerAdapter colorsListAdapter;
    @Inject NotesManager notesManager;
    @Inject NotesListActionBarViewMvc actionBarMvc;
    @Inject MainThreadPoster mainThreadPoster;
    @Inject BackgroundThreadPoster backgroundPoster;

    private NotesListViewMvc notesListViewMvc;
    @SuppressWarnings("FieldCanBeLocal")
    private ColorPickerViewMvc colorPickerViewMvc;
    private final Map<Integer, Note> highlightedNotes = Maps.newConcurrentMap();

    private Animations noteDetailAnimations;
    private NotesListItemTouchHelper notesTouchHelper;

    private int selectedNoteColor;
    private int COLOR_HIGHLIGHTED_FRAME;

    private int updateOperationFlag;

    private static final int UPDATE_ORDERING = 1;
    private static final int UPDATE_COLOR = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeColors(getResources());
        setHasOptionsMenu(true);

        notesListAdapter.setListener(this);
        colorsListAdapter.setListener(this);
        colorsListAdapter.setColors(getContext().getResources()
                .getIntArray(R.array.color_picker_colors));
        colorsListAdapter.setHighlightColor(Color.BLACK);
        notesManager.registerListener(this);
        actionBarMvc.setHomeButtonVisible(false);
        noteDetailAnimations = getAnimationsForNoteDetailFragment();
        notesTouchHelper = new NotesListItemTouchHelper(
                new NotesListItemTouchCallback(notesListAdapter), null);
    }

    @Override
    protected boolean performInjection(ControllerComponent component) {
        component.inject(this);
        return true;
    }

    private void initializeColors(Resources resources) {
        selectedNoteColor = Color.WHITE;
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
        notesListViewMvc = new NotesListViewMvcImpl(inflater, container, notesListAdapter,
                new LinearLayoutManager(inflater.getContext()));
        notesListViewMvc.registerListener(this);
        notesListViewMvc.addNoteItemDecoration(new NotesListItemDecorationImpl(
                Units.dp2px(3, getResources().getDisplayMetrics())));

        colorPickerViewMvc = new ColorPickerViewMvcImpl(inflater, null);
        colorPickerViewMvc.setLayoutManager(new LinearLayoutManager(getContext()));
        colorPickerViewMvc.setAdapter(colorsListAdapter);
        colorPickerViewMvc.addColorItemsDecoration(new ColorPickerItemDecoration(
                Units.dp2px(10, getResources().getDisplayMetrics())));
        notesListViewMvc.addViewToRightSideContainer(colorPickerViewMvc.getRootView());
        notesTouchHelper.attachToRecyclerView(notesListViewMvc.getRecyclerView());
        notesTouchHelper.listen(false);

        actionBarMvc.setTitle(getString(R.string.your_notes_title));
        return notesListViewMvc.getRootView();
    }

    public void onStart() {
        super.onStart();
        notesManager.fetchNotes();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        colorsListAdapter.unregisterListener();

        notesListAdapter.unregisterListener();
        notesListAdapter = null;
        notesManager.removeListener(this);
        notesManager = null;

        notesListViewMvc.unregisterListener(this);
        notesListViewMvc = null;
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
            case R.id.mi_reorder:
                actionBarMvc.setHomeButtonVisible(true);
                actionBarMvc.setColorPaletteItemVisible(false);
                actionBarMvc.setReorderItemVisible(false);
                notesTouchHelper.listen(true);
                break;
            case android.R.id.home: clearNoteHighlighting();
                if (notesListViewMvc.isRightSideContainerVisible())
                    notesListViewMvc.hideRightSideContainer();
                actionBarMvc.setReorderItemVisible(true);
                actionBarMvc.setColorPaletteItemVisible(true);
                notesTouchHelper.listen(false);
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showOrHideColorPicker() {
        if (notesListViewMvc.isRightSideContainerVisible())
            notesListViewMvc.hideRightSideContainer();
        else {
            notesListViewMvc.showRightSideContainer();
            if (highlightedNotes.isEmpty()) { // TODO: a bad design BLEUH!
                actionBarMvc.setColorPaletteItemVisible(false);
                actionBarMvc.setHomeButtonVisible(true);
            }
        }
    }

    private void deleteHighlightedNotes() {
        List<Note> notes = Lists.newArrayListWithCapacity(highlightedNotes.size());
        highlightedNotes.forEach((id, note) -> notes.add(note));
        notesManager.removeNotes(notes);
    }

    private void addAllNotesToHighlighted() {
        List<Note> notes = notesListAdapter.getListOfAllNotes();
        notes.forEach(note -> highlightedNotes.put(note.getId(), note));
        onNoteAddedToHighlighted();
        notesListAdapter.updateDataSet();
    }

    private void clearNoteHighlighting() {
        highlightedNotes.clear();
        onNoteRemovedFromHighlighted();
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
        else if (notesListViewMvc.isRightSideContainerVisible()) {
            Note coloredNote = createColorUpdatedNote(note, selectedNoteColor);
            updateNoteInDatabase(coloredNote, UPDATE_COLOR);
        } else {
            displayDetailsOfNote(note);
        }
    }

    private Note createColorUpdatedNote(Note note, @ColorInt int color) {
        Note updated = new Note(note.getId(), note.getOrderNo(), color, note.isPinned(),
                note.getTitle(), note.getText(), note.getTimestamp());
        if (notesListAdapter.updateNote(note, updated)) {
            return updated;
        }
        return new Note(-1, -1, -1, false, "", "", 0);
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
        if (notesListViewMvc.isRightSideContainerVisible())
            return;
        final boolean isHighlighted = highlightedNotes.containsKey(note.getId());
        if (isHighlighted) {
            highlightedNotes.remove(note.getId());
            onNoteRemovedFromHighlighted();
        } else {
            highlightedNotes.put(note.getId(), note);
            onNoteAddedToHighlighted();
        }
        setNoteItemBackground(note, noteItemView);
    }

    private void onNoteRemovedFromHighlighted() {
        if (highlightedNotes.isEmpty()) {
            actionBarMvc.setDeleteMenuItemVisible(false);
            actionBarMvc.setSelectAllMenuItemVisible(false);
            actionBarMvc.setHomeButtonVisible(false);
            actionBarMvc.setTitle(getString(R.string.your_notes_title));
            notesListViewMvc.showAddButton();
        } else {
            actionBarMvc.setTitle(String.valueOf(highlightedNotes.size()));
        }
    }

    private void onNoteAddedToHighlighted() {
        if (highlightedNotes.size() == 1) {
            actionBarMvc.setDeleteMenuItemVisible(true);
            actionBarMvc.setHomeButtonVisible(true);
            actionBarMvc.setSelectAllMenuItemVisible(true);
            notesListViewMvc.hideAddButton();
        }
        actionBarMvc.setTitle(String.valueOf(highlightedNotes.size()));
    }

    @Override
    public void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView) {
        noteItemView.setText(note.getText());
        noteItemView.setTitle(note.getTitle());
        setNoteItemBackground(note, noteItemView);
    }

    @Override
    public void onNoteItemPositionChanged(Note note, int posFrom, int posTo) {
        backgroundPoster.post(() -> reorderNotesDueToNotePositionChange(note, posFrom, posTo));
    }

    private void reorderNotesDueToNotePositionChange(Note note, int posFrom, int posTo) {
        if (posFrom != posTo) {
            int startIndex = Math.min(posFrom, posTo);
            int endIndex = Math.max(posFrom, posTo);
            int orderNumberChange = posFrom < posTo ? -1 : +1;
            List<Note> notes = notesListAdapter.getListOfAllNotes();
            List<Note> reordered = getNotesInRangeWithUpdatedOrderNoByAnAmount(notes, startIndex,
                    endIndex, orderNumberChange);
            Note updatedNote = changeNoteOrderNumberTo(note, posTo + 1);
            if (posTo < posFrom)
                reordered.set(0, updatedNote);
            else
                reordered.set(reordered.size() - 1, updatedNote);
            updateNotesInDatabase(reordered, UPDATE_ORDERING);
        }
    }

    private void setNoteItemBackground(Note note, NotesListItemViewMvc noteItemView) {
        final boolean isHighlighted = highlightedNotes.containsKey(note.getId());
        final int highlightColor = isHighlighted ? COLOR_HIGHLIGHTED_FRAME : note.getColor();
        noteItemView.setBackgroundColors(highlightColor, note.getColor());
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
        if (note.getId() != -1) {
            updateOperationFlag = UPDATE_ORDERING;
            backgroundPoster.post(() -> updateOrderNumbersOfNotesStartingWithNote(note));
        }
        else
            notesManager.fetchNotes();
    }

    private void updateOrderNumbersOfNotesStartingWithNote(Note newNote) {
        List<Note> notes = notesListAdapter.getListOfAllNotes();
        if (notes.isEmpty() || !notes.get(0).equals(newNote))
            notes.add(0, newNote);
        List<Note> updatedNotes =
                getNotesInRangeWithUpdatedOrderNoByAnAmount(notes, 0, notes.size() - 1, 1);
        updateNotesInDatabase(updatedNotes, UPDATE_ORDERING);
    }

    private List<Note> getNotesInRangeWithUpdatedOrderNoByAnAmount(List<Note> notes, int startInc,
                                                                   int endInc, int amount) {
        if (endInc < startInc)
            throw new IllegalArgumentException("end index must be greater than start index");

        List<Note> updatedNotes = Lists.newArrayListWithCapacity(endInc - startInc + 1);
        for (int i = startInc; i <= endInc; ++i) {
            Note oldNote = notes.get(i);
            updatedNotes.add(changeNoteOrderNumberTo(oldNote, oldNote.getOrderNo() + amount));
        }
        return updatedNotes;
    }

    private Note changeNoteOrderNumberTo(@NonNull Note note, int orderNumber) {
        return new Note(
                note.getId(),
                orderNumber,
                note.getColor(),
                note.isPinned(),
                note.getTitle(),
                note.getText(),
                note.getTimestamp()
        );
    }

    @Override public void onNoteUpdated(@NonNull Note note) {
        if (updateOperationFlag == UPDATE_COLOR) {
            notesListAdapter.updateNote(note, note); // it updates based on the ID
            notesListAdapter.notifyNoteChanged(note);
        }
    }

    @Override
    public void onMultipleNotesUpdated(@NonNull List<Note> notes) {
        if (updateOperationFlag == UPDATE_ORDERING) {
            showToast(R.string.note_saved_message, "");
            notesListAdapter.replaceNotesStartingFrom(notes, notes.get(0).getOrderNo() -1);
            notesListAdapter.updateDataSet();
        } else if (updateOperationFlag == UPDATE_COLOR) {
            notesListAdapter.updateDataSet();
        }
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


    /* ************************************************************************
     *                 ColorPickerAdapterListener methods
     * ************************************************************************/

    @Override
    public void onColorClicked(@ColorInt int color) {
        selectedNoteColor = color;
        if (!highlightedNotes.isEmpty()) {
            updateOperationFlag = UPDATE_COLOR;
            backgroundPoster.post(() -> updateHighlightedNotesiWithColor(color));
        }
    }

    private void updateHighlightedNotesiWithColor(@ColorInt int color) {
        final List<Note> toUpdate = Lists.newArrayListWithCapacity(highlightedNotes.size());
        highlightedNotes.forEach((id, note) -> toUpdate.add(createColorUpdatedNote(note, color)));
        notesManager.updateNotes(toUpdate);
    }


    /* ************************************************************************
     *                          OTHER methods
     * ************************************************************************/

    private void updateNoteInDatabase(Note note, int updateFlag) {
        mainThreadPoster.post(() -> {
            updateOperationFlag = updateFlag;
            notesManager.updateNote(note);
        });
    }

    private void updateNotesInDatabase(List<Note> notes, int updateFlag) {
        mainThreadPoster.post(() -> {
            updateOperationFlag = updateFlag;
            notesManager.updateNotes(notes);
        });
    }
}