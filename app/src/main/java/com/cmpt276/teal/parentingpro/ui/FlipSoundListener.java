package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.media.SoundPool;


/**
 * the class use for the animation in flip coin activity
 */
public class FlipSoundListener extends AnimatorListenerAdapter
{
    private final SoundPool soundPool;
    private int flipSound;
    private int flipCoinStreamID;

    public FlipSoundListener(final SoundPool soundPool, int flipSound) {
        this.soundPool = soundPool;
        this.flipSound= flipSound;
    }


    @Override
    public void onAnimationStart(Animator animation) {
        flipCoinStreamID = soundPool.play(flipSound, 1, 1, 1, 0, 1);
    }


    @Override
    public void onAnimationEnd(Animator animation) {
        soundPool.stop(flipCoinStreamID);
    }
}
