package fm.kirtsim.kharos.noteapp.ui.base;

import android.view.Menu;

import fm.kirtsim.kharos.noteapp.ui.main.MenuViewMvc;

/**
 * Created by kharos on 04/08/2017
 */

public abstract class BaseMenuViewMvc implements MenuViewMvc{

    protected Menu menu;

    protected void setMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("menu is null");
        }
        this.menu = menu;
    }

    @Override
    public Menu getMenu() {
        return menu;
    }
}
