package com.cmpt276.teal.parentingpro.model;

/**
 * the class represent the coin we need to flip
 */
public class Coin
{
    public enum CoinState
    {
        HEAD, TAIL
    }

    private CoinState state;

    // set state to random
    public Coin(){
        flipCoin();
    }

    // set state
    public Coin(CoinState state){
        this.state = state;
    }

    public CoinState getState() {
        return state;
    }

    public void setState(CoinState state) {
        this.state = state;
    }


    /**
     * set the coin state to random either HEAD or TAIL
     */
    public void flipCoin(){
        this.setState(getRandomState());
    }


    /**
     * get a random state for the coin
     * @return random state for the coin
     */
    public CoinState getRandomState(){
        int random = (int)Math.random() * 10;   // get random number from 0 - 9

        CoinState output;
        if(random < 5)
            output = CoinState.HEAD;
        else
            output = CoinState.TAIL;

        return output;
    }

}
