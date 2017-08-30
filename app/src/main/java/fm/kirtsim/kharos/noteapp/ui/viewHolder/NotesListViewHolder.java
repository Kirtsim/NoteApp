package fm.kirtsim.kharos.noteapp.ui.viewHolder;

import android.view.MotionEvent;

import fm.kirtsim.kharos.noteapp.ui.base.BaseViewHolder;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListViewHolder extends
        BaseViewHolder<NotesListViewHolder.NotesListViewHolderListener, NotesListItemViewMvc> {

    public interface NotesListViewHolderListener {
        void onNoteSingleClicked(int position, NotesListItemViewMvc noteItemView);
        void onNoteLongClicked(int position, NotesListItemViewMvc noteItemView);
    }

    public NotesListViewHolder(NotesListItemViewMvc itemView) {
        super(itemView);
    }

    @Override
    protected void onSingleTap(MotionEvent e) {
        listener.onNoteSingleClicked(getAdapterPosition(), viewMvc);
    }

    @Override
    protected void onLongTap(MotionEvent e) {
        listener.onNoteLongClicked(getAdapterPosition(), viewMvc);
    }
}
