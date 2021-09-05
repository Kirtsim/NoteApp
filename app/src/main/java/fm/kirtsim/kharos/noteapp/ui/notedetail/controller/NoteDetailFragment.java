package fm.kirtsim.kharos.noteapp.ui.notedetail.controller;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.dataholder.TemporaryDataHolder;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc.NoteDetailActionBarViewMvc;

import fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc.NoteDetailViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc.NoteDetailViewMvcImpl;
import fm.kirtsim.kharos.noteapp.utils.DateUtils;
import fm.kirtsim.kharos.noteapp.utils.StringUtils;

import static fm.kirtsim.kharos.noteapp.Constants.NOTE_TITLE_MAX_LETTER_COUNT;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDetailFragment extends BaseFragment implements
        NoteDetailViewMvc.NoteDetailViewMvcListener {

    public static final String ARG_NOTE_ID = "DETAIL_NOTE_ID";
    public static final String ARG_NOTE_ORDER_NO = "DETAIL_NOTE_ORDER_NO";
    public static final String ARG_NOTE_COLOR = "DETAIL_NOTE_COLOR";
    public static final String ARG_NOTE_PINNED = "DETAIL_NOTE_PINNED";
    public static final String ARG_NOTE_TITLE = "DETAIL_NOTE_TITLE";
    public static final String ARG_NOTE_TEXT = "DETAIL_NOTE_TEXT";
    public static final String ARG_NOTE_TIME = "DETAIL_NOTE_TIMESTAMP";

    private static final String ARG_DEFAULT_TITLE = "DETAIL_NOTE_DEF_TITLE";
    private static final String ARG_DEFAULT_TEXT = "DETAIL_NOTE_DEF_TEXT";
    private static final String ARG_ACTIONBAR_STATE = "DETAIL_NOTE_ACTIONBAR_STATE";

    @Inject NotesManager notesManager;
    @Inject
    NoteDetailActionBarViewMvc actionBarView;
    private NoteDetailViewMvc viewMvc;

    private int noteId = -1;
    private int orderNo = 0;
    private int color = Color.WHITE;
    private boolean pinned = false;
    private long timestamp = -1;

    private TemporaryDataHolder tempData;

    private boolean isTitleDefault = true;
    private boolean isTextDefault = true;

    private @ColorInt int userTextColor, defaultTextColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initializeTextColorAttributes();
        Bundle noteDetails = savedInstanceState == null ? getArguments() : savedInstanceState;
        tempData = new TemporaryDataHolder();
        initFromBundle(noteDetails);
    }

    private void initFromBundle(Bundle arguments) {
        if (arguments != null) {
            noteId = arguments.getInt(ARG_NOTE_ID, noteId);
            orderNo = arguments.getInt(ARG_NOTE_ORDER_NO, orderNo);
            color = arguments.getInt(ARG_NOTE_COLOR, color);
            pinned = arguments.getBoolean(ARG_NOTE_PINNED, pinned);
            timestamp = arguments.getLong(ARG_NOTE_TIME, timestamp);

            final boolean isNoteNew = noteId != -1;
            isTextDefault = arguments.getBoolean(ARG_DEFAULT_TEXT, isNoteNew);
            isTitleDefault = arguments.getBoolean(ARG_DEFAULT_TITLE, isNoteNew);
        }
    }

    private void initializeTextColorAttributes() {
        userTextColor = ResourcesCompat.getColor(getResources(), R.color.user_text_color, null);
        defaultTextColor = ResourcesCompat.
                getColor(getResources(), R.color.default_text_color, null);
    }

    @Override
    protected boolean performInjection(ControllerComponent component) {
        component.inject(this);
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (viewMvc == null) {
            viewMvc = new NoteDetailViewMvcImpl(inflater, container);
            viewMvc.registerListener(this);
        }
        return viewMvc.getRootView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            viewMvc.initFromSavedState(savedInstanceState);
        } else if (noteId != -1) {
            displayNoteDetailsFromBundle(getArguments());
        } else
            setDefaultNoteDetails();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_NOTE_ID, noteId);
        outState.putInt(ARG_NOTE_COLOR, color);
        outState.putInt(ARG_NOTE_ORDER_NO, orderNo);
        outState.putLong(ARG_NOTE_TIME, timestamp);
        outState.putBoolean(ARG_NOTE_PINNED, pinned);
        outState.putBoolean(ARG_DEFAULT_TITLE, isTitleDefault);
        outState.putBoolean(ARG_DEFAULT_TEXT, isTextDefault);
        outState.putBundle(ARG_ACTIONBAR_STATE, actionBarView.getState());
        viewMvc.getState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        actionBarView.setMenu(menu, inflater);
        actionBarView.initializeFromSavedState(tempData.getData(ARG_ACTIONBAR_STATE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_save:
                Note note = createNoteFromDetails();
                if (!saveNote(note))
                    deleteNote(note);
                closeKeyboardAndPop();
                break;
            case R.id.mi_delete:
                deleteNote(createNoteFromDetails());
                closeKeyboardAndPop();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void displayNoteDetailsFromBundle(Bundle arguments) {
        if (arguments != null) {
            viewMvc.setNoteText(arguments.getString(ARG_NOTE_TEXT));
            viewMvc.setNoteTitle(arguments.getString(ARG_NOTE_TITLE));
            viewMvc.setNoteDateAndTime(DateUtils.getDateAndTimeStringFromTimeStamp(timestamp));

            if (!validateNoteTitle())
                displayDefaultTitle();
            if (!validateNoteText())
                displayDefaultText();
        }
    }

    private void setDefaultNoteDetails() {
        viewMvc.setNoteDateAndTime(
                DateUtils.getDateAndTimeStringFromTimeStamp(System.currentTimeMillis()));
        displayDefaultText();
        displayDefaultTitle();
    }

    private void displayDefaultText() {
        viewMvc.setTextColor(defaultTextColor);
        viewMvc.setNoteText(getString(R.string.default_text));
    }

    private void displayDefaultTitle() {
        viewMvc.setTitleColor(defaultTextColor);
        viewMvc.setNoteTitle(getString(R.string.default_title));
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        notesManager = null;

        viewMvc.unregisterListener(this);
        viewMvc = null;
    }

    private boolean saveNote(Note note) {
        if (note == null) return false;
        timestamp = note.getTimestamp();
        if (noteId == -1) {
            notesManager.addNewNote(note);
        } else {
            notesManager.updateNote(note);
        }
        return true;
    }

    @Nullable
    private Note createNoteFromDetails() {
        validateNoteTitle();
        validateNoteText();
        if (isTitleDefault && isTextDefault) return null;

        String noteTitle = viewMvc.getTitle();
        String noteText = isTextDefault ? "" : viewMvc.getText();
        final long time = createModificationTime();
        if (isTitleDefault) {
            noteTitle = StringUtils.extractFirstWordsUpToLength(noteText,
                    NOTE_TITLE_MAX_LETTER_COUNT);
            if (noteTitle.isEmpty())
                noteTitle = DateUtils.getDateStringFromTimestamp(time);
        }
        return new Note(noteId, orderNo, color, pinned, noteTitle, noteText, time);
    }

    private long createModificationTime() {
        Bundle args = getArguments();
        if (hasNoteBeenModified())
            return System.currentTimeMillis();
        return args != null ? args.getLong(ARG_NOTE_TIME) : System.currentTimeMillis();
    }

    private boolean hasNoteBeenModified() {
        if (noteId == -1) return true;
        final Bundle args = getArguments();
        return args == null || !viewMvc.getTitle().equals(args.getString(ARG_NOTE_TITLE)) ||
                !viewMvc.getText().equals(args.getString(ARG_NOTE_TEXT));
    }

    private void deleteNote(Note note) {
        if (note != null) {
            notesManager.removeNote(note);
        } else {
            showToast(R.string.note_discarded_message);
        }
    }


    /* ****************************************************
     *          NoteDetailMvcViewListener methods
     * ****************************************************/
    @Override
    public void onNoteTitleFocusChanged(boolean hasFocus) {
        validateNoteTitle();
        if (!hasFocus && isTitleDefault) {
            displayDefaultTitle();
        } else if (isTitleDefault) {
            viewMvc.setNoteTitle("");
            viewMvc.setTitleColor(userTextColor);
        }
    }

    private boolean validateNoteTitle() {
        String title = viewMvc.getTitle();
        int color = viewMvc.getTitleColor();
        isTitleDefault =
                title == null || title.isEmpty() || StringUtils.getFirstWordIndex(title) == -1 ||
                color == defaultTextColor;
        return !isTitleDefault;
    }

    @Override
    public void onNoteTextFocusChanged(boolean hasFocus) {
        validateNoteText();
        if (!hasFocus && isTextDefault) {
            displayDefaultText();
        } else if (isTextDefault) {
            viewMvc.setNoteText("");
            viewMvc.setTextColor(userTextColor);
        }
    }

    private boolean validateNoteText() {
        String text = viewMvc.getText();
        int color = viewMvc.getTextColor();
        isTextDefault = (text == null || text.isEmpty()) || color == defaultTextColor;
        return !isTextDefault;
    }

    @Override
    protected boolean onBackPressed() {
        Note note = createNoteFromDetails();
        if (!saveNote(note))
            deleteNote(note);
        closeKeyboardAndPop();
        return true;
    }

    private void closeKeyboardAndPop() {
        viewMvc.clearAllFocus();
        popFromBackStack();
    }
}
