package fm.kirtsim.kharos.noteapp.ui.colorPicker;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.adapter.ListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.BaseListItemDecoration;
import fm.kirtsim.kharos.noteapp.ui.recyclerview.NotesRecyclerView;

/**
 * Created by kharos on 22/08/2017
 */

public class ColorPickerViewMvcImpl extends BaseViewMvc implements ColorPickerViewMvc {

    private static final String ARG_SCROLL_POSITION = "colorPicker.SCROLL_POSITION";

    private NotesRecyclerView recyclerView;

    public ColorPickerViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        setRootView(inflater.inflate(R.layout.layout_color_picker, container, false));
        recyclerView = (NotesRecyclerView) rootView.findViewById(R.id.color_picker_rv);
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
    public void addColorItemsDecoration(BaseListItemDecoration decoration) {
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void getState(Bundle bundle) {
        bundle.putInt(ARG_SCROLL_POSITION, recyclerView.getScrollPosition());
    }

    @Override
    public void initFromSavedState(@Nullable Bundle savedState) {
        super.initFromSavedState(savedState);
        if (savedState != null) {
            int scrollPosition = savedState.getInt(ARG_SCROLL_POSITION, -1);
            if (scrollPosition != -1)
                recyclerView.scrollToPosition(scrollPosition);
        }
    }

    @Override
    public NotesRecyclerView getRecyclerView() {
        return recyclerView;
    }
}
