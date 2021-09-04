package fm.kirtsim.kharos.noteapp.ui.colorPicker;

import androidx.recyclerview.widget.RecyclerView;

import fm.kirtsim.kharos.noteapp.ui.adapter.ListAdapter;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.BaseListItemDecoration;
import fm.kirtsim.kharos.noteapp.ui.main.viewmvc.ViewMvc;
import fm.kirtsim.kharos.noteapp.ui.recyclerview.NotesRecyclerView;

/**
 * Created by kharos on 22/08/2017
 */

public interface ColorPickerViewMvc extends ViewMvc {

    void setAdapter(ListAdapter adapter);

    void setLayoutManager(RecyclerView.LayoutManager layoutManager);

    void addColorItemsDecoration(BaseListItemDecoration decoration);

    NotesRecyclerView getRecyclerView();
}
