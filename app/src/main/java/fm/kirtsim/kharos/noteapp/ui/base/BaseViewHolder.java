package fm.kirtsim.kharos.noteapp.ui.base;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kharos on 29/07/2017
 */

public abstract class BaseViewHolder<ListenerType> extends RecyclerView.ViewHolder {

    protected ListenerType listener;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setListener(ListenerType listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener passed as a parameter is null");
        this.listener = listener;
        GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(),
                new TouchListener(this));
        itemView.setOnTouchListener(new ViewCustomTouchListener(gestureDetector));
    }

    public void removeListener() {
        listener = null;
        itemView.setOnTouchListener(null);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    protected abstract void onSingleTap(MotionEvent e);
    protected abstract void onLongTap(MotionEvent e);

    private static class TouchListener extends GestureDetector.SimpleOnGestureListener {

        private BaseViewHolder holder;

        public TouchListener(BaseViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            holder.onSingleTap(e);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            holder.onLongTap(e);
        }
    }

    private static class ViewCustomTouchListener implements View.OnTouchListener {
        private final GestureDetectorCompat gestureDetector;


        public ViewCustomTouchListener(GestureDetectorCompat gestureDetector) {
            this.gestureDetector = gestureDetector;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    }
}
