package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;

public class ScaleAnimator extends ValueAnimator
{
    private final View animatedView;
    private float cancelScaleValue;

    public ScaleAnimator(View v, float... values){
        this.setFloatValues(values);
        this.animatedView = v;
        this.addUpdateListener(new ScaleUpdateListener());
        this.addListener(new ScaleStateListener());
    }

    public float getCancelScaleValue() {
        return cancelScaleValue;
    }

    public void setCancelScaleValue(float cancelScaleValue) {
        this.cancelScaleValue = cancelScaleValue;
    }

    class ScaleUpdateListener implements AnimatorUpdateListener
    {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float factor = (float)valueAnimator.getAnimatedValue();
            animatedView.setScaleX(factor);
            animatedView.setScaleY(factor);
        }
    }


    class ScaleStateListener extends AnimatorListenerAdapter
    {
        @Override
        public void onAnimationCancel(Animator animator)
        {
            // set the view to its original size
            animatedView.setScaleX(cancelScaleValue);
            animatedView.setScaleY(cancelScaleValue);
        }
    }

}
