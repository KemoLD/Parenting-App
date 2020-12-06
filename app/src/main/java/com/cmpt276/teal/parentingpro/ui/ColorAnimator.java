package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ColorAnimator extends ValueAnimator
{
    private final View animatedView;
    private int cancleColorValue;
    private ArgbEvaluator argbEvaluator;

    public ColorAnimator(View v, int... values){
        this.setIntValues(values);
        this.animatedView = v;
        this.addUpdateListener(new ColorAnimator.ColorUpdateListener());
        this.addListener(new ColorAnimator.ColorStateListener());
        this.argbEvaluator = new ArgbEvaluator();
        this.setEvaluator(argbEvaluator);
    }

    public int getCancleColorValue() {
        return cancleColorValue;
    }

    public void setCancleColorValue(int cancleColorValue) {
        this.cancleColorValue = cancleColorValue;
    }

    class ColorUpdateListener implements AnimatorUpdateListener
    {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int factor = (int)valueAnimator.getAnimatedValue();
            System.out.println(factor);
            Drawable drawable = animatedView.getBackground();
            drawable.setColorFilter(factor, PorterDuff.Mode.SRC_OVER);
            // animatedView.setBackgroundColor(factor);
        }
    }


    class ColorStateListener extends AnimatorListenerAdapter
    {
        @Override
        public void onAnimationCancel(Animator animator)
        {
            // set the view to its original color
            animatedView.getBackground().setColorFilter(cancleColorValue, PorterDuff.Mode.SRC_OVER);
        }
    }
}
