package fm.kirtsim.kharos.noteapp.ui.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 31/07/2017
 */

public class MainViewMvcImpl extends BaseViewMvc<MainViewMvc.MainViewMvcListener> implements
        MainViewMvc {

    public MainViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.activity_main, container, false));
    }

    @Override
    public void getState(Bundle bundle) {
        // coming soon;
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_fragment_holder;
    }
}
