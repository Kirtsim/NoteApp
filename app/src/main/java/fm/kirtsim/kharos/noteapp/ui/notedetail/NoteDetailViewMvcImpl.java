package fm.kirtsim.kharos.noteapp.ui.notedetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDetailViewMvcImpl extends BaseViewMvc<NoteDetailViewMvc.NoteDetailViewMvcListener>
        implements NoteDetailViewMvc {

    private EditText titleET;
    private EditText textET;
    private FrameLayout delimiterFL;

    public NoteDetailViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.layout_note_detail, container, false));
        initializeViews();
        titleET.setOnFocusChangeListener((v, focus) -> listeners.
                forEach(listener -> listener.onNoteTitleFocusChanged(focus)));
        textET.setOnFocusChangeListener((v, focus) -> listeners.
                forEach(listener -> listener.onNoteTextFocusChanged(focus)));
    }

    private void initializeViews() {
        titleET = (EditText) rootView.findViewById(R.id.detail_note_title);
        textET = (EditText) rootView.findViewById(R.id.detail_note_text);
        delimiterFL = (FrameLayout) rootView.findViewById(R.id.detail_note_delimiter);
    }


    /* ***************************************
     *         ViewMvc methods
     * ***************************************/

    @Override
    public void getState(Bundle bundle) {
        // coming soon;
    }


    /* ***************************************
     *         NoteDetailViewMvc methods
     * ***************************************/

    @Override
    public void setNoteTitle(String title) {
        titleET.setText(title);
    }

    @Override
    public void setNoteText(String text) {
        textET.setText(text);
    }

    @Override
    public void setTitleColor(int rgb) {
        titleET.setTextColor(rgb);
    }

    @Override
    public void setTextColor(int rgb) {
        textET.setTextColor(rgb);
    }

    @Override
    public String getTitle() {
        return titleET.getText().toString();
    }

    @Override
    public String getText() {
        return textET.getText().toString();
    }
}
