package fm.kirtsim.kharos.noteapp.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 29/07/2017
 */

public abstract class BaseViewMvc<ListenerType> implements ViewMvc {

    protected View rootView;
    protected final Set<ListenerType> listeners = new HashSet<>(1);

    public void setRootView(View view) {
        this.rootView = view;
    }

    public void registerListener(ListenerType listener) {
        if (listener == null) {
            throw new IllegalArgumentException("attempt to register listener that is null");
        }
        listeners.add(listener);
    }

    public void unregisterListener(ListenerType listener) {
        if (listener == null) {
            throw new IllegalArgumentException("attempt to register listener that is null");
        }
        listeners.remove(listener);
    }


    @SuppressWarnings("unchecked")
    @Nullable
    protected <T extends View> T findViewById(@IdRes int resId) {
        if (rootView != null) {
            return (T) rootView.findViewById(resId);
        }
        return null;
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
}
