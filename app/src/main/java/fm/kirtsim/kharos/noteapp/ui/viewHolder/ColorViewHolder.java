package fm.kirtsim.kharos.noteapp.ui.viewHolder;

import android.view.MotionEvent;

import fm.kirtsim.kharos.noteapp.ui.base.BaseViewHolder;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerItemViewMvc;

/**
 * Created by kharos on 23/08/2017
 */

public class ColorViewHolder extends
        BaseViewHolder<ColorViewHolder.ColorViewHolderListener, ColorPickerItemViewMvc> {

    public ColorViewHolder(ColorPickerItemViewMvc viewMvc) {
        super(viewMvc);
    }

    public interface ColorViewHolderListener {
        void onColorClicked(int pos, ColorPickerItemViewMvc colorItemMvc);
    }

    @Override
    protected void onSingleTap(MotionEvent e) {
        listener.onColorClicked(getLayoutPosition(), getItemViewMvc());
    }

    @Override
    protected void onLongTap(MotionEvent e) {
        // nothing
    }
}
