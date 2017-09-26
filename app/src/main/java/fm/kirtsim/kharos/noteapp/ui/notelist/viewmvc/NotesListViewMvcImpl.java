package fm.kirtsim.kharos.noteapp.ui.notelist.viewmvc;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.adapter.ListAdapter;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewMvc;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.BaseListItemDecoration;
import fm.kirtsim.kharos.noteapp.ui.recyclerview.NotesRecyclerView;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListViewMvcImpl extends BaseViewMvc<NotesListViewMvc.NotesListViewMvcListener>
        implements NotesListViewMvc {

    private static final String ARG_RIGHT_SIDE_CONTAINER_VISIBLE = "notesList.CONTAINER_VISIBLE";
    private static final String ARG_ADD_BUTTON_VISIBLE = "notesList.ADD_BUTTON_VISIBLE";

    private float FAB_MAX_TRANSLATION_Y;
    private float RIGHT_SIDE_CONTAINER_MAX_TRANSLATION_X;

    private ObjectAnimator fabAnimator;
    private ObjectAnimator rightSideContainerAnimator;
    private NotesRecyclerView notesList;
    private FloatingActionButton addNoteButton;

    private FrameLayout rightSideContainer;


    public NotesListViewMvcImpl(LayoutInflater inflater, ViewGroup container, ListAdapter adapter,
                         RecyclerView.LayoutManager layoutManager) {
        setRootView(inflater.inflate(R.layout.layout_notes_list, container, false));
        initializeViews();
        initializeRecyclerView((RecyclerView.Adapter<? extends RecyclerView.ViewHolder>) adapter,
                layoutManager);
        initializeTranslationBounds();
        initializeAnimators();
        addNoteButton.setOnClickListener(this::onAddNewButtonClicked);
    }

    private void initializeViews() {
        notesList = (NotesRecyclerView) rootView.findViewById(R.id.notes_recycler_view);
        addNoteButton = (FloatingActionButton) rootView.findViewById(R.id.new_note_fab);
        rightSideContainer = (FrameLayout) rootView.findViewById(R.id.color_picker_container);
    }

    private void initializeRecyclerView(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
                                        RecyclerView.LayoutManager layoutManager) {
        notesList.setAdapter(adapter);
        notesList.setLayoutManager(layoutManager);
    }

    private void initializeTranslationBounds() {
        FAB_MAX_TRANSLATION_Y = 220;
        RIGHT_SIDE_CONTAINER_MAX_TRANSLATION_X = rightSideContainer.getTranslationX();
    }

    private void initializeAnimators() {
        fabAnimator = ObjectAnimator.ofFloat(addNoteButton, "translationY", FAB_MAX_TRANSLATION_Y);
        fabAnimator.setDuration(300);

        rightSideContainerAnimator = ObjectAnimator.ofFloat(rightSideContainer, "translationX",
                0, RIGHT_SIDE_CONTAINER_MAX_TRANSLATION_X);
        rightSideContainerAnimator.setDuration(300);
    }

    private void onAddNewButtonClicked(View v) {
        listeners.forEach(NotesListViewMvcListener::onNewNoteRequested);
    }

    @MainThread
    @Override
    public void getState(Bundle bundle) {
        bundle.putBoolean(ARG_RIGHT_SIDE_CONTAINER_VISIBLE, isRightSideContainerVisible());
        bundle.putBoolean(ARG_ADD_BUTTON_VISIBLE, isAddButtonVisible());
    }

    @Override
    public void initFromSavedState(@Nullable Bundle savedState) {
        super.initFromSavedState(savedState);
        if (savedState != null) {
            if (savedState.getBoolean(ARG_RIGHT_SIDE_CONTAINER_VISIBLE))
                rightSideContainer.setTranslationX(0);
            if (savedState.getBoolean(ARG_ADD_BUTTON_VISIBLE))
                addNoteButton.setTranslationY(0);
        }
    }

    @MainThread
    @Override
    public void showAddButton() {
        if (addNoteButton.getTranslationY() == FAB_MAX_TRANSLATION_Y) {
            fabAnimator.reverse();
        }
    }

    @Override
    public void hideAddButton() {
        if (addNoteButton.getTranslationY() != FAB_MAX_TRANSLATION_Y) {
            fabAnimator.start();
        }
    }

    @Override
    public void showRightSideContainer() {
        if (!isRightSideContainerVisible())
            rightSideContainerAnimator.reverse();
    }

    @Override
    public void hideRightSideContainer() {
        if (rightSideContainer.getTranslationX() != RIGHT_SIDE_CONTAINER_MAX_TRANSLATION_X)
            rightSideContainerAnimator.start();
    }

    @Override
    public void addViewToRightSideContainer(View view) {
        rightSideContainer.addView(view);
    }

    @Override
    public void removeViewFromRightSideContainer() {
        rightSideContainer.removeAllViews();
    }

    @Override
    public boolean isRightSideContainerVisible() {
        return rightSideContainer.getTranslationX() == 0;
    }

    @Override
    public boolean isAddButtonVisible() {
        return addNoteButton.getTranslationY() == 0;
    }

    @Override
    public RecyclerView.Adapter<?> getRecyclerViewAdapter() {
        return notesList.getAdapter();
    }

    @Override
    public void addNoteItemDecoration(BaseListItemDecoration decoration) {
        if (decoration != null) {
            notesList.addItemDecoration(decoration);
        }
    }

    @Override
    public NotesRecyclerView getRecyclerView() {
        return notesList;
    }
}
