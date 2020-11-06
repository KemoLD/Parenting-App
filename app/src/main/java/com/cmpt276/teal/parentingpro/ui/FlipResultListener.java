package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.ImageView;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.model.Coin;

public class FlipResultListener extends AnimatorListenerAdapter {
    private final ImageView imageViewCoin;
    private History historyList;

    public FlipResultListener(final ImageView imageViewCoin) {
        this.imageViewCoin = imageViewCoin;
        historyList = History.getInstance();
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        Coin.CoinState flipResult = historyList.getHistoryData(historyList.numOfHistory() - 1).getResultState();
        boolean flipResultIsHeads = (flipResult == Coin.CoinState.HEAD);
        imageViewCoin.setImageResource(flipResultIsHeads ? R.drawable.coin_heads : R.drawable.coin_tails);
    }
}
