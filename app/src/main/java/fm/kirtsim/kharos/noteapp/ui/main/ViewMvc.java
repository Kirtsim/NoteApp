package fm.kirtsim.kharos.noteapp.ui.main;

import android.os.Bundle;
import android.view.View;

/**
 * Created by kharos on 29/07/2017
 */

public interface ViewMvc {

    View getRootView();

    void getState(Bundle bundle);

    Bundle getState();
}
