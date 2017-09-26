package fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc;

import android.support.annotation.ColorInt;

import fm.kirtsim.kharos.noteapp.ui.main.viewmvc.ViewMvc;

/**
 * Created by kharos on 30/07/2017
 */

public interface NotesListItemViewMvc extends ViewMvc{

    void setTitle(String title);
    void setText(String text);
    void setBackgroundColors(@ColorInt int outerBackgroundColor,
                             @ColorInt int innerBackgroundColor);
    void setBorderColor(@ColorInt int color);
}
