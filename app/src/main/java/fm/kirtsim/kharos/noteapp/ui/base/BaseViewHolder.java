package fm.kirtsim.kharos.noteapp.ui.base;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.Touch;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by kharos on 29/07/2017
 */

public abstract class BaseViewHolder<ListenerType> extends RecyclerView.ViewHolder {

    protected ListenerType listener;
    protected GestureDetectorCompat gestureDetector;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setListener(ListenerType listener) {
        this.listener = listener;
        gestureDetector = new GestureDetectorCompat(getContext(),new TouchListener(this));
    }

    public void removeListener() {
        listener = null;
        gestureDetector = null;
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    protected abstract void onSingleTap(MotionEvent e);
    protected abstract void onLongTap(MotionEvent e);

    private static class TouchListener extends GestureDetector.SimpleOnGestureListener {

        private WeakReference<BaseViewHolder> holder;

        public TouchListener(BaseViewHolder holder) {
            this.holder = new WeakReference<>(holder);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            holder.get().onSingleTap(e);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            holder.get().onLongTap(e);
        }
    }
}
