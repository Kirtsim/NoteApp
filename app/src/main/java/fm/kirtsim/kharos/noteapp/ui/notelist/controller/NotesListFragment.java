package fm.kirtsim.kharos.noteapp.ui.notelist.controller;

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

import java.util.List;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.dataholder.TemporaryDataHolder;
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
import fm.kirtsim.kharos.noteapp.ui.notedetail.controller.NoteDetailFragment;
import fm.kirtsim.kharos.noteapp.ui.notelist.manager.NotesListActionbarManager;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesReorderer;
import fm.kirtsim.kharos.noteapp.ui.notelist.State;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ColorPaletteNonSelectionActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ColorPaletteSelectionActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.HomeActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.ReorderActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.SelectionActionbarVisual;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc.NotesListViewMvcImpl;
import fm.kirtsim.kharos.noteapp.utils.ListUtils;
import fm.kirtsim.kharos.noteapp.utils.Units;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListFragment extends BaseFragment implements
        NotesListAdapterImpl.NotesListAdapterListener,
        NotesListViewMvc.NotesListViewMvcListener,
        NotesManager.NotesManagerListener,
        ColorPickerAdapter.ColorPickerAdapterListener {

    private static final String ARG_IDS_HIGHLIGHTED = "NOTES_LIST_IDS_HIGHLIGHTED";
    private static final String ARG_STATE = "NOTES_LIST_STATE";
    private static final String ARG_SELECTED_NOTE_COLOR = "NOTES_LIST_SELECTED_COLOR";
    private static final String ARG_ACTIONBAR_STATE = "NOTES_LIST_ACTIONBAR_STATE";
    private static final String ARG_NOTES_LIST_SCROLL = "NOTES_LIST_SCROLL";
    private static final String ARG_SELECTED_COLOR_POSITION = "NOTES_LIST_COLOR_POSITION";

    @Inject NotesListAdapter notesListAdapter;
    @Inject ColorPickerAdapter colorListAdapter;
    @Inject NotesManager notesManager;
    @Inject
    NotesListActionbarManager actionbarManager;
    @Inject MainThreadPoster mainThreadPoster;
    @Inject BackgroundThreadPoster backgroundPoster;


    private State state;
    private NotesListViewMvc notesListViewMvc;
    private AdapterNotesListCoordinator notesCoordinator;
    @SuppressWarnings("FieldCanBeLocal")
    private ColorPickerViewMvc colorPickerViewMvc;

    private Animations noteDetailAnimations;
    private NotesListItemTouchHelper notesTouchHelper;
    private TemporaryDataHolder temporaryData;

    private int selectedNoteColor;
    private int COLOR_HIGHLIGHTED_FRAME;

    private int updateOperationFlag;

    private static final int UPDATE_ORDERING = 1;
    private static final int UPDATE_COLOR = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        onCreateInitialization(savedInstanceState);
    }

    private void onCreateInitialization(Bundle savedState) {
        initializeColors(getResources());
        notesCoordinator = notesListAdapter.getNotesCoordinator();
        notesListAdapter.setListener(this);
        colorListAdapter.setListener(this);
        colorListAdapter.setColors(getContext().getResources()
                .getIntArray(R.array.color_picker_colors));
        colorListAdapter.setHighlightColor(Color.BLACK);
        notesManager.registerListener(this);
        noteDetailAnimations = getAnimationsForNoteDetailFragment();
        state = savedState == null ? new State() : savedState.getParcelable(ARG_STATE);
        temporaryData = new TemporaryDataHolder();
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
        setupColorPicker(inflater, savedInstanceState);
        setupViewMvc(inflater, container, savedInstanceState);
        setupNotesListItemTouchHelper();
        return notesListViewMvc.getRootView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedState) {
        super.onActivityCreated(savedState);
        if (savedState != null) {
            final int[] ids = savedState.getIntArray(ARG_IDS_HIGHLIGHTED);
            temporaryData.putData(ARG_NOTES_LIST_SCROLL, savedState.getInt(ARG_NOTES_LIST_SCROLL));
            temporaryData.putData(ARG_ACTIONBAR_STATE, savedState.getBundle(ARG_ACTIONBAR_STATE));
            temporaryData.putData(ARG_IDS_HIGHLIGHTED, ids);
            colorPickerViewMvc.initFromSavedState(savedState);
            colorListAdapter.setHighlightedColorPosition(
                    savedState.getInt(ARG_SELECTED_COLOR_POSITION));

            notesListViewMvc.initFromSavedState(savedState);
        }
    }

    private void setupViewMvc(LayoutInflater inflater, @Nullable ViewGroup container,
                              Bundle savedState) {
        notesListViewMvc = new NotesListViewMvcImpl(inflater, container, notesListAdapter,
                new LinearLayoutManager(inflater.getContext()));
        notesListViewMvc.registerListener(this);
        notesListViewMvc.addNoteItemDecoration(new NotesListItemDecorationImpl(
                Units.dp2px(3, getResources().getDisplayMetrics())));
        notesListViewMvc.addViewToRightSideContainer(colorPickerViewMvc.getRootView());
        notesListViewMvc.initFromSavedState(savedState);
    }

    private void setupColorPicker(LayoutInflater inflater, Bundle savedState) {
        colorPickerViewMvc = new ColorPickerViewMvcImpl(inflater, null);
        colorPickerViewMvc.setLayoutManager(new LinearLayoutManager(getContext()));
        colorPickerViewMvc.setAdapter(colorListAdapter);
        colorPickerViewMvc.addColorItemsDecoration(new ColorPickerItemDecoration(
                Units.dp2px(10, getResources().getDisplayMetrics())));
        colorPickerViewMvc.initFromSavedState(savedState);
    }

    private void setupNotesListItemTouchHelper() {
        notesTouchHelper = new NotesListItemTouchHelper(
                new NotesListItemTouchCallback(notesListAdapter), null);
        notesTouchHelper.attachToRecyclerView(notesListViewMvc.getRecyclerView());
        notesTouchHelper.listen(state.isInReorderState());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        state.setWasRestored(true);
        notesListViewMvc.getState(outState);
        colorPickerViewMvc.getState(outState);
        outState.putInt(ARG_SELECTED_NOTE_COLOR, selectedNoteColor);
        outState.putParcelable(ARG_STATE, state);
        final int [] ids = ListUtils.extractNoteIdsIntoArray(
                notesCoordinator.getListOfHighlightedNotes());
        outState.putIntArray(ARG_IDS_HIGHLIGHTED, ids);
        temporaryData.putData(ARG_IDS_HIGHLIGHTED, ids); // sometimes the fragment does not get destroyed and its attributes stay alive
        final Bundle actionBarState = actionbarManager.getActionBarState();
        outState.putBundle(ARG_ACTIONBAR_STATE, actionBarState);
        temporaryData.putData(ARG_ACTIONBAR_STATE, actionBarState);

        outState.putInt(ARG_SELECTED_COLOR_POSITION, colorListAdapter.getHighlightedColorPosition());
        outState.putInt(ARG_NOTES_LIST_SCROLL, notesListViewMvc.getRecyclerView().getScrollPosition());
    }

    public void onStart() {
        super.onStart();
        if (state == null || state.isInStartState() || state.wasRestored())
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
        Bundle savedState = temporaryData.getData(ARG_ACTIONBAR_STATE);
        if (savedState != null)
            actionbarManager.initializeActionBarFromSavedState(savedState);
        else
            actionbarManager.applyVisual(new HomeActionbarVisual(),
                    getString(R.string.your_notes_title));
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
        actionbarManager.applyVisual(new ReorderActionbarVisual(),
                getString(R.string.notes_reorder_title));
        notesListViewMvc.hideAddButton();
        notesTouchHelper.listen(true);
    }

    private void onHomeBackButtonClicked() {
        onBackPressed();
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
        setStateAfterBackPressAndApplyVisual();
        return true;
    }

    private void onBackPressedInSelectionState() {
        notesTouchHelper.listen(false);
        clearNoteHighlighting();
    }

    private void setStateAfterBackPressAndApplyVisual() {
        if (state.isInSelectionColoringState()) {
            state.setState(State.SELECTION);
            actionbarManager.applyVisual(new SelectionActionbarVisual(),
                    String.valueOf(notesCoordinator.getHighlightedCount()));
        } else {
            state.setState(State.DEFAULT);
            actionbarManager.applyVisual(new HomeActionbarVisual(),
                    getString(R.string.your_notes_title));
        }
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
            if (!state.isInDefaultState()) {
                state.setState(State.DEFAULT);
                actionbarManager.applyVisual(new HomeActionbarVisual());
                actionbarManager.setTitle(getString(R.string.your_notes_title));
            }
            notesListViewMvc.showAddButton();
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
        noteItemView.setText(note.getText());
        noteItemView.setTitle(note.getTitle());
        setNoteItemBackground(note, noteItemView);
    }

    @Override
    public void onNoteItemPositionChanged(Note note, int posFrom, int posTo) {
        backgroundPoster.post(() -> { // TODO: WRONG!!
            final int lowerIndex = Math.min(posFrom, posTo);
            final int startingOrderNumber = lowerIndex + 1;
            updateNotesOrderNumberFromIndexStartingWithOrderNumber(lowerIndex, startingOrderNumber);
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
        notesCoordinator.setNewNotesList(notes);
        if (state.wasRestored()) {
            restoreNoteHighlighting();
            scrollNotesListToSavedPosition();
            state.setWasRestored(false);
        }
        notesListAdapter.updateDataSet();
        if (state.isInStartState())
            state.setState(State.DEFAULT);
    }

    private void restoreNoteHighlighting() {
        final int[] savedIds = temporaryData.getAndRemoveData(ARG_IDS_HIGHLIGHTED);
        if (savedIds != null) {
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < savedIds.length; ++i) {
                Note note = notesCoordinator.getNoteWithIdOrDefault(savedIds[i], null);
                if (note != null)
                    notesCoordinator.addNoteToHighlighted(note);
            }
        }
    }

    private void scrollNotesListToSavedPosition() {
        final Integer position = temporaryData.getAndRemoveData(ARG_NOTES_LIST_SCROLL);
        if (position != null && position != -1) {
            notesListViewMvc.getRecyclerView().scrollToPosition(position);
        }
    }

    @Override public void onNewNoteAdded(@NonNull Note note) {
        if (note.getId() != -1) {
            if (notesCoordinator.getNoteCount() == 0 || !notesCoordinator.getNoteAt(0).equals(note))
                notesCoordinator.addNote(note, 0);
            backgroundPoster.post(()
                    -> updateNotesOrderNumberFromIndexStartingWithOrderNumber(0, 1));
        } else
            notesManager.fetchNotes();
        state.setState(State.DEFAULT);
    }

    private void updateNotesOrderNumberFromIndexStartingWithOrderNumber(int indexFromInc,
                                                                        int startOrderNumber) {
        if (indexFromInc < notesCoordinator.getNoteCount()) {
            List<Note> notes = notesCoordinator.getListOfNotes(
                    indexFromInc, notesCoordinator.getNoteCount() - indexFromInc);
            NotesReorderer reorderer = new NotesReorderer(notes);
            reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(0, startOrderNumber);
            updateNotesInDatabase(reorderer.getUpdatedNotes(), UPDATE_ORDERING);
        }
    }

    @Override public void onNoteUpdated(@NonNull Note note) {
        notesCoordinator.updateNote(note, note);
        if (updateOperationFlag != UPDATE_ORDERING)
            notesListAdapter.notifyNoteChanged(note);
    }

    @Override
    public void onMultipleNotesUpdated(@NonNull List<Note> notes) {
        if (updateOperationFlag == UPDATE_ORDERING) {
            ListUtils.sortNoteListByOrderNumber(notes);
            final int replaceFromIndex = notes.get(0).getOrderNo() - 1;
            notesCoordinator.replaceNotes(replaceFromIndex, notes);
            notesListAdapter.updateDataSet();
        } else if (updateOperationFlag == UPDATE_COLOR) {
            notesListAdapter.updateDataSet();
        }
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        clearNoteHighlighting();
        backgroundPoster.post(() -> {
            int deletionIndex = notesCoordinator.getIndexOfNote(note);
            if (notesCoordinator.removeNote(note)) {
                updateNotesOrderNumberFromIndexStartingWithOrderNumber(deletionIndex, note.getOrderNo());
                mainThreadPoster.post(() -> showToast(R.string.note_deleted_message, ""));
            } else
                notesManager.fetchNotes();
        });
        state.setState(State.DEFAULT);
    }

    @Override
    public void onMultipleNotesDeleted(@NonNull List<Note> notes) {
        clearNoteHighlighting();
        backgroundPoster.post(() -> {
            ListUtils.sortNoteListByOrderNumber(notes);
            final int lowestDeletionIndex = notesCoordinator.getIndexOfNote(notes.get(0));
            final int lowestDeletedOrderNumber = notes.get(0).getOrderNo();
            if (notesCoordinator.removeNotes(notes)) {
                updateNotesOrderNumberFromIndexStartingWithOrderNumber(
                        lowestDeletionIndex, lowestDeletedOrderNumber);
                showToast(R.string.note_deleted_message, notes.size() == 1 ? "" : "s");
            } else
                notesManager.fetchNotes();
            state.setState(State.DEFAULT);
        });
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