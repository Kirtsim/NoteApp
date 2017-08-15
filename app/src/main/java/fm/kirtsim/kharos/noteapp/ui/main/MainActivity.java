package fm.kirtsim.kharos.noteapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import fm.kirtsim.kharos.noteapp.ui.Animations;
import fm.kirtsim.kharos.noteapp.ui.base.BaseActivity;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListFragment;

/**
 * Created by kharos on 27/07/2017
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewMvc viewMvc = new MainViewMvcImpl(getLayoutInflater(), null);
        setContentView(viewMvc.getRootView());
        if (savedInstanceState == null) {
            requestFragmentChange(NotesListFragment.class, null, createInitialAnimations(), false,
                    NotesListFragment.class.getSimpleName());
        }
    }

    private Animations createInitialAnimations() {
        Animations.Builder builder = new Animations.Builder();
        builder.setEnterAnimation(android.R.anim.fade_in);
        return builder.build();
    }


}
