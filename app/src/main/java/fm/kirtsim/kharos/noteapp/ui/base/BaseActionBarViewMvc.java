package fm.kirtsim.kharos.noteapp.ui.base;

import android.support.v7.app.ActionBar;
import android.view.Menu;

import fm.kirtsim.kharos.noteapp.ui.main.ActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public abstract class BaseActionBarViewMvc implements ActionBarViewMvc {

    protected final ActionBar actionBar;
    protected Menu menu;

    public BaseActionBarViewMvc(ActionBar actionBar) {
        if (actionBar == null) {
            throw new IllegalArgumentException("action bar is null");
        }
        this.actionBar = actionBar;
    }

    protected void setMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("menu is null");
        }
        this.menu = menu;
    }

    @Override
    public void setDisplayHomeAsUp(boolean homeAsUp) {
        actionBar.setDisplayHomeAsUpEnabled(homeAsUp);
    }

    @Override
    public void setShowHomeButton(boolean showHomeButton) {
        actionBar.setDisplayShowHomeEnabled(showHomeButton);
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Menu getMenu() {
        return menu;
    }
}
