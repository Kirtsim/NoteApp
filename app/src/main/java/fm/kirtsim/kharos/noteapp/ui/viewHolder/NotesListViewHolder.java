package fm.kirtsim.kharos.noteapp.ui.viewHolder;

import android.view.MotionEvent;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.base.BaseViewHolder;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public class NotesListViewHolder extends
        BaseViewHolder<NotesListViewHolder.NotesListViewHolderListener, NotesListItemViewMvc> {

    interface NotesListViewHolderListener {
        void onNoteSingleClicked(int position);
        void onNoteLongClicked(int position);
    }

    public NotesListViewHolder(NotesListItemViewMvc itemView) {
        super(itemView);
    }

    @Override
    protected void applyDataFromNote(Note note) {
        viewMvc.setText(note.getText());
        viewMvc.setTitle(note.getTitle());
    }

    @Override
    protected void onSingleTap(MotionEvent e) {
        listener.onNoteSingleClicked(getAdapterPosition());
    }

    @Override
    protected void onLongTap(MotionEvent e) {
        listener.onNoteLongClicked(getAdapterPosition());
    }
}
