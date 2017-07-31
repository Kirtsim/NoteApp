package fm.kirtsim.kharos.noteapp.ui.main;

import android.support.annotation.IdRes;

/**
 * Created by kharos on 31/07/2017
 */

public interface MainViewMvc extends ViewMvc {

    interface MainViewMvcListener {

    }

    @IdRes int getFragmentContainerId();
}
