package fm.kirtsim.kharos.noteapp.ui.adapter.itemTouchHelper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by kharos on 29/08/2017
 */

public class NotesListItemTouchHelper extends ItemTouchHelper {

    private RecyclerView recyclerView;

    public NotesListItemTouchHelper(Callback callback, RecyclerView recyclerView) {
        super(callback);
        this.recyclerView = recyclerView;
    }

    public void listen(boolean listen) {
        if (listen)
            attachToRecyclerView(recyclerView);
        else
            attachToRecyclerView(null);
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView != this.recyclerView)
            this.recyclerView = recyclerView;
        super.attachToRecyclerView(recyclerView);
    }
}
