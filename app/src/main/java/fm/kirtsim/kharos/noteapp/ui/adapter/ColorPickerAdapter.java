package fm.kirtsim.kharos.noteapp.ui.adapter;

import androidx.annotation.ColorInt;

/**
 * Created by kharos on 23/08/2017
 */

public interface ColorPickerAdapter extends
        ListAdapter<ColorPickerAdapter.ColorPickerAdapterListener> {

    public interface ColorPickerAdapterListener {
        void onColorClicked(@ColorInt int color);
    }

    int getHighlightedColorPosition();

    void setHighlightedColorPosition(int position);

    void setColors(int[] colors);

    void setHighlightColor(@ColorInt int color);

    void resetHighlighting();

}
