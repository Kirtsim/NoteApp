package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDetailFragment extends BaseFragment implements
        NoteDetailViewMvc.NoteDetailViewMvcListener{

    public static final String ARG_NOTE_TITLE = "DETAIL_NOTE_TITLE";
    public static final String ARG_NOTE_TEXT = "DETAIL_NOTE_TEXT";

    private NoteDetailViewMvc viewMvc;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewMvc = new NoteDetailViewMvcImpl(inflater, container);
        return viewMvc.getRootView();
    }

    private void displayNoteDetailsFromArguments(Bundle arguments) {
        if (arguments != null) {
            viewMvc.setNoteTitle(arguments.getString(ARG_NOTE_TITLE, "TITLE -1"));
            viewMvc.setNoteText(arguments.getString(ARG_NOTE_TEXT, "TEXT -1"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayNoteDetailsFromArguments(getArguments());
    }

    @Override
    public void onNoteTitleChange(String title) {
        // TODO: update Note title in database
    }

    @Override
    public void onNoteTextChange(String text) {
        // TODO: update Note text in database
    }
}
