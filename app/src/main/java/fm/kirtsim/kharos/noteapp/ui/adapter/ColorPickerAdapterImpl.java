package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.colorPicker.ColorPickerItemViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.ColorViewHolder;

/**
 * Created by kharos on 23/08/2017
 */

public class ColorPickerAdapterImpl extends
        BaseListAdapter<ColorPickerAdapter.ColorPickerAdapterListener, ColorViewHolder>
        implements ColorPickerAdapter, ColorViewHolder.ColorViewHolderListener {

    @ColorInt private int highlightColor;
    private final List<Integer> colorsList;
    private ColorPickerItemViewMvc highlightedView;
    private int highlightedAt;

    public ColorPickerAdapterImpl(LayoutInflater layoutInflater) {
        super(layoutInflater);
        colorsList = Lists.newArrayList();
        resetHighlighting();
    }

    @Override
    protected void registerTouchListener(ColorViewHolder viewHolder) {
        viewHolder.setListener(this);
    }


    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorViewHolder(new ColorPickerItemViewMvcImpl(layoutInflater, parent));
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        ColorPickerItemViewMvc colorView = holder.getItemViewMvc();
        colorView.setMainColor(colorsList.get(position));
        colorView.setHighlightColor(colorsList.get(position));
        if (position == highlightedAt)
            highlightColor(position, colorView);
    }

    private void highlightColor(int pos, ColorPickerItemViewMvc colorView) {
        highlightedAt = pos;
        highlightedView = colorView;
        colorView.setHighlightColor(highlightColor);
        colorView.elevate(true);
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }

    @Override
    public void onColorClicked(int pos, ColorPickerItemViewMvc colorItemMvc) {
        final int oldHighlightedAt = highlightedAt;
        resetHighlighting();
        highlightColor(pos, colorItemMvc);
        listener.onColorClicked(colorsList.get(pos));
        notifyItemChanged(pos);
        if (oldHighlightedAt != -1)
            notifyItemChanged(oldHighlightedAt);
    }

    @Override
    public void setColors(int[] colors) {
        colorsList.clear();
        for (int color : colors) colorsList.add(color);
    }

    @Override
    public void setHighlightColor(@ColorInt int color) {
        highlightColor = color;
    }

    @Override
    public void resetHighlighting() {
        if (highlightedView != null) {
            highlightedView.setHighlightColor(colorsList.get(highlightedAt));
            highlightedView.elevate(false);
            highlightedView = null;
        }
        highlightedAt = -1;
    }
}
