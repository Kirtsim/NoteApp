package fm.kirtsim.kharos.noteapp.ui.listItemDecorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kharos on 17/08/2017
 */

public abstract class BaseListItemDecoration extends RecyclerView.ItemDecoration {

    @SuppressWarnings("WeakerAccess")
    protected int space;

    void setSpacing(int spacePx) {
        this.space = spacePx;
    }

    int getSpacing() {
        return space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}
