package fm.kirtsim.kharos.noteapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListFragment;

/**
 * Created by kharos on 27/07/2017
 */

public class MainActivity extends BaseActivity {

    private MainViewMvc viewMvc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new MainViewMvcImpl(getLayoutInflater(), null);
        setContentView(viewMvc.getRootView());
        requestFragmentChange(NotesListFragment.class, null, false);
    }
}
