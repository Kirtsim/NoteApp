package fm.kirtsim.kharos.noteapp.ui.main;

import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by kharos on 04/08/2017
 */

public interface ActionBarViewMvc {

    void setMenu(Menu menu, MenuInflater menuInflater);

    void setDisplayHomeAsUp(boolean homeAsUp);

    void setShowHomeButton(boolean showHomeButton);

    void setTitle(String title);

    String getTitle();

    Menu getMenu();
}
