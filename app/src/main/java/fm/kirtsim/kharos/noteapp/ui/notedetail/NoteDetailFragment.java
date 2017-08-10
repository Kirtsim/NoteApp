package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

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
        NoteDetailViewMvc.NoteDetailViewMvcListener,
        NotesManager.NotesManagerListener {

    public static final String ARG_NOTE_ID = "DETAIL_NOTE_ID";
    public static final String ARG_NOTE_TITLE = "DETAIL_NOTE_TITLE";
    public static final String ARG_NOTE_TEXT = "DETAIL_NOTE_TEXT";
    public static final String ARG_NOTE_TIME = "DETAIL_NOTE_TIMESTAMP";

    private NoteDetailViewMvc viewMvc;
    @Inject NotesManager notesManager;

    private int noteId = -1;
    private long timestamp = -1;

    private boolean isTitleDefault = true;
    private boolean isTextDefault = true;

    private @ColorInt int userTextColor, defaultTextColor;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        notesManager.registerListener(this);
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
            case R.id.save: onSaveNoteMenuItemClicked(); break;
            case R.id.delete: onDeleteNoteMenuItemClicked(); break;
            case android.R.id.home:
                onSaveNoteMenuItemClicked();
                popFromBackStack(NotesListFragment.class.getSimpleName());
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
        notesManager.removeListener(this);
        notesManager = null;

        viewMvc.unregisterListener(this);
        viewMvc = null;
    }

    private void displayNoteDetailsFromArguments(Bundle arguments) {
        String noteTitle = "", noteText = "";
        if (arguments != null) {
            noteTitle = arguments.getString(ARG_NOTE_TITLE, getString(R.string.default_title));
            noteText = arguments.getString(ARG_NOTE_TEXT, getString(R.string.default_text));
        }
        viewMvc.setNoteTitle(noteTitle);
        viewMvc.setNoteText(noteText.isEmpty() ? getString(R.string.default_text) : noteText);

        if (noteId == -1) {
            viewMvc.setTitleColor(defaultTextColor);
            viewMvc.setTextColor(defaultTextColor);
        } else {
            isTitleDefault = false;
            isTextDefault = noteText.isEmpty();
        }
    }

    private void onSaveNoteMenuItemClicked() {
        timestamp = System.currentTimeMillis();
        String noteTitle = viewMvc.getTitle();
        String noteText = viewMvc.getText();
        validateNoteTitle(noteTitle, viewMvc.getTitleColor());
        validateNoteText(noteText, viewMvc.getTextColor());
        if (isTextDefault && isTitleDefault)
            return;

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
    }

    private void onDeleteNoteMenuItemClicked() {
        if (noteId != -1) {
            Note note = new Note(noteId, viewMvc.getTitle(), viewMvc.getText(), timestamp);
            notesManager.removeNote(note);
        } else {
            displayToast(R.string.note_discarded_message);
            popFromBackStack(NotesListFragment.class.getSimpleName());
        }
    }


    /* ****************************************************
     *                  NotesManager methods
     * ****************************************************/
    @Override public void onNotesFetched(@NonNull List<Note> notes) {}

    @Override public void onMultipleNotesDeleted(@NonNull List<Note> notes) {}

    @Override
    public void onNewNoteAdded(@NonNull Note note) {
        if (note.getTimestamp() == timestamp) {
            noteId = note.getId();
            displayToast(getString(R.string.note_saved_message, ""));
        }
    }

    @Override
    public void onNoteUpdated(@NonNull Note note) {
        if (note.getTimestamp() == timestamp) {
            displayToast(getString(R.string.note_saved_message, ""));
        }
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        if (note.getId() == noteId) {
            displayToast(getString(R.string.note_deleted_message, ""));
            popFromBackStack(NotesListFragment.class.getSimpleName());
        }
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
        isTitleDefault = textColor == defaultTextColor &&
                (text == null || text.isEmpty() || StringUtils.getFirstWordIndex(text) == -1);
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
        isTextDefault = textColor == defaultTextColor && text.isEmpty();
        return !isTextDefault;
    }

    private void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void displayToast(@StringRes int messageRes) {
        Toast.makeText(getContext(), messageRes, Toast.LENGTH_LONG).show();
    }
}
