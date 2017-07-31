package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.adapter.NotesListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseFragment;
import fm.kirtsim.kharos.noteapp.ui.notedetail.NoteDetailFragment;

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
        adapter.addNotes(getDummyNotes());
        return mvcView.getRootView();
    }

    private List<Note> getDummyNotes() {
        final int COUNT = 20;
        List<Note> notes = new ArrayList<>(COUNT);
        for (int i = 1; i <= COUNT; ++i) {
            notes.add(new Note("Title " + i, "Text text text text text " + i, i));
        }
        return notes;
    }

    @Override
    public void onNoteItemSingleClicked(Note note) {
        Bundle arguments = new Bundle(2);
        arguments.putString(NoteDetailFragment.ARG_NOTE_TITLE, note.getTitle());
        arguments.putString(NoteDetailFragment.ARG_NOTE_TEXT, note.getText());
        startNewFragment(NoteDetailFragment.class, arguments, true);
    }

    @Override
    public void onNoteItemLongClicked(Note note) {
        Toast.makeText(getContext(), "long: " + note.getTitle(), Toast.LENGTH_LONG).show();
    }
}
