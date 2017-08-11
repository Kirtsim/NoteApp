package fm.kirtsim.kharos.noteapp.ui;

import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import fm.kirtsim.kharos.noteapp.R;

/**
 * Created by kharos on 11/08/2017
 */

public class Animations {

    public static final @AnimRes int ANIMATION_NONE = R.anim.animation_none;

    private @AnimRes int enterAnimation;
    private @AnimRes int exitAnimation;
    private @AnimRes int popEnterAnimation;
    private @AnimRes int popExitAnimation;

    private Animations() {}

    public void applyAnimationsToTransaction(@NonNull FragmentTransaction transaction) {
        transaction.setCustomAnimations(enterAnimation, exitAnimation,
                popEnterAnimation, popExitAnimation);
    }

    public static class Builder {

        private Animations animations;

        public Builder() {
            animations = new Animations();
            initializeWithDefaultAnimations();
        }

        private void initializeWithDefaultAnimations() {
            animations.enterAnimation = ANIMATION_NONE;
            animations.exitAnimation = ANIMATION_NONE;
            animations.popEnterAnimation = ANIMATION_NONE;
            animations.popExitAnimation = ANIMATION_NONE;
        }

        public Builder setEnterAnimation(@AnimRes int enterAnim) {
            animations.enterAnimation = enterAnim;
            return this;
        }

        public Builder setExitAnimation(@AnimRes int exitAnim) {
            animations.exitAnimation = exitAnim;
            return this;
        }
        public Builder setPopEnterAnimation(@AnimRes int popEnterAnim) {
            animations.popEnterAnimation = popEnterAnim;
            return this;
        }

        public Builder setPopExitAnimation(@AnimRes int popExitAnim) {
            animations.popExitAnimation = popExitAnim;
            return this;
        }


        public Animations build() {
            return animations;
        }
    }
}
