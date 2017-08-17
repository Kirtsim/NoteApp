package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kharos on 17/08/2017
 */

public abstract class NotesListItemDecoration extends RecyclerView.ItemDecoration {

    protected int space;

    void setSpacing(int spacePx) {
        this.space = spacePx;
    }

    int getSpacing() {
        return space;
    }
}
