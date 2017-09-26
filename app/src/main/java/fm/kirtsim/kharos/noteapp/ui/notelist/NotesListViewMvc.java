package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import fm.kirtsim.kharos.noteapp.ui.base.ObservableViewMvc;
import fm.kirtsim.kharos.noteapp.ui.listItemDecorator.BaseListItemDecoration;
import fm.kirtsim.kharos.noteapp.ui.recyclerview.NotesRecyclerView;

/**
 * Created by kharos on 29/07/2017
 */

interface NotesListViewMvc extends
        ObservableViewMvc<NotesListViewMvc.NotesListViewMvcListener>
{
    interface NotesListViewMvcListener {
        void onNewNoteRequested();
    }

    @MainThread void showAddButton();

    @MainThread void hideAddButton();

    void showRightSideContainer();

    void hideRightSideContainer();

    void addViewToRightSideContainer(View view);

    void removeViewFromRightSideContainer();

    boolean isRightSideContainerVisible();

    boolean isAddButtonVisible();

    void addNoteItemDecoration(BaseListItemDecoration decoration);

    NotesRecyclerView getRecyclerView();

    @SuppressWarnings("unused")
    RecyclerView.Adapter getRecyclerViewAdapter();
}
