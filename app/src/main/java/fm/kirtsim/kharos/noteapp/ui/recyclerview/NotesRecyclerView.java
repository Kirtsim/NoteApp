package fm.kirtsim.kharos.noteapp.ui.recyclerview;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by kharos on 25/09/2017
 */

public class NotesRecyclerView extends RecyclerView {

    public NotesRecyclerView(Context context) {
        super(context);
    }

    public NotesRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public int getScrollPosition() {
        int position = -1;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return position;
    }

    public void setScrollPosition(int scrollPosition) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            layoutManager.scrollToPosition(scrollPosition);
        }
    }
}
