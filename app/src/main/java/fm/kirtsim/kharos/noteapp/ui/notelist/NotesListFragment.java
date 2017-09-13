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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.List;

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
import fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator.AdapterNotesListCoordinator;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerViewMvc;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.ColorPickerItemDecoration;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.NotesListItemDecorationImpl;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ColorPaletteNonSelectionActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ColorPaletteSelectionActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.HomeActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.NotesListItemViewMvcDebug;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ReorderActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.SelectionActionbarVisual;
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
    @Inject ColorPickerAdapter colorListAdapter;
    @Inject NotesManager notesManager;
    @Inject NotesListActionbarManager actionbarManager;
    @Inject MainThreadPoster mainThreadPoster;
    @Inject BackgroundThreadPoster backgroundPoster;


    private State state;
    private NotesListViewMvc notesListViewMvc;
    private AdapterNotesListCoordinator notesCoordinator;
    @SuppressWarnings("FieldCanBeLocal")
    private ColorPickerViewMvc colorPickerViewMvc;

    private Animations noteDetailAnimations;
    private NotesListItemTouchHelper notesTouchHelper;

    private int selectedNoteColor;
    private int COLOR_HIGHLIGHTED_FRAME;

    private int updateOperationFlag;

    private static final int UPDATE_ORDERING = 1;
    private static final int UPDATE_COLOR = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(getClassName(), "onCreate()");
        super.onCreate(savedInstanceState);
        initializeColors(getResources());
        setHasOptionsMenu(true);
        state = new State();

        notesCoordinator = notesListAdapter.getNotesCoordinator();
        notesListAdapter.setListener(this);
        colorListAdapter.setListener(this);
        colorListAdapter.setColors(getContext().getResources()
                .getIntArray(R.array.color_picker_colors));
        colorListAdapter.setHighlightColor(Color.BLACK);
        notesManager.registerListener(this);
        noteDetailAnimations = getAnimationsForNoteDetailFragment();
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
        setupColorPicker(inflater);
        setupViewMvc(inflater, container);
        setupNotesListItemTouchHelper();
        return notesListViewMvc.getRootView();
    }

    private void setupViewMvc(LayoutInflater inflater, @Nullable ViewGroup container) {
        notesListViewMvc = new NotesListViewMvcImpl(inflater, container, notesListAdapter,
                new LinearLayoutManager(inflater.getContext()));
        notesListViewMvc.registerListener(this);
        notesListViewMvc.addNoteItemDecoration(new NotesListItemDecorationImpl(
                Units.dp2px(3, getResources().getDisplayMetrics())));
        notesListViewMvc.addViewToRightSideContainer(colorPickerViewMvc.getRootView());
    }

    private void setupColorPicker(LayoutInflater inflater) {
        colorPickerViewMvc = new ColorPickerViewMvcImpl(inflater, null);
        colorPickerViewMvc.setLayoutManager(new LinearLayoutManager(getContext()));
        colorPickerViewMvc.setAdapter(colorListAdapter);
        colorPickerViewMvc.addColorItemsDecoration(new ColorPickerItemDecoration(
                Units.dp2px(10, getResources().getDisplayMetrics())));
    }

    private void setupNotesListItemTouchHelper() {
        notesTouchHelper = new NotesListItemTouchHelper(
                new NotesListItemTouchCallback(notesListAdapter), null);
        notesTouchHelper.attachToRecyclerView(notesListViewMvc.getRecyclerView());
        notesTouchHelper.listen(false);
    }

    public void onStart() {
        super.onStart();
        notesManager.fetchNotes();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        colorListAdapter.unregisterListener();

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
        actionbarManager.inflateWithMenu(menu, inflater);
        actionbarManager.applyVisual(new HomeActionbarVisual(), getString(R.string.your_notes_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_delete: deleteHighlightedNotes();break;
            case R.id.mi_select_all: addAllNotesToHighlighted(); break;
            case R.id.mi_color_picker: onColorPickerIconClicked(); break;
            case R.id.mi_reorder: onReorderIconClicked(); break;
            case android.R.id.home: onHomeBackButtonClicked(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void onColorPickerIconClicked() {
        notesListViewMvc.showRightSideContainer();
        if (state.isInSelectionState()) {
            actionbarManager.applyVisual(new ColorPaletteSelectionActionbarVisual(),
                    getString(R.string.title_color_selected));
            state.setState(State.SELECTION_COLOR);
        } else {
            actionbarManager.applyVisual(new ColorPaletteNonSelectionActionbarVisual());
            actionbarManager.setTitle(getString(R.string.title_color_notes));
            state.setState(State.COLOR);
        }
    }

    private void onReorderIconClicked() {
        state.setState(State.REORDER);
        actionbarManager.applyVisual(new ReorderActionbarVisual());
        notesListViewMvc.hideAddButton();
        notesTouchHelper.listen(true);
    }

    private void onHomeBackButtonClicked() {
        clearNoteHighlighting();
        if (notesListViewMvc.isRightSideContainerVisible())
            notesListViewMvc.hideRightSideContainer();


        state.setState(State.DEFAULT);
    }

    private void deleteHighlightedNotes() {
        List<Note> notes = notesCoordinator.getListOfHighlightedNotes();
        notesManager.removeNotes(notes);
    }

    private void addAllNotesToHighlighted() {
        notesCoordinator.addAllNotesToHighlighted();
        onNoteAddedToHighlighted();
        notesListAdapter.updateDataSet();
    }

    private void clearNoteHighlighting() {
        notesCoordinator.removeAllNotesFromHighlighted();
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
        if (state.isInDefaultState())
            return false;
        if (state.isInColoringState() || state.isInSelectionColoringState()) {
            notesListViewMvc.hideRightSideContainer();
        } else if (state.isInSelectionState()) {
            onBackPressedInSelectionState();
        } else if (state.isInReorderState())
            notesListViewMvc.showAddButton();
        setStateAfterBackPress();
        return true;
    }

    private void onBackPressedInSelectionState() {
        actionbarManager.applyVisual(new HomeActionbarVisual(), getString(R.string.your_notes_title));
        notesTouchHelper.listen(false);
        clearNoteHighlighting();
        state.setState(State.DEFAULT);
    }

    private void setStateAfterBackPress() {
        if (state.isInSelectionColoringState())
            state.setState(State.SELECTION);
        else
            state.setState(State.DEFAULT);
    }

    // ###################### BaseFragment ########################


    /* ************************************************************************
     *                NoteListAdapterListener methods
     * ************************************************************************/

    @Override
    public void onNoteItemSingleClicked(Note note, NotesListItemViewMvc noteItemView,
                                        int notePosition) {
        if (state.isInDefaultState()) {
            displayDetailsOfNote(note);
        } else if (state.isInSelectionState() || state.isInSelectionColoringState())
            onNoteItemLongClicked(note, noteItemView, notePosition);
        else if (state.isInColoringState()) {
            updateNoteInDatabase(createColorUpdatedNote(note, selectedNoteColor), UPDATE_COLOR);
        }
    }

    private Note createColorUpdatedNote(Note note, @ColorInt int color) {
        Note updated = new Note.NoteBuilder(note).color(color).build();
        if (notesCoordinator.updateNote(note, updated)) {
            return updated;
        }
        return Note.NoteBuilder.createDefaultNote();
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
        if (state.isInDefaultState()) {
            setNoteHighlighted(note, noteItemView, true);
        } else if (state.isInSelectionState()) {
            boolean highlightNote = !notesCoordinator.isNoteHighlighted(note);
            setNoteHighlighted(note, noteItemView, highlightNote);
        } else if (state.isInSelectionColoringState() && !notesCoordinator.isNoteHighlighted(note)) {
            updateNoteInDatabase(createColorUpdatedNote(note, selectedNoteColor), UPDATE_COLOR);
            setNoteHighlighted(note, noteItemView, true);
        }
    }

    private void setNoteHighlighted(Note note, NotesListItemViewMvc noteViewMvc, boolean highlight) {
        if (highlight) {
            notesCoordinator.addNoteToHighlighted(note);
            onNoteAddedToHighlighted();
            noteViewMvc.setBorderColor(COLOR_HIGHLIGHTED_FRAME);
        } else {
            notesCoordinator.removeNoteFromHighlighted(note);
            onNoteRemovedFromHighlighted();
            noteViewMvc.setBorderColor(note.getColor());
        }
    }

    private void onNoteRemovedFromHighlighted() {
        if (!notesCoordinator.containsHighlightedNotes()) {
            actionbarManager.applyVisual(new HomeActionbarVisual());
            actionbarManager.setTitle(getString(R.string.your_notes_title));
            notesListViewMvc.showAddButton();
            state.setState(State.DEFAULT);
        } else {
            actionbarManager.setTitle(String.valueOf(notesCoordinator.getHighlightedCount()));
        }
    }

    private void onNoteAddedToHighlighted() {
        if (notesCoordinator.getHighlightedCount() == 1) {
            actionbarManager.applyVisual(new SelectionActionbarVisual());
            notesListViewMvc.hideAddButton();
            state.setState(State.SELECTION);
        }
        actionbarManager.setTitle(String.valueOf(notesCoordinator.getHighlightedCount()));
    }

    @Override
    public void onNoteItemVisible(Note note, NotesListItemViewMvc noteItemView) {
        NotesListItemViewMvcDebug viewMvc = (NotesListItemViewMvcDebug) noteItemView;
        viewMvc.setId(note.getId());
        viewMvc.setOrderNo(note.getOrderNo());
        noteItemView.setText(note.getText());
        noteItemView.setTitle(note.getTitle());
        setNoteItemBackground(note, noteItemView);
    }

    @Override
    public void onNoteItemPositionChanged(Note note, int posFrom, int posTo) {
        backgroundPoster.post(() -> { // TODO: WRONG!!
            final int lowerIndex = Math.min(posFrom, posTo);
            updateOrderNumbersOfNotesStartingWithNote(notesCoordinator.getNoteAt(lowerIndex));
        });
    }

    private void setNoteItemBackground(Note note, NotesListItemViewMvc noteItemView) {
        final boolean isHighlighted = notesCoordinator.isNoteHighlighted(note);
        final int highlightColor = isHighlighted ? COLOR_HIGHLIGHTED_FRAME : note.getColor();
        noteItemView.setBackgroundColors(highlightColor, note.getColor());
    }
    // ###################### NoteListAdapterListener ########################


    /* ************************************************************************
     *                 NotesManagerListener methods
     * ************************************************************************/

    @Override
    public void onNotesFetched(@NonNull List<Note> notes) {
        Log.d(getClassName(), "onNotesFetched()");
        if (state.isInStartState()) {
            Log.d(getClassName(), "onNotesFetched() : was in default state");
            notesCoordinator.setNewNotesList(notes);
            notesListAdapter.updateDataSet();
            state.setState(State.DEFAULT);
        }
    }

    @Override public void onNewNoteAdded(@NonNull Note note) {
        Log.d(getClassName(), "onNewNoteAdded()");
        if (note.getId() != -1) {
            backgroundPoster.post(() -> updateOrderNumbersOfNotesStartingWithNote(note));
        } else
            notesManager.fetchNotes();
        state.setState(State.DEFAULT);
    }

    private void updateOrderNumbersOfNotesStartingWithNote(Note newNote) {
        List<Note> notes = notesCoordinator.getListOfAllNotes();
        if (notes.isEmpty() || !notes.get(0).equals(newNote))
            notes.add(0, newNote);
        NotesReorderer reorderer = new NotesReorderer(notes);
        reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(0, 1);
        updateNotesInDatabase(reorderer.getUpdatedNotes(), UPDATE_ORDERING);
    }

    @Override public void onNoteUpdated(@NonNull Note note) {
        if (updateOperationFlag == UPDATE_COLOR) {
            notesCoordinator.updateNote(note, note); // it updates based on the ID
            notesListAdapter.notifyNoteChanged(note);
        }
    }

    @Override
    public void onMultipleNotesUpdated(@NonNull List<Note> notes) {
        if (updateOperationFlag == UPDATE_ORDERING) {
            showToast(R.string.note_saved_message, "");
            notesCoordinator.replaceNotesStartingFrom(notes, notes.get(0).getOrderNo() -1);
            notesListAdapter.updateDataSet();
        } else if (updateOperationFlag == UPDATE_COLOR) {
            notesListAdapter.updateDataSet();
        }
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        backgroundPoster.post(() -> {
            clearNoteHighlighting();
            if (notesCoordinator.removeNote(note)) {
                int deletionIndex = notesCoordinator.popLastDeletedNoteAndItsIndex().second;
                NotesReorderer reorderer = new NotesReorderer(notesCoordinator.getListOfAllNotes());
                reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(deletionIndex,
                        note.getOrderNo());
                updateNotesInDatabase(reorderer.getUpdatedNotes(), UPDATE_ORDERING);
//                notesListAdapter.notifyNoteRemoved(note);
                mainThreadPoster.post(() -> showToast(R.string.note_deleted_message, ""));
            } else
                notesManager.fetchNotes();
        });
        state.setState(State.DEFAULT);
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        clearNoteHighlighting();
        if (notesCoordinator.removeNotes(notes)) {
            NotesReorderer reorderer = new NotesReorderer(notesCoordinator.getListOfAllNotes());
            reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(0, 1);
            updateNotesInDatabase(reorderer.getUpdatedNotes(), UPDATE_ORDERING);

//            notesListAdapter.updateDataSet(); // TODO: reordering!
            showToast(R.string.note_deleted_message, notes.size() == 1 ? "" : "s");
        } else
            notesManager.fetchNotes();
        state.setState(State.DEFAULT);
    }
    // ###################### NotesManagerListener ########################


    /* ************************************************************************
     *                 NotesListViewMvcListener methods
     * ************************************************************************/

    @Override
    public void onNewNoteRequested() {
        startNewFragment(NoteDetailFragment.class, null, noteDetailAnimations, true);
        state.setState(State.NEW_NOTE);
    }


    /* ************************************************************************
     *                 ColorPickerAdapterListener methods
     * ************************************************************************/

    @Override
    public void onColorClicked(@ColorInt int color) {
        selectedNoteColor = color;
        if (notesCoordinator.containsHighlightedNotes()) {
            updateOperationFlag = UPDATE_COLOR;
            backgroundPoster.post(() -> updateHighlightedNotesWithColor(color));
        }
    }

    private void updateHighlightedNotesWithColor(@ColorInt int color) {
        final List<Note> highlighted = notesCoordinator.getListOfHighlightedNotes();
        final List<Note> toUpdate = Lists.newArrayListWithCapacity(highlighted.size());
        highlighted.forEach(note -> toUpdate.add(createColorUpdatedNote(note, color)));
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