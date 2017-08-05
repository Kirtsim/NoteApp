package fm.kirtsim.kharos.noteapp.ui.base;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public abstract class BaseViewHolder<ListenerType, MvcView extends ViewMvc>
        extends RecyclerView.ViewHolder {

    protected ListenerType listener;
    protected MvcView viewMvc;

    public BaseViewHolder(MvcView viewMvc) {
        super(viewMvc.getRootView());
        this.viewMvc = viewMvc;
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

    public MvcView getItemViewMvc() {
        return viewMvc;
    }

    protected abstract void onSingleTap(MotionEvent e);
    protected abstract void onLongTap(MotionEvent e);


    /**************************************
     *          TOUCH LISTENER
     **************************************/
    private static class TouchListener extends GestureDetector.SimpleOnGestureListener {

        private BaseViewHolder<?,?> holder;

        public TouchListener(BaseViewHolder<?,?> holder) {
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


    /**************************************
     *          CUSTOM TOUCH LISTENER
     **************************************/
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
