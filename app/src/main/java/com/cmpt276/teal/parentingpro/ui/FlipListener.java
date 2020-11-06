package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

import com.cmpt276.teal.parentingpro.model.Coin;


// Source: https://stackoverflow.com/a/28430661
public class FlipListener implements ValueAnimator.AnimatorUpdateListener {

    private final View mHeadsView;
    private final View mTailsView;
    private boolean mFlipped;

    public FlipListener(final View heads, final View tails) {
        this.mHeadsView = heads;
        this.mTailsView = tails;
        this.mTailsView.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        final float value = animation.getAnimatedFraction();
        final float scaleValue = 0.625f + (1.5f * (value - 0.5f) * (value - 0.5f));

        if(value <= 0.5f){
            this.mHeadsView.setRotationX(180 * value);
            this.mHeadsView.setScaleX(scaleValue);
            this.mHeadsView.setScaleY(scaleValue);
            if(mFlipped){
                setStateFlipped(false);
            }
        } else {
            this.mTailsView.setRotationX(-180 * (1f- value));
            this.mTailsView.setScaleX(scaleValue);
            this.mTailsView.setScaleY(scaleValue);
            if(!mFlipped){
                setStateFlipped(true);
            }
        }
    }

    private void setStateFlipped(boolean flipped) {
        mFlipped = flipped;
        this.mHeadsView.setVisibility(flipped ? View.GONE : View.VISIBLE);
        this.mTailsView.setVisibility(flipped ? View.VISIBLE : View.GONE);
    }
}
