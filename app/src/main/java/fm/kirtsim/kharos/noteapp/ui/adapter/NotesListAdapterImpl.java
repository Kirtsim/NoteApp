package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator.AdapterNotesListCoordinator;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvcImpl;
import fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual.NotesListItemViewMvcImplDebug;
import fm.kirtsim.kharos.noteapp.ui.viewHolder.NotesListViewHolder;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListAdapterImpl extends NotesListAdapter {

    public NotesListAdapterImpl(LayoutInflater layoutInflater,
                                AdapterNotesListCoordinator notesCoordinator) {
        super(layoutInflater);
        setNotesCoordinator(notesCoordinator);
        setHasStableIds(true);
    }

    /* ****************************************************
     *            Adapter Inherited  methods
     * ****************************************************/
    @Override
    public NotesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotesListItemViewMvc mvcView = new NotesListItemViewMvcImplDebug(layoutInflater, parent);
        NotesListViewHolder viewHolder = new NotesListViewHolder(mvcView);
        addViewHolder(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int position) {
        final Note note = notesCoordinator.getNoteAt(position);
        final NotesListItemViewMvc listItemView = holder.getItemViewMvc();
        if (listener != null) {
            listener.onNoteItemVisible(note, listItemView);
        }
    }

    @Override
    public long getItemId(int position) {
        return notesCoordinator.getNoteAt(position).getId();
    }

    @Override
    public int getItemCount() {
        return notesCoordinator.getNoteCount();
    }


    /* ****************************************************
     *            BaseAdapter Inherited  methods
     * ****************************************************/

    @Override
    protected void registerTouchListener(NotesListViewHolder viewHolder) {
        viewHolder.setListener(this);
    }

    /* ********************************************************
     *            NotesListViewHolderListener methods
     * ********************************************************/
    @Override
    public void onNoteSingleClicked(int position, NotesListItemViewMvc noteItemView) {
        final Note clickedNote = notesCoordinator.getNoteAt(position);
        if (listener != null) {
            listener.onNoteItemSingleClicked(clickedNote, noteItemView, position);
        }
    }

    @Override
    public void onNoteLongClicked(int position, NotesListItemViewMvc noteItemView) {
        final Note clickedNote = notesCoordinator.getNoteAt(position);
        if (listener != null) {
            listener.onNoteItemLongClicked(clickedNote, noteItemView, position);
        }
    }

    // ************** NotesListAdapter ***************************

    @Override
    public void notifyNoteChanged(Note note) {
        final int index = notesCoordinator.getIndexOfNote(note);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    @Override
    public void notifyNoteRemoved(Note note) {
        final int index = notesCoordinator.getIndexOfNote(note);
        if (index != -1) {
            notifyItemRemoved(index);
        }
    }

    @Override
    public boolean onItemMove(int posFrom, int posTo) {
        int lower = Math.min(posFrom, posTo);
        int higher = Math.max(posFrom, posTo);
        while (lower < higher) {
            notesCoordinator.swapNotesAt(lower, lower + 1);
            lower++;
        }
        notifyItemMoved(posFrom, posTo);
        return true;
    }

    @Override
    public void dragFinished(int startedFrom, int finishedAt) {
        listener.onNoteItemPositionChanged(notesCoordinator.getNoteAt(finishedAt),
                startedFrom, finishedAt);
    }
}
