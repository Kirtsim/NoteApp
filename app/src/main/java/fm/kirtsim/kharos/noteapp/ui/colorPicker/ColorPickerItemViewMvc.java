package fm.kirtsim.kharos.noteapp.ui.colorPicker;

import android.support.annotation.ColorInt;

import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 23/08/2017
 */

public interface ColorPickerItemViewMvc extends ViewMvc{

    void setMainColor(@ColorInt int color);

    void setHighlightColor(@ColorInt int color);

    void elevate(boolean elevate);
}
