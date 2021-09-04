package fm.kirtsim.kharos.noteapp.ui.adapter.touchCallback;


import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by kharos on 27/08/2017
 */

public class NotesListItemTouchCallback extends ItemTouchHelper.Callback {

    private final ItemTouchCallbackListener listener;
    private int startPos = -1;

    public NotesListItemTouchCallback(ItemTouchCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, ItemTouchHelper.ACTION_STATE_IDLE);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (startPos == -1)
            startPos = viewHolder.getAdapterPosition();
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (startPos != -1) {
            listener.dragFinished(startPos, viewHolder.getAdapterPosition());
            startPos = -1;
        }
    }
}