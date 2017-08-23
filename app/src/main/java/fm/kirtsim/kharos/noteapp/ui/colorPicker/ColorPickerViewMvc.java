package fm.kirtsim.kharos.noteapp.ui.colorPicker;

import android.support.v7.widget.RecyclerView;

import fm.kirtsim.kharos.noteapp.ui.adapter.ListAdapter;
import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 22/08/2017
 */

public interface ColorPickerViewMvc extends ViewMvc {

    void setAdapter(ListAdapter adapter);

    void setLayoutManager(RecyclerView.LayoutManager layoutManager);

}
