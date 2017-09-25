package fm.kirtsim.kharos.noteapp.ui.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;

import fm.kirtsim.kharos.noteapp.ui.main.ActionBarViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

@SuppressWarnings("WeakerAccess")
public abstract class BaseActionBarViewMvc implements ActionBarViewMvc {

    protected final ActionBar actionBar;
    protected Menu menu;
    private boolean homeButtonVisible;

    public BaseActionBarViewMvc(ActionBar actionBar) {
        if (actionBar == null) {
            throw new IllegalArgumentException("action bar is null");
        }
        this.actionBar = actionBar;
    }

    protected boolean isBackButtonVisible() {
        return homeButtonVisible;
    }

    protected void setMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("menu is null");
        }
        this.menu = menu;
    }

    @Override
    public Bundle getState() {
        final Bundle state = new Bundle(6);
        getState(state);
        return state;
    }

    @Override
    public void setHomeButtonVisible(boolean visible) {
        actionBar.setDisplayHomeAsUpEnabled(visible);
        actionBar.setDisplayShowHomeEnabled(visible);
        homeButtonVisible = visible;
    }

    @Override
    public void setTitle(String title) {
        actionBar.setTitle(title);
    }

    @Override
    public String getTitle() {
        CharSequence csTitle = actionBar.getTitle();
        if (csTitle == null)
            return "";
        return csTitle.toString();
    }

    @Override
    public Menu getMenu() {
        return menu;
    }
}
