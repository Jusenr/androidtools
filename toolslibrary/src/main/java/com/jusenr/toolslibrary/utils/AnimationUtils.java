package com.jusenr.toolslibrary.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Animation tool class
 * Created by guchenkai on 2016/1/21.
 */
public final class AnimationUtils {
    /**
     * Default animation duration
     */
    public static final long DEFAULT_ANIMATION_DURATION = 400;


    /**
     * Get a spin animation
     *
     * @param fromDegrees       start angle
     * @param toDegrees         end angle
     * @param pivotXType        Rotation center point X axis coordinate relative type
     * @param pivotXValue       Rotation center point X axis coordinate
     * @param pivotYType        Rotation center point Y axis coordinate relative type
     * @param pivotYValue       Rotation center point Y axis coordinate
     * @param durationMillis    duration
     * @param animationListener Animation monitor
     * @return A rotating animation
     */
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long durationMillis, Animation.AnimationListener animationListener) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,
                toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        rotateAnimation.setDuration(durationMillis);
        if (animationListener != null)
            rotateAnimation.setAnimationListener(animationListener);
        return rotateAnimation;
    }

    /**
     * Gets an animation that rotates according to the center of the view itself
     *
     * @param durationMillis    animation duration
     * @param animationListener Animation monitor
     * @return An animation that rotates according to the center
     */
    public static RotateAnimation getRotateAnimationByCenter(long durationMillis, Animation.AnimationListener animationListener) {
        return getRotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, durationMillis,
                animationListener);
    }

    /**
     * Gets an animation that rotates according to the center point
     *
     * @param duration animation duration
     * @return An animation that rotates according to the center
     */
    public static RotateAnimation getRotateAnimationByCenter(long duration) {
        return getRotateAnimationByCenter(duration, null);
    }

    /**
     * Gets an animation that rotates according to the center of the view itself
     *
     * @param animationListener Animation monitor
     * @return An animation that rotates according to the center
     */
    public static RotateAnimation getRotateAnimationByCenter(Animation.AnimationListener animationListener) {
        return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION,
                animationListener);
    }

    /**
     * Gets an animation that rotates according to the center of the view itself
     *
     * @return An animation that rotates according to the center, The default duration is 400ms.
     */
    public static RotateAnimation getRotateAnimationByCenter() {
        return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION, null);
    }

    /**
     * Gets a transparency gradient animation
     *
     * @param fromAlpha         Transparency at the beginning
     * @param toAlpha           Transparency at the end
     * @param durationMillis    duration
     * @param animationListener Animation monitor
     * @return A transparency gradient animation
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis, Animation.AnimationListener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(durationMillis);
        if (animationListener != null)
            alphaAnimation.setAnimationListener(animationListener);
        return alphaAnimation;
    }

    /**
     * Gets a transparency gradient animation
     *
     * @param fromAlpha      Transparency at the beginning
     * @param toAlpha        Transparency at the end
     * @param durationMillis duration
     * @return A transparency gradient animation
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
        return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, null);
    }

    /**
     * Gets a transparency gradient animation
     *
     * @param fromAlpha         Transparency at the beginning
     * @param toAlpha           Transparency at the end
     * @param animationListener Animation monitor
     * @return A transparency gradient animation, The default duration is 400ms.
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, Animation.AnimationListener animationListener) {
        return getAlphaAnimation(fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION, animationListener);
    }

    /**
     * Gets a transparency gradient animation
     *
     * @param fromAlpha Transparency at the beginning
     * @param toAlpha   Transparency at the end
     * @return A transparency gradient animation, The default duration is 400ms.
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
        return getAlphaAnimation(fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION, null);
    }

    /**
     * Gets a transparency gradient animation that changes from full display to invisible.
     *
     * @param durationMillis    duration
     * @param animationListener Animation monitor
     * @return A transparent gradient animation that changes from full display to invisible.
     */
    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        return getAlphaAnimation(1.0f, 0.0f, durationMillis, animationListener);
    }

    /**
     * Gets a transparency gradient animation that changes from full display to invisible.
     *
     * @param durationMillis duration
     * @return A transparent gradient animation that changes from full display to invisible.
     */
    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis) {
        return getHiddenAlphaAnimation(durationMillis, null);
    }

    /**
     * Gets a transparency gradient animation that changes from full display to invisible.
     *
     * @param animationListener Animation monitor
     * @return A transparent gradient animation that changes from full display to invisible, The default duration is 400ms.
     */
    public static AlphaAnimation getHiddenAlphaAnimation(Animation.AnimationListener animationListener) {
        return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, animationListener);
    }

    /**
     * Gets a transparency gradient animation that changes from full display to invisible.
     *
     * @return A transparent gradient animation that changes from full display to invisible,The default duration is 400ms.
     */
    public static AlphaAnimation getHiddenAlphaAnimation() {
        return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, null);
    }

    /**
     * Gets a transparency gradient animation that changes from invisible to full display.
     *
     * @param durationMillis    duration
     * @param animationListener Animation monitor
     * @return A transparent gradient animation that changes from invisible to full display.
     */
    public static AlphaAnimation getShowAlphaAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, animationListener);
    }

    /**
     * Gets a transparency gradient animation that changes from invisible to full display.
     *
     * @param durationMillis duration
     * @return A transparent gradient animation that changes from invisible to full display.
     */
    public static AlphaAnimation getShowAlphaAnimation(long durationMillis) {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, null);
    }

    /**
     * Gets a transparency gradient animation that changes from invisible to full display.
     *
     * @param animationListener Animation monitor
     * @return A transparent gradient animation that changes from invisible to full display, The default duration is 400ms.
     */
    public static AlphaAnimation getShowAlphaAnimation(Animation.AnimationListener animationListener) {
        return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, animationListener);
    }

    /**
     * Gets a transparency gradient animation that changes from invisible to full display.
     *
     * @return A transparent gradient animation that changes from invisible to full display, The default duration is 400ms.
     */
    public static AlphaAnimation getShowAlphaAnimation() {
        return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, null);
    }

    /**
     * Get a shrink animation
     *
     * @param durationMillis    duration
     * @param animationListener Animation monitor
     * @return A shrink animation
     */
    public static ScaleAnimation getLessenScaleAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                0.0f, ScaleAnimation.RELATIVE_TO_SELF,
                ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    /**
     * Get a shrink animation
     *
     * @param durationMillis duration
     * @return A shrink animation
     */
    public static ScaleAnimation getLessenScaleAnimation(long durationMillis) {
        return getLessenScaleAnimation(durationMillis, null);
    }

    /**
     * Get a shrink animation
     *
     * @param animationListener Animation monitor
     * @return A shrink animation
     */
    public static ScaleAnimation getLessenScaleAnimation(Animation.AnimationListener animationListener) {
        return getLessenScaleAnimation(DEFAULT_ANIMATION_DURATION, animationListener);
    }

    /**
     * Get an enlarged animation
     *
     * @param durationMillis    duration
     * @param animationListener Animation monitor
     * @return An enlarged animation
     */
    public static ScaleAnimation getAmplificationAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
                1.0f, ScaleAnimation.RELATIVE_TO_SELF,
                ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    /**
     * Get an enlarged animation
     *
     * @param durationMillis duration
     * @return An enlarged animation
     */
    public static ScaleAnimation getAmplificationAnimation(long durationMillis) {
        return getAmplificationAnimation(durationMillis, null);
    }

    /**
     * Get an enlarged animation
     *
     * @param animationListener Animation monitor
     * @return An enlarged animation
     */
    public static ScaleAnimation getAmplificationAnimation(Animation.AnimationListener animationListener) {
        return getAmplificationAnimation(DEFAULT_ANIMATION_DURATION, animationListener);
    }

    /**
     * Get an animation collection
     *
     * @return Animation collection
     */
    public static AnimationSet getAnimationSet() {
        return new AnimationSet(true);
    }

}
