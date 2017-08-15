package fm.kirtsim.kharos.noteapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by kharos on 29/07/2017
 */

public interface ViewMvc {

    View getRootView();

    void initFromSavedState(@Nullable Bundle savedState);

    void getState(Bundle bundle);

    Bundle getState();
}
