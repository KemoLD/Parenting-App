package com.cmpt276.teal.parentingpro.ui;

import android.content.Context;

import com.cmpt276.teal.parentingpro.model.Coin;


/**
 *  the class represent the coin in UI
 */
public class CoinUI extends Coin
{
    private Context context;
    private int id;     // the image view id or other container view id

    public CoinUI(Context context, int id){
        super();
        this.context = context;
        this.id = id;
    }

    public CoinUI(Context context, int id, CoinState state){
        this(context, id);
        setState(state);
    }


    public void flipCoin(){
        super.flipCoin();

        // some code for playing animation empty for now
    }
}
