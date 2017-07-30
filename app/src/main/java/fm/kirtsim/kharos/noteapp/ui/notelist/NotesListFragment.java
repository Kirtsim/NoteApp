package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListFragment extends BaseFragment implements
        NotesListAdapter.NotesListAdapterListener,
        NotesListViewMvc.NotesListViewMvcListener {

    private NotesListViewMvc mvcView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final NotesListAdapter adapter = new NotesListAdapter(inflater);
        mvcView = new NotesListViewMvcImpl(inflater, container,
                adapter, new LinearLayoutManager(inflater.getContext()));
        adapter.registerListener(this);
        return mvcView.getRootView();
    }

    @Override
    public void onNoteItemSingleClicked(Note note) {
        Toast.makeText(getContext(), "short: " + note.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoteItemLongClicked(Note note) {
        Toast.makeText(getContext(), "long: " + note.getTitle(), Toast.LENGTH_LONG).show();
    }
}
