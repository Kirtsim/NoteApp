package fm.kirtsim.kharos.noteapp.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import javax.inject.Inject;

import fm.kirtsim.kharos.noteapp.NoteApplication;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ViewMvcModule;
import fm.kirtsim.kharos.noteapp.threading.MainThreadPoster;
import fm.kirtsim.kharos.noteapp.ui.Animations;

/**
 * Created by kharos on 27/07/2017
 */

public abstract class BaseFragment extends Fragment {

    @SuppressWarnings("WeakerAccess")
    public interface BaseFragmentListener {
        void requestFragmentChange(Class<? extends BaseFragment> class_, Bundle arguments,
                                   Animations animations,
                                   boolean addToBackStack, String backStackStateName);
        void popBackStack(String callingFragmentName);
        void registerBackPressListener(BaseActivity.BackPressListener listener);
        void unregisterBackPressListener(BaseActivity.BackPressListener listener);
    }

    @Inject MainThreadPoster mainThread;
    private boolean isControllerComponentUsed = false;
    protected BaseFragmentListener baseFragmentListener;
    private BaseActivity.BackPressListener backPressListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ControllerComponent component = getControllerComponent();
        if (!performInjection(component))
            component.inject(this);
        super.onCreate(savedInstanceState);
    }

    private ControllerComponent getControllerComponent() {
        if (isControllerComponentUsed)
            throw new IllegalStateException("controller component can be used only once at a time");
        isControllerComponentUsed = true;
        return ((NoteApplication) getActivity().getApplication()).getApplicationComponent()
                .newControllerComponent(
                        new ControllerModule((BaseActivity) getActivity()), new ViewMvcModule());
    }

    protected abstract String getClassName();

    protected boolean performInjection(ControllerComponent component) {
        return false;
    }

    protected void startNewFragment(Class<? extends BaseFragment> class_, Bundle arguments,
                                    Animations animations,
                                    boolean addToBackStack) {
        baseFragmentListener.requestFragmentChange(class_, arguments,
                animations, addToBackStack, class_.getSimpleName());
    }

    protected void popFromBackStack(String backStackStateName) {
        baseFragmentListener.popBackStack(backStackStateName);
    }

    protected final void popFromBackStack() {
        popFromBackStack(getClassName());
    }

    protected void onBackPressed() {
        popFromBackStack();
    }

    protected void showToast(@StringRes int resId) {
        mainThread.post(() -> Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show());
    }

    protected void showToast(@StringRes int resId, Object... formatArgs) {
        mainThread.post(() -> Toast.makeText(getContext(), getString(resId, formatArgs),
                Toast.LENGTH_LONG).show());
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

    private BaseActivity.BackPressListener getBackPressListener() {
        if (backPressListener == null) {
            backPressListener = this::onBackPressed;
        }
        return backPressListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseFragmentListener = null;
        backPressListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        baseFragmentListener.unregisterBackPressListener(backPressListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        baseFragmentListener.registerBackPressListener(getBackPressListener());
    }
}
