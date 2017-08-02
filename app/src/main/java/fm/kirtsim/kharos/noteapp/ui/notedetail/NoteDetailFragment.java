package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.manager.NotesManager;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListFragment;

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

    private InputMethodManager inputMethodManager;
    private NoteDetailViewMvc viewMvc;
    @Inject NotesManager notesManager;

    private int noteId = -1;
    private long timestamp = -1;

    private boolean isTitleDefault = true;
    private boolean isTextDefault = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        notesManager.registerListener(this);
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Service.INPUT_METHOD_SERVICE);
        initNoteDetailsFromArguments(getArguments());
    }

    private void initNoteDetailsFromArguments(Bundle arguments) {
        if (arguments != null) {
            noteId = arguments.getInt(ARG_NOTE_ID, noteId);
            timestamp = arguments.getLong(ARG_NOTE_TIME, timestamp);
        }
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
    public void onResume() {
        super.onResume();
        displayNoteDetailsFromArguments(getArguments());
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
        if (arguments != null) {
            viewMvc.setNoteTitle(arguments.getString(ARG_NOTE_TITLE, getString(R.string.default_title)));
            viewMvc.setNoteText(arguments.getString(ARG_NOTE_TEXT, getString(R.string.default_text)));
        } else {
            viewMvc.setNoteTitle(getString(R.string.default_title));
            viewMvc.setNoteText(getString(R.string.default_text));
            final int color = getColor(R.color.default_text_color);
            viewMvc.setTitleColor(color);
            viewMvc.setTextColor(color);
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
            displayToast(R.string.note_saved_message);
        }
    }

    @Override
    public void onNoteUpdated(@NonNull Note note) {
        if (note.getTimestamp() == timestamp) {
            displayToast(R.string.note_saved_message);
        }
    }

    @Override
    public void onNoteDeleted(@NonNull Note note) {
        if (note.getId() == noteId) {
            startNewFragment(NotesListFragment.class, null, false);
            displayToast(R.string.note_deleted_message);
        }
    }


    /* ****************************************************
     *          NoteDetailMvcViewListener methods
     * ****************************************************/
    @Override
    public void onNoteTitleFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            if (viewMvc.getTitle().isEmpty()) {
                viewMvc.setTitleColor(getColor(R.color.default_text_color));
                viewMvc.setNoteTitle(getString(R.string.default_title));
                isTitleDefault = true;
            } else {
                isTitleDefault = false;
            }
        } else if (isTitleDefault) {
            viewMvc.setNoteTitle("");
            viewMvc.setTitleColor(getColor(R.color.user_text_color));
        }
    }

    @Override
    public void onNoteTextFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            if (viewMvc.getText().isEmpty()) {
                viewMvc.setTextColor(getColor(R.color.default_text_color));
                viewMvc.setNoteText(getString(R.string.default_text));
                isTextDefault = true;
            } else {
                isTextDefault = false;
            }
        } else if (isTextDefault) {
            viewMvc.setNoteText("");
            viewMvc.setTextColor(getColor(R.color.user_text_color));
        }
    }

    private void displayToast(@StringRes int messageRes) {
        Toast.makeText(getContext(), messageRes, Toast.LENGTH_LONG).show();
    }

    private int getColor(@ColorRes int colorResId) {
        return ResourcesCompat.getColor(getResources(), colorResId, null);
    }
}
