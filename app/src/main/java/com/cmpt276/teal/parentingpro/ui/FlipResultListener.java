package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.model.Coin;

public class FlipResultListener extends AnimatorListenerAdapter
{
    private final Context context;
    private final ImageView imageViewCoin;
    private History historyList;


    public FlipResultListener(final Context context, final ImageView imageViewCoin) {
        this.context = context;
        this.imageViewCoin = imageViewCoin;
        historyList = History.getInstance();
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        Coin.CoinState flipResult = historyList.getHistoryData(historyList.numOfHistory() - 1).getResultState();
        boolean flipResultIsHeads = (flipResult == Coin.CoinState.HEADS);
        imageViewCoin.setImageResource(flipResultIsHeads ? R.drawable.coin_heads : R.drawable.coin_tails);
        Toast.makeText(context, "" + flipResult, Toast.LENGTH_SHORT).show();
    }
}
