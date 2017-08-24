package fm.kirtsim.kharos.noteapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.noteapp.ui.base.BaseViewHolder;

/**
 * Created by kharos on 07/08/2017
 */

public abstract class BaseListAdapter<AdapterListener, Holder extends BaseViewHolder>
        extends RecyclerView.Adapter<Holder> implements ListAdapter<AdapterListener> {

    protected AdapterListener listener;
    protected final List<Holder> viewHolders;
    protected final LayoutInflater layoutInflater;

    public BaseListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        viewHolders = Lists.newArrayList();
    }

    @Override
    public void setListener(AdapterListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener " +
                    NotesListAdapterImpl.NotesListAdapterListener.class.getSimpleName() + " cannot be null");
        this.listener = listener;
        startListeningToTouches();
    }

    @Override
    public void unregisterListener() {
        if (listener != null)
            stopListeningToTouches();
        listener = null;
    }

    @Override
    public void updateDataSet() {
        notifyDataSetChanged();
    }

    @Override
    public void updateItemAtPosition(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void updateRemovedAtPosition(int position) {
        notifyItemChanged(position);
    }

    protected void startListeningToTouches() {
        viewHolders.forEach(this::registerTouchListener);
    }

    protected abstract void registerTouchListener(Holder viewHolder);

    protected void stopListeningToTouches() {
        viewHolders.forEach(Holder::removeListener);
    }

    protected void addViewHolder(Holder holder) {
        viewHolders.add(holder);
        if (listener != null)
            registerTouchListener(holder);
    }
}
