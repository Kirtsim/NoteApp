package fm.kirtsim.kharos.noteapp.ui.main.viewmvc;

import android.os.Bundle;
import androidx.annotation.Nullable;
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
