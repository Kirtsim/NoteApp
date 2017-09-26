package fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDetailViewMvcImpl extends BaseViewMvc<NoteDetailViewMvc.NoteDetailViewMvcListener>
        implements NoteDetailViewMvc {

    private static final String ARG_NOTE_TITLE = "NOTE_TITLE";
    private static final String ARG_NOTE_TEXT = "NOTE_TEXT";
    private static final String ARG_NOTE_TITLE_COLOR = "NOTE_TITLE_COLOR";
    private static final String ARG_NOTE_TEXT_COLOR = "NOTE_TEXT_COLOR";
    private static final String ARG_NOTE_DATE_TIME = "NOTE_DATE_TIME";

    private TextView dateTimeTV;
    private EditText titleET;
    private EditText textET;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
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
        dateTimeTV = (TextView) rootView.findViewById(R.id.detail_note_date_time);
        titleET = (EditText) rootView.findViewById(R.id.detail_note_title);
        textET = (EditText) rootView.findViewById(R.id.detail_note_text);
        delimiterFL = (FrameLayout) rootView.findViewById(R.id.detail_note_delimiter);
    }


    /* ***************************************
     *         ViewMvc methods
     * ***************************************/

    @Override
    public void initFromSavedState(@Nullable Bundle savedState) {
        super.initFromSavedState(savedState);
        if (savedState != null) {
            setNoteTitle(savedState.getString(ARG_NOTE_TITLE));
            setNoteText(savedState.getString(ARG_NOTE_TEXT));
            setTitleColor(savedState.getInt(ARG_NOTE_TITLE_COLOR, 0));
            setTextColor(savedState.getInt(ARG_NOTE_TEXT_COLOR, 0));
            dateTimeTV.setText(savedState.getString(ARG_NOTE_DATE_TIME));
        }
    }

    @Override
    public void getState(Bundle bundle) {
        bundle.putString(ARG_NOTE_TITLE, getTitle());
        bundle.putString(ARG_NOTE_TEXT, getText());
        bundle.putInt(ARG_NOTE_TITLE_COLOR, getTitleColor());
        bundle.putInt(ARG_NOTE_TEXT_COLOR, getTextColor());
        bundle.putString(ARG_NOTE_DATE_TIME, dateTimeTV.getText().toString());
    }


    /* ***************************************
     *         NoteDetailViewMvc methods
     * ***************************************/

    @Override
    public void setNoteDateAndTime(String dateAndTime) {
        dateTimeTV.setText(dateAndTime);
    }

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

    @Override
    public int getTitleColor() {
        return titleET.getCurrentTextColor();
    }

    @Override
    public int getTextColor() {
        return textET.getCurrentTextColor();
    }
}
