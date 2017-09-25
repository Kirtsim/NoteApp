package fm.kirtsim.kharos.noteapp.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by kharos on 04/08/2017
 */

public interface ActionBarViewMvc {

    void setMenu(Menu menu, MenuInflater menuInflater);

    void setHomeButtonVisible(boolean visible);

    void setTitle(String title);

    String getTitle();

    Menu getMenu();

    void hideAllIcons();

    void getState(Bundle bundle);

    Bundle getState();

    void initializeFromSavedState(Bundle savedState);
}
