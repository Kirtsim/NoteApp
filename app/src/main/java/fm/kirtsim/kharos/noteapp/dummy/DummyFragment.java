package fm.kirtsim.kharos.noteapp.dummy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;

/**
 * Created by kharos on 13/08/2017
 */

public class DummyFragment extends BaseFragment {

    @SuppressWarnings("FieldCanBeLocal")
    private DummyViewMvc viewMvc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewMvc = new DummyViewMvcImpl(inflater, container);
        return viewMvc.getRootView();
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
