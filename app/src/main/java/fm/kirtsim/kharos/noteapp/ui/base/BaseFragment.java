package fm.kirtsim.kharos.noteapp.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import fm.kirtsim.kharos.noteapp.NoteApplication;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ViewMvcModule;

/**
 * Created by kharos on 27/07/2017
 */

public abstract class BaseFragment extends Fragment {

    public interface BaseFragmentListener {
        void requestFragmentChange(Class<? extends BaseFragment> class_, Bundle arguments,
                                   boolean addToBackStack);
    }

    private boolean isControllerComponentUsed = false;
    protected BaseFragmentListener baseFragmentListener;

    public ControllerComponent getControllerComponent() {
        if (isControllerComponentUsed)
            throw new IllegalStateException("controller component can be used only once at a time");
        isControllerComponentUsed = true;
        return ((NoteApplication) getActivity().getApplication()).getApplicationComponent()
                .newControllerComponent(new ControllerModule(getActivity()), new ViewMvcModule());
    }

    protected void startNewFragment(Class<? extends BaseFragment> class_, Bundle arguments,
                                    boolean addToBackStack) {
        baseFragmentListener.requestFragmentChange(class_, arguments, addToBackStack);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragmentListener) {
            baseFragmentListener = (BaseFragmentListener) context;
        } else {
            throw new IllegalArgumentException("activity must implement" +
                    BaseFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseFragmentListener = null;
    }
}
