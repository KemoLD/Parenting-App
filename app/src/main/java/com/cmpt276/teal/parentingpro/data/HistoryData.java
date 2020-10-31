package com.cmpt276.teal.parentingpro.data;

import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.Coin;

import java.util.Date;

public class HistoryData
{
    private Child child;
    private Date date;
    private Coin.CoinState choosedState;
    private Coin.CoinState resultState;



    public HistoryData(Child child, Date date, Coin.CoinState choosedState, Coin.CoinState resultState){
        this.child = child;
        this.date = date;
        this.choosedState = choosedState;
        this.resultState = resultState;
    }



    public Child getChild() {
        return child;
    }



    public void setChild(Child child) {
        this.child = child;
    }



    public Date getDate() {
        return date;
    }



    public void setDate(Date date) {
        this.date = date;
    }



    public Coin.CoinState getChoosedState() {
        return choosedState;
    }



    public void setChoosedState(Coin.CoinState choosedState) {
        this.choosedState = choosedState;
    }



    public Coin.CoinState getResultState() {
        return resultState;
    }



    public void setResultState(Coin.CoinState resultState) {
        this.resultState = resultState;
    }



    public HistoryData clone(){
        HistoryData data = new HistoryData(this.child, this.date, this.choosedState, this.resultState);
        return data;
    }



    public String toString(){
        return child.toString() + date.toString() + choosedState + resultState;
    }
}
