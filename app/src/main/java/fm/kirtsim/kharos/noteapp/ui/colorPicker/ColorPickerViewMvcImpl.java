package fm.kirtsim.kharos.noteapp.ui.colorPicker;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.adapter.ListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 22/08/2017
 */

public class ColorPickerViewMvcImpl extends BaseViewMvc implements ColorPickerViewMvc {

    private RecyclerView recyclerView;

    public ColorPickerViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.layout_color_picker, container, false));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.color_picker_rv);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        recyclerView.setAdapter((RecyclerView.Adapter<? extends RecyclerView.ViewHolder>) adapter);
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void getState(Bundle bundle) {
        // not coming
    }
}
