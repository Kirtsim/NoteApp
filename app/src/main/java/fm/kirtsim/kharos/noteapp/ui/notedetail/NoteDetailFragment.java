package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dummy.DummyFragment;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.utils.DateUtils;
import fm.kirtsim.kharos.noteapp.utils.StringUtils;

import static fm.kirtsim.kharos.noteapp.Constants.NOTE_TITLE_MAX_LETTER_COUNT;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDetailFragment extends BaseFragment implements
        NoteDetailViewMvc.NoteDetailViewMvcListener {

    public static final String ARG_NOTE_ID = "DETAIL_NOTE_ID";
    public static final String ARG_NOTE_TITLE = "DETAIL_NOTE_TITLE";
    public static final String ARG_NOTE_TEXT = "DETAIL_NOTE_TEXT";
    public static final String ARG_NOTE_TIME = "DETAIL_NOTE_TIMESTAMP";

    @Inject NotesManager notesManager;
    @Inject NoteDetailActionBarViewMvc actionBarView;
    private NoteDetailViewMvc viewMvc;

    private int noteId = -1;
    private long timestamp = -1;

    private boolean isTitleDefault = true;
    private boolean isTextDefault = true;

    private @ColorInt int userTextColor, defaultTextColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initNoteDetailsFromArguments(getArguments());
        initializeTextColorAttributes();
    }

    private void initNoteDetailsFromArguments(Bundle arguments) {
        if (arguments != null) {
            noteId = arguments.getInt(ARG_NOTE_ID, noteId);
            timestamp = arguments.getLong(ARG_NOTE_TIME, timestamp);
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
        if (savedInstanceState == null) {
            displayNoteDetailsFromArguments(getArguments());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        actionBarView.setMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_save:
                startNewFragment(DummyFragment.class, null, null, true);
                Note note = createNoteFromDetails();
                if (!saveNote(note))
                    deleteNote(note);
                popFromBackStack();
                break;
            case R.id.mi_delete:
                deleteNote(createNoteFromDetails());
                popFromBackStack();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void displayNoteDetailsFromArguments(Bundle arguments) {
        if (noteId == -1) {
            setDefaultNoteDetails();
        } else {
            assignValuesFromArguments(arguments);
            if (!validateNoteTitle()) {
                viewMvc.setNoteTitle(getString(R.string.default_title));
                viewMvc.setTitleColor(defaultTextColor);
            }
            if (!validateNoteText()) {
                viewMvc.setNoteText(getString(R.string.default_text));
                viewMvc.setTextColor(defaultTextColor);
            }
        }
    }

    private void setDefaultNoteDetails() {
        viewMvc.setNoteDateAndTime(
                DateUtils.getDateAndTimeStringFromTimeStamp(System.currentTimeMillis()));
        viewMvc.setTitleColor(defaultTextColor);
        viewMvc.setTextColor(defaultTextColor);
        viewMvc.setNoteTitle(getString(R.string.default_title));
        viewMvc.setNoteText(getString(R.string.default_text));
    }

    private void assignValuesFromArguments(Bundle arguments) {
        if (arguments != null) {
            viewMvc.setNoteTitle(arguments.getString(ARG_NOTE_TITLE, ""));
            viewMvc.setNoteText(arguments.getString(ARG_NOTE_TEXT, ""));
            long time = arguments.getLong(ARG_NOTE_TIME, System.currentTimeMillis());
            viewMvc.setNoteDateAndTime(DateUtils.getDateAndTimeStringFromTimeStamp(time));
        }
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
        // TODO: default text gets saved instead of leaving the note body empty
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
        return new Note(noteId, noteTitle, noteText, time);
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
            viewMvc.setTitleColor(defaultTextColor);
            viewMvc.setNoteTitle(getString(R.string.default_title));
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
            viewMvc.setTextColor(defaultTextColor);
            viewMvc.setNoteText(getString(R.string.default_text));
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
        popFromBackStack();
        return true;
    }
}
