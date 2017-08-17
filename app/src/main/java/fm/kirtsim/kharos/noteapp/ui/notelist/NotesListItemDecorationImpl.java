package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kharos on 17/08/2017
 */

public class NotesListItemDecorationImpl extends NotesListItemDecoration {

    public NotesListItemDecorationImpl(int spacePx) {
        space = spacePx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = space;
    }
}
