package com.cmpt276.teal.parentingpro.ui;

import android.animation.ValueAnimator;
import android.view.View;


// Source: https://stackoverflow.com/a/28430661
public class FlipListener implements ValueAnimator.AnimatorUpdateListener {

    private final View mFrontView;
    private final View mBackView;
    private boolean mFlipped;

    public FlipListener(final View front, final View back) {
        this.mFrontView = front;
        this.mBackView = back;
        this.mBackView.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        final float value = animation.getAnimatedFraction();
        final float scaleValue = 0.625f + (1.5f * (value - 0.5f) * (value - 0.5f));

        if(value <= 0.5f){
            this.mFrontView.setRotationX(180 * value);
            this.mFrontView.setScaleX(scaleValue);
            this.mFrontView.setScaleY(scaleValue);
            if(mFlipped){
                setStateFlipped(false);
            }
        } else {
            this.mBackView.setRotationX(-180 * (1f- value));
            this.mBackView.setScaleX(scaleValue);
            this.mBackView.setScaleY(scaleValue);
            if(!mFlipped){
                setStateFlipped(true);
            }
        }
    }

    private void setStateFlipped(boolean flipped) {
        mFlipped = flipped;
        this.mFrontView.setVisibility(flipped ? View.GONE : View.VISIBLE);
        this.mBackView.setVisibility(flipped ? View.VISIBLE : View.GONE);
    }
}
