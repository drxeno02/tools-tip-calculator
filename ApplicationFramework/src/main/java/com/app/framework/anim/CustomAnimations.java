package com.app.framework.anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.app.framework.anim.listeners.OnAnimationPaddingListener;
import com.app.framework.anim.listeners.OnAnimationTranslateListener;
import com.app.framework.anim.listeners.OnAnimationUpdateListener;
import com.app.framework.utilities.FrameworkUtils;

/**
 * Created by leonard on 8/24/2016.
 */
public class CustomAnimations {

    // custom callbacks
    private static OnAnimationTranslateListener mOnAnimationTranslateListener;
    private static OnAnimationPaddingListener mOnAnimationPaddingListener;
    private static OnAnimationUpdateListener mOnAnimationUpdateListener;

    /**
     * Method is used to fade in/out views. Has no start offset time interval
     *
     * @param context   Interface to global information about an application environment
     * @param view      The views to fade
     * @param animation The animation type
     * @param isFadeIn  Toggle true to fade in, otherwise false
     */
    public static void fadeAnimation(Context context, View view, int animation, boolean isFadeIn) {
        fadeAnim(context, view, animation, isFadeIn, 0);
    }

    /**
     * Method is used to fade in/out views. Has start offset time interval
     *
     * @param context    Interface to global information about an application environment
     * @param view       The views to fade
     * @param animation  The animation type
     * @param isFadeIn   Toggle true to fade in, otherwise false
     * @param startDelay Set animation start offset (ms)
     */
    public static void fadeAnimation(Context context, View view, int animation, boolean isFadeIn, long startDelay) {
        fadeAnim(context, view, animation, isFadeIn, startDelay);
    }

