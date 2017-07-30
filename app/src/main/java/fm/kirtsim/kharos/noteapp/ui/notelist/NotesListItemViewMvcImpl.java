package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 30/07/2017
 */

public class NotesListItemViewMvcImpl extends BaseViewMvc implements NotesListItemViewMvc {

    private TextView titleTV;
    private TextView textTV;

    public NotesListItemViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.layout_note_list_item, container, false));
        initializeViews();
    }

    private void initializeViews() {
        titleTV = (TextView) rootView.findViewById(R.id.note_list_item_title);
        textTV = (TextView) rootView.findViewById(R.id.note_list_item_text);
    }

    @Override
    public void getState(Bundle bundle) {
        // coming soon;
    }

    @Override
    public void setTitle(String title) {
        titleTV.setText(title);
    }

    @Override
    public void setText(String text) {
        textTV.setText(text);
    }

    @Override
    public void registerListener(Object listener) {}

    @Override
    public void unregisterListener(Object listener) {}
}
