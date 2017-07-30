package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListViewMvcImpl extends BaseViewMvc<NotesListViewMvc.NotesListViewMvcListener>
        implements NotesListViewMvc {

    private RecyclerView notesList;

    public NotesListViewMvcImpl(LayoutInflater inflater, ViewGroup container,
                                RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
                                RecyclerView.LayoutManager layoutManager) {
        setRootView(inflater.inflate(R.layout.layout_notes_list, container, false));
        initializeRecyclerView(adapter, layoutManager);
    }

    private void initializeRecyclerView(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
                                        RecyclerView.LayoutManager layoutManager) {
        notesList = (RecyclerView) rootView.findViewById(R.id.notes_recycler_view);
        notesList.setAdapter(adapter);
        notesList.setLayoutManager(layoutManager);
    }

    @Override
    public void getState(Bundle bundle) {
        // coming soon
    }


    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return notesList.getAdapter();
    }

}
