package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.media.SoundPool;
import android.widget.Toast;

public class FlipSoundListener extends AnimatorListenerAdapter {
    private final Context context;
    private SoundPool soundPool;
    private int flipSound;
    private int flipCoinStreamID;

    public FlipSoundListener(final Context context, SoundPool soundPool, int flipSound) {
        this.context = context;
        this.soundPool = soundPool;
        this.flipSound= flipSound;
    }

    // Sound source: https://freesound.org/people/bone666138/sounds/198877/
    // license link: https://creativecommons.org/licenses/by/3.0/
    @Override
    public void onAnimationStart(Animator animation) {
        flipCoinStreamID = soundPool.play(flipSound, 1, 1, 1, 0, 1);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        soundPool.stop(flipCoinStreamID);
    }
}
