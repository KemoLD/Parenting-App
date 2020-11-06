package com.cmpt276.teal.parentingpro.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

import com.cmpt276.teal.parentingpro.FlipCoinPage;
import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.model.Coin;

public class FlipResult implements ValueAnimator.AnimatorListener {
    private final View mHeadsView;
    private final View mTailsView;
    private History historyList;    // the history record contain history data

    public FlipResult(final View heads, final View tails) {
        this.mHeadsView = heads;
        this.mTailsView = tails;
        historyList = History.getInstance();
    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        Coin.CoinState flipResult = historyList.getHistoryData(historyList.numOfHistory() - 1).getResultState();
        boolean flipResultIsHeads = (flipResult == Coin.CoinState.HEAD);
        mHeadsView.setVisibility(flipResultIsHeads ? View.VISIBLE : View.GONE);
        mTailsView.setVisibility(flipResultIsHeads ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
