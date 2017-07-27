package fm.kirtsim.kharos.noteapp.ui.base;

import android.support.v4.app.Fragment;

import fm.kirtsim.kharos.noteapp.NoteApplication;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ViewMvcModule;

/**
 * Created by kharos on 27/07/2017
 */

public abstract class BaseFragment extends Fragment {

    private boolean isControllerComponentUsed = false;

    public ControllerComponent getControllerComponent() {
        if (isControllerComponentUsed)
            throw new IllegalStateException("controller component can be used only once at a time");
        isControllerComponentUsed = true;
        return ((NoteApplication) getActivity().getApplication()).getApplicationComponent()
                .newControllerComponent(new ControllerModule(getActivity()), new ViewMvcModule());
    }
}
