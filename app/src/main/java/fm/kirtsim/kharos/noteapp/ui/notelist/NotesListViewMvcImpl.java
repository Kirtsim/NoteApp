package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.adapter.BaseListAdapter;
import fm.kirtsim.kharos.noteapp.ui.adapter.ListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListViewMvcImpl extends BaseViewMvc<NotesListViewMvc.NotesListViewMvcListener>
        implements NotesListViewMvc {

    private static final int MAX_TRANSLATION_Y = 220;

    private ObjectAnimator animator;
    private RecyclerView notesList;
    private FloatingActionButton addNoteButton;

    public NotesListViewMvcImpl(LayoutInflater inflater, ViewGroup container, ListAdapter adapter,
                                RecyclerView.LayoutManager layoutManager) {
        setRootView(inflater.inflate(R.layout.layout_notes_list, container, false));
        initializeViews();
        initializeRecyclerView((RecyclerView.Adapter<? extends RecyclerView.ViewHolder>) adapter,
                layoutManager);
        initializeAnimator();
        addNoteButton.setOnClickListener(this::onAddNewButtonClicked);
    }

    private void initializeViews() {
        notesList = (RecyclerView) rootView.findViewById(R.id.notes_recycler_view);
        addNoteButton = (FloatingActionButton) rootView.findViewById(R.id.new_note_fab);
    }

    private void initializeRecyclerView(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
                                        RecyclerView.LayoutManager layoutManager) {
        notesList.setAdapter(adapter);
        notesList.setLayoutManager(layoutManager);
    }

    private void initializeAnimator() {
        animator = ObjectAnimator.ofFloat(addNoteButton, "translationY", MAX_TRANSLATION_Y);
        animator.setDuration(300);
    }

    private void onAddNewButtonClicked(View v) {
        listeners.forEach(NotesListViewMvcListener::onNewNoteRequested);
    }

    @MainThread
    @Override
    public void getState(Bundle bundle) {}

    @MainThread
    @Override
    public void showAddButton() {
        if (addNoteButton.getTranslationY() == MAX_TRANSLATION_Y) {
            animator.reverse();
        }
    }

    @Override
    public void hideAddButton() {
        if (addNoteButton.getTranslationY() != MAX_TRANSLATION_Y) {
            animator.start();
        }
    }

    @Override
    public RecyclerView.Adapter<?> getRecyclerViewAdapter() {
        return notesList.getAdapter();
    }
}
