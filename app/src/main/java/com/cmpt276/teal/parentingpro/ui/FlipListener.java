package com.cmpt276.teal.parentingpro.ui;

import android.animation.ValueAnimator;
import android.widget.ImageView;

import com.cmpt276.teal.parentingpro.R;

// Adapted from: https://stackoverflow.com/a/28430661

/**
 * the class use for the Animation in the flip coin activity
 */
public class FlipListener implements ValueAnimator.AnimatorUpdateListener
{
    private final ImageView imageViewCoin;
    private boolean flipped;

     public FlipListener(final ImageView imageViewCoin) {
         this.imageViewCoin = imageViewCoin;
     }

     @Override
     public void onAnimationUpdate(final ValueAnimator animation) {
         final float value = animation.getAnimatedFraction();

         if(value <= 0.5f){
             imageViewCoin.setRotationX(180 * value);
             if(flipped){
                 setStateFlipped(false);
             }
         } else {
             imageViewCoin.setRotationX(-180 * (1f - value));
             if(!flipped){
                 setStateFlipped(true);
             }
         }
     }

     private void setStateFlipped(boolean flipped) {
         this.flipped = flipped;
         imageViewCoin.setImageResource(flipped ? R.drawable.coin_heads : R.drawable.coin_tails);
     }
}

