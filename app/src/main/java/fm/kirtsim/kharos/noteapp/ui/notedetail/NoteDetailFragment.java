package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListFragment;
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
    private NoteDetailViewMvc viewMvc;

    private int noteId = -1;
    private long timestamp = -1;

    private boolean isTitleDefault = true;
    private boolean isTextDefault = true;

    private @ColorInt int userTextColor, defaultTextColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getControllerComponent().inject(this);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewMvc = new NoteDetailViewMvcImpl(inflater, container);
        viewMvc.registerListener(this);
        return viewMvc.getRootView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (!onSaveNoteMenuItemClicked())
                    onDeleteNoteMenuItemClicked();
                break;
            case R.id.delete: onDeleteNoteMenuItemClicked(); break;
            case android.R.id.home:
                Log.d(getClassName(), "home button clicked");
                if (!onSaveNoteMenuItemClicked())
                    onDeleteNoteMenuItemClicked();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayNoteDetailsFromArguments(getArguments());
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

    private void displayNoteDetailsFromArguments(Bundle arguments) {
        if (noteId == -1) {
            viewMvc.setTitleColor(defaultTextColor);
            viewMvc.setTextColor(defaultTextColor);
            viewMvc.setNoteText(getString(R.string.default_text));
        } else {
            String noteTitle = "", noteText = "";
            if (arguments != null) {
                noteTitle = arguments.getString(ARG_NOTE_TITLE, "");
                noteText = arguments.getString(ARG_NOTE_TEXT, "");
            }
            validateNoteTitle(noteTitle, userTextColor);
            validateNoteText(noteText, userTextColor);

            viewMvc.setNoteTitle(isTitleDefault ? getString(R.string.default_title) : noteTitle);
            viewMvc.setTitleColor(isTitleDefault ? defaultTextColor : userTextColor);
            viewMvc.setNoteText(isTextDefault ? getString(R.string.default_text) : noteText);
            viewMvc.setTextColor(isTextDefault ? defaultTextColor : userTextColor);
        }
    }

    private boolean onSaveNoteMenuItemClicked() {
        timestamp = System.currentTimeMillis();
        String noteTitle = viewMvc.getTitle();
        String noteText = viewMvc.getText();
        validateNoteTitle(noteTitle, viewMvc.getTitleColor());
        validateNoteText(noteText, viewMvc.getTextColor());
        if (isTextDefault && isTitleDefault)
            return false;

        if (isTitleDefault) {
            noteTitle = StringUtils.extractFirstWordsUpToLength(noteText, NOTE_TITLE_MAX_LETTER_COUNT);
            if (noteTitle.isEmpty())
                noteTitle = DateUtils.getDateStringFromTimestamp(timestamp);
        }

        Note note = new Note(noteId, noteTitle, noteText, timestamp);
        if (noteId == -1) {
            notesManager.addNewNote(note);
        } else {
            notesManager.updateNote(note);
        }
        popFromBackStack(NotesListFragment.class.getSimpleName());
        return true;
    }

    private void onDeleteNoteMenuItemClicked() {
        if (noteId != -1) {
            Note note = new Note(noteId, viewMvc.getTitle(), viewMvc.getText(), timestamp);
            notesManager.removeNote(note);
        } else {
            showToast(R.string.note_discarded_message);
        }
        popFromBackStack(NotesListFragment.class.getSimpleName());
    }


    /* ****************************************************
     *          NoteDetailMvcViewListener methods
     * ****************************************************/
    @Override
    public void onNoteTitleFocusChanged(boolean hasFocus) {
        if (!hasFocus && !validateNoteTitle(viewMvc.getTitle(), viewMvc.getTitleColor())) {
            viewMvc.setTitleColor(defaultTextColor);
            viewMvc.setNoteTitle(getString(R.string.default_title));
        } else if (isTitleDefault) {
            viewMvc.setNoteTitle("");
            viewMvc.setTitleColor(userTextColor);
        }
    }

    private boolean validateNoteTitle(String text, @ColorInt int textColor) {
        isTitleDefault =
                text == null || text.isEmpty() || StringUtils.getFirstWordIndex(text) == -1 ||
                textColor == defaultTextColor;
        return !isTitleDefault;
    }

    @Override
    public void onNoteTextFocusChanged(boolean hasFocus) {
        if (!hasFocus && !validateNoteText(viewMvc.getText(), viewMvc.getTextColor())) {
            viewMvc.setTextColor(defaultTextColor);
            viewMvc.setNoteText(getString(R.string.default_text));
        } else if (isTextDefault) {
            viewMvc.setNoteText("");
            viewMvc.setTextColor(userTextColor);
        }
    }

    private boolean validateNoteText(String text, @ColorInt int textColor) {
        isTextDefault = (text == null || text.isEmpty()) || textColor == defaultTextColor;
        return !isTextDefault;
    }
}
