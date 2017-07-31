package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDetailViewMvcImpl extends BaseViewMvc<NoteDetailViewMvc.NoteDetailViewMvcListener>
        implements NoteDetailViewMvc {

    private EditText titleTV;
    private EditText textTV;
    private FrameLayout delimiterFL;

    public NoteDetailViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.layout_note_detail, container, false));
        initializeViews();
    }

    private void initializeViews() {
        titleTV = (EditText) rootView.findViewById(R.id.detail_note_title);
        textTV = (EditText) rootView.findViewById(R.id.detail_note_text);
        delimiterFL = (FrameLayout) rootView.findViewById(R.id.detail_note_delimiter);
    }


    @Override
    public void getState(Bundle bundle) {
        // coming soon;
    }

    @Override
    public void setNoteTitle(String title) {
        titleTV.setText(title);
    }

    @Override
    public void setNoteText(String text) {
        textTV.setText(text);
    }
}
