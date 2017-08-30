package fm.kirtsim.kharos.noteapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.common.collect.Sets;

import java.util.Set;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.NoteApplication;
import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ViewMvcModule;
import fm.kirtsim.kharos.noteapp.threading.BackgroundThreadPoster;
import fm.kirtsim.kharos.noteapp.ui.Animations;

/**
 * Created by kharos on 27/07/2017
 */

public abstract class BaseActivity extends AppCompatActivity implements
        BaseFragment.BaseFragmentListener {

    private boolean finalBackPressTag = false;

    @SuppressWarnings("WeakerAccess")
    @FunctionalInterface
    public interface BackPressListener {
        boolean onBackPressed();
    }

    @Inject BackgroundThreadPoster backgroundThread;
    private final Set<BackPressListener> backPressListeners = Sets.newConcurrentHashSet();
    private boolean isControllerComponentUsed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ControllerComponent component = getControllerComponent();
        if (!performDependencyInjection(component)) {
            component.inject(this);
        }
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("UnusedParameters")
    protected boolean performDependencyInjection(ControllerComponent component) {
        return false;
    }

    private ControllerComponent getControllerComponent() {
        if (isControllerComponentUsed)
            throw new IllegalStateException("controller component can be used only once at a time");
        isControllerComponentUsed = true;
        return ((NoteApplication) getApplication()).getApplicationComponent()
                .plus(new ControllerModule(this), new ViewMvcModule());
    }

    @Override
    public void onBackPressed() {
        finalBackPressTag = true;
        backPressListeners.forEach(this::notifyListenerAboutBackPress);
        if (finalBackPressTag)
            super.onBackPressed();
    }

    private void notifyListenerAboutBackPress(BackPressListener listener) {
        if (listener.onBackPressed()) {
            finalBackPressTag = false;
        }
    }

    @Override
    protected void onDestroy() {
        backPressListeners.clear();
        super.onDestroy();
    }

    @Override
    public void requestFragmentChange(Class<? extends BaseFragment> class_, Bundle arguments,
                                      Animations animations,
                                      boolean addToBackStack, String backStackStateName) {
        BaseFragment fragment = instantiateFragmentFromClass(class_);
        if (fragment != null) {
            fragment.setArguments(arguments);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (addToBackStack) {
                transaction.addToBackStack(backStackStateName);
            }
            if (animations != null)
                animations.applyAnimationsToTransaction(transaction);
            transaction.replace(R.id.main_fragment_holder, fragment, class_.getSimpleName());
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

    @Override
    public void popBackStack(String backStackStateName) {
        getSupportFragmentManager().popBackStack(backStackStateName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void registerBackPressListener(BackPressListener listener) {
        if (listener != null) {
            backgroundThread.post(() -> backPressListeners.add(listener));
        }
    }

    @Override
    public void unregisterBackPressListener(BackPressListener listener) {
        if (listener != null) {
            backgroundThread.post(() -> backPressListeners.remove(listener));
        }
    }
}
