package fm.kirtsim.kharos.noteapp.dummy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 13/08/2017
 */

public class DummyViewMvcImpl extends BaseViewMvc implements DummyViewMvc {

    public DummyViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.layout_note_list_item, container, false));
    }

    @Override
    public void getState(Bundle bundle) {}
}
