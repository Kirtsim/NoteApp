package fm.kirtsim.kharos.noteapp.ui.base;

import android.os.Bundle;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kharos on 29/07/2017
 */

public abstract class BaseViewMvc<ListenerType> implements ObservableViewMvc<ListenerType> {

    protected View rootView;
    protected final Set<ListenerType> listeners = new HashSet<>(1);

    public void setRootView(View view) {
        this.rootView = view;
    }

    @Override
    public void registerListener(ListenerType listener) {
        if (listener == null) {
            throw new IllegalArgumentException("attempt to register listener that is null");
        }
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(ListenerType listener) {
        if (listener == null) {
            throw new IllegalArgumentException("attempt to register listener that is null");
        }
        listeners.remove(listener);
    }

    /***************************************
     *              ViewMvc
     ****************************************/

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getState() {
        Bundle bundle = new Bundle();
        getState(bundle);
        return bundle;
    }

    @Override
    public void invalidate() {
        rootView.invalidate();
    }
}