    /**
     * Method is used to fade in/out views. Has no start offset time interval and accepts var args
     *
     * @param context   Interface to global information about an application environment
     * @param animation The animation type
     * @param isFadeIn  Toggle true to fade in, otherwise false
     * @param view      The views to fade
     */
    public static void fadeAnimation(Context context, int animation, boolean isFadeIn, View... view) {
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                fadeAnim(context, v, animation, isFadeIn, 0);
            }
        }
    }

    /**
     * Method is used to fade in/out views. Has start offset time interval and accepts var args
     *
     * @param context    Interface to global information about an application environment
     * @param view       The views to fade
     * @param animation  The animation type
     * @param isFadeIn   Toggle true to fade in, otherwise false
     * @param startDelay Set animation start offset (ms)
     */
    public static void fadeAnimation(Context context, int animation, boolean isFadeIn, long startDelay, View... view) {
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                fadeAnim(context, v, animation, isFadeIn, startDelay);
            }
        }
    }

    /**
     * @param context    Interface to global information about an application environment
     * @param view       The views to fade
     * @param animation  The animation type
     * @param isFadeIn   Toggle true to fade in, otherwise false
     * @param startDelay Set animation start offset (ms)
     */
    private static void fadeAnim(Context context, final View view, int animation, final boolean isFadeIn, long startDelay) {
        final Animation fade = AnimationUtils.loadAnimation(context, animation);
        fade.setStartOffset(startDelay);
        fade.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFadeIn) {
                    FrameworkUtils.setViewVisible(view);
                } else {
                    FrameworkUtils.setViewGone(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // do nothing
            }
        });
        view.startAnimation(fade);
    }

    /**
     * Method is used to translate a view by given pixel values for X and Y
     *
     * @param toX        The x-coordinate the view will translate to
     * @param toY        The y-coordinate the view will translate to
     * @param duration   How long the animation will last (milliseconds)
     * @param startDelay Set animation start offset (ms)
     * @param isCallback Toggle for setting callback or not
     * @param view       The view to translate
     */
    public static void translateAnimation(float toX, float toY, long duration, long startDelay, final boolean isCallback, View... view) {
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.animate().setStartDelay(startDelay).x(toX).y(toY).setDuration(duration)
                        .setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // set listener
                        if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationTranslateListener)) {
                            mOnAnimationTranslateListener.onAnimationComplete();
                        }
                    }
                });
            }
        }
    }

    /**
     * @param fromX      The x-coordinate the view will translate from
     * @param toX        The x-coordinate the view will translate to
     * @param fromY      The y-coordinate the view will translate from
     * @param toY        The y-coordinate the view will translate to
     * @param duration   How long the animation will last (milliseconds)
     * @param startDelay Set animation start offset (ms)
     * @param isCallback Toggle for setting callback or not
     * @param view       The view to translate
     */
    public static void translateAnimation(float fromX, final float toX, float fromY, final float toY, final long duration, final long startDelay, final boolean isCallback, View... view) {
        for (final View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.setX(fromX);
                v.setY(fromY);
                v.animate().setStartDelay(startDelay).x(toX).y(toY).setDuration(duration)
                        .setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // set listener
                        if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationTranslateListener)) {
                            mOnAnimationTranslateListener.onAnimationComplete();
                        }
                    }
                });
            }
        }
    }

    /**
     * Method is used to apply a scale animation
     *
     * @param fromX    The x-coordinate to scale from
     * @param toX      The x-coordinate to scale to
     * @param fromY    The y-coordinate to scale from
     * @param toY      The y-cordinate to scale to
     * @param duration How long the animation will last (milliseconds)
     * @param view     The view to translate
     */
    public static void scaleAnimation(float fromX, float toX, float fromY, float toY, long duration, View... view) {
        scaleAnim(fromX, toX, fromY, toY, duration, view);
    }

    /**
     * @param fromX    The x-coordinate to scale from
     * @param toX      The x-coordinate to scale to
     * @param fromY    The y-coordinate to scale from
     * @param toY      The y-cordinate to scale to
     * @param duration How long the animation will last (milliseconds)
     * @param view     The view to translate
     */
    private static void scaleAnim(float fromX, float toX, float fromY, float toY, long duration, View... view) {
        final ScaleAnimation scaleAnim = new ScaleAnimation(fromX, toX, fromY, toY);
        scaleAnim.setDuration(duration);
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.startAnimation(scaleAnim);
            }
        }
    }

    /**
     * Method is used to rotate views
     *
     * @param fromDegrees The degree from which to rotate
     * @param toDegrees   The degree to rotate to
     * @param pivotXValue Specifies how pivotXValue should be interpreted. One of
     *                    Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT
     * @param pivotYValue Specifies how pivotYValue should be interpreted. One of
     *                    Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT
     * @param duration    How long the animation will last (milliseconds)
     * @param isInfinite  True to rotate endlessly, otherwise false
     * @param view        The view to translate
     */
    public static void rotateAnimation(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue,
                                       long duration, boolean isInfinite, View... view) {
        rotateAnim(fromDegrees, toDegrees, pivotXValue, pivotYValue, duration, isInfinite, view);
    }

    /**
     * @param fromDegrees The degree from which to rotate
     * @param toDegrees   The degree to rotate to
     * @param pivotXValue Specifies how pivotXValue should be interpreted. One of
     *                    Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT
     * @param pivotYValue Specifies how pivotYValue should be interpreted. One of
     *                    Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT
     * @param duration    How long the animation will last (milliseconds)
     * @param isInfinite  True to rotate endlessly, otherwise false
     * @param view        The view to translate
     */
    private static void rotateAnim(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue,
                                   long duration, boolean isInfinite, View... view) {
        final RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, pivotXValue,
                Animation.RELATIVE_TO_SELF, pivotYValue);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(duration);
        rotateAnimation.setRepeatCount(isInfinite ? Animation.INFINITE : 1);

        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.startAnimation(rotateAnimation);
            }
        }
    }

    /**
     * Method is used to set callback for when translate animation is complete
     *
     * @param listener Callback for when translate animation is complete
     */
    public static void onAnimationTranslateListener(OnAnimationTranslateListener listener) {
        mOnAnimationTranslateListener = listener;
    }

    /**
     * @param fromPadding The amount of padding the view should have in the beginning of the animation (pixels)
     * @param toPadding   The amount of padding the view will have at the end of the animation (pixels)
     * @param duration    How long the animation will last (milliseconds)
     * @param isCallback  Toggle for setting callback or not
     * @param view        The view whose padding will be modified
     */
    public static void animPadding(int fromPadding, int toPadding, long duration, final boolean isCallback, final View... view) {
        ValueAnimator animator = ValueAnimator.ofInt(fromPadding, toPadding);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (View v : view) {
                    if (!FrameworkUtils.checkIfNull(v)) {
                        if (!FrameworkUtils.checkIfNull(mOnAnimationUpdateListener)) {
                            mOnAnimationUpdateListener.onAnimationUpdate(v, animation);
                        } else {
                            v.setPadding((Integer) animation.getAnimatedValue(),
                                    (Integer) animation.getAnimatedValue(),
                                    (Integer) animation.getAnimatedValue(),
                                    (Integer) animation.getAnimatedValue());
                        }
                    }
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationRepeat(animation);
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * Method is used to set callback for when padding animation is complete
     *
     * @param listener Callback for when padding animation is complete
     */
    public static void onAnimationPaddingListener(OnAnimationPaddingListener listener) {
        mOnAnimationPaddingListener = listener;
    }

    /**
     * Method is used to set callback for when value animator updates
     *
     * @param listener Callback for when value animator updates
     */
    public static void onAnimationUpdateListener(OnAnimationUpdateListener listener) {
        mOnAnimationUpdateListener = listener;
    }
}
