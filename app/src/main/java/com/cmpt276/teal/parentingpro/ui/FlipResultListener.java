package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmpt276.teal.parentingpro.FlipCoinActivity;
import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.model.Coin;

public class FlipResultListener extends AnimatorListenerAdapter
{
    private final Context context;
    private final ImageView imageViewCoin;

    public FlipResultListener(final Context context, final ImageView imageViewCoin) {
        this.context = context;
        this.imageViewCoin = imageViewCoin;
    }

    // Coin heads image source: https://imgbin.com/png/V0sKs9P6/150th-anniversary-of-canada-toonie-royal-canadian-mint-canadian-dollar-png
    // Coin tails image source: https://imgbin.com/png/Bu9vSHhb/150th-anniversary-of-canada-toonie-loonie-coin-png
    @Override
    public void onAnimationEnd(Animator animation)
    {
        Coin.CoinState flipResult = FlipCoinActivity.coin.getState();
        boolean flipResultIsHeads = (flipResult == Coin.CoinState.HEADS);
        imageViewCoin.setImageResource(flipResultIsHeads ? R.drawable.coin_heads : R.drawable.coin_tails);
        CharSequence outputText = flipResult == Coin.CoinState.HEADS ? context.getText(R.string.head) : context.getText(R.string.tail);
        Toast.makeText(context, "" + outputText, Toast.LENGTH_SHORT).show();
    }
}
