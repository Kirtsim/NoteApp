package fm.kirtsim.kharos.noteapp.ui.colorPicker;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;
import fm.kirtsim.kharos.noteapp.utils.Units;

/**
 * Created by kharos on 24/08/2017
 */

public class ColorPickerItemViewMvcImpl extends BaseViewMvc implements ColorPickerItemViewMvc {

    private final int ELEVATION_HEIGHT;

    private ImageView mainColorIV;
    private FrameLayout highlightColorFL;

    public ColorPickerItemViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        ELEVATION_HEIGHT = Units.dp2px(2, inflater.getContext().getResources().getDisplayMetrics());
        setRootView(inflater.inflate(R.layout.layout_color_item, container, false));
        initializeViews();
    }

    private void initializeViews() {
        mainColorIV = (ImageView) rootView.findViewById(R.id.color_body);
        highlightColorFL = (FrameLayout) rootView.findViewById(R.id.color_select_border);
    }

    @Override
    public void setMainColor(@ColorInt int color) {
        final GradientDrawable background = (GradientDrawable) mainColorIV.getBackground();
        background.setColor(color);
    }

    @Override
    public void setHighlightColor(@ColorInt int color) {
        final GradientDrawable background = (GradientDrawable) highlightColorFL.getBackground();
        background.setColor(color);
    }

    @Override
    public void elevate(boolean elevate) {
        highlightColorFL.setElevation(elevate ? ELEVATION_HEIGHT : 0);
    }

    @Override
    public void getState(Bundle bundle) {
        // nothing
    }
}
