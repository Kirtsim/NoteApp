package fm.kirtsim.kharos.noteapp.ui.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fm.kirtsim.kharos.noteapp.NoteApplication;
import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dependencyinjection.application.DaggerApplicationComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ViewMvcModule;
import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 27/07/2017
 */

public abstract class BaseActivity extends AppCompatActivity implements
        BaseFragment.BaseFragmentListener {

    private boolean isControllerComponentUsed = false;

    public ControllerComponent getControllerComponent() {
        if (isControllerComponentUsed)
            throw new IllegalStateException("controller component can be used only once at a time");
        isControllerComponentUsed = true;
        return ((NoteApplication) getApplication()).getApplicationComponent()
                .newControllerComponent(new ControllerModule(this), new ViewMvcModule());
    }

    @Override
    public void requestFragmentChange(Class<? extends BaseFragment> class_, Bundle arguments,
                                      boolean addToBackStack) {
        BaseFragment fragment = instantiateFragmentFromClass(class_);
        if (fragment != null) {
            fragment.setArguments(arguments);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (addToBackStack)
                transaction.addToBackStack(null);
            transaction.add(R.id.main_fragment_holder, fragment, class_.getSimpleName());
            transaction.commit();
        }
    }

    private BaseFragment instantiateFragmentFromClass(Class<? extends BaseFragment> class_) {
        BaseFragment fragment = null;
        try {
            fragment = class_.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
