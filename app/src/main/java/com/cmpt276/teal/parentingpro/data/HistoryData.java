package com.cmpt276.teal.parentingpro.data;

import android.graphics.Bitmap;

import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.Coin;
import com.cmpt276.teal.parentingpro.ui.ChildUI;

import java.util.Date;

public class HistoryData
{
    private Child child;
    private Date date;
    private Coin.CoinState chosenState;
    private Coin.CoinState resultState;


    public HistoryData(Child child, Date date, Coin.CoinState chosenState,
                       Coin.CoinState resultState){
        this.child = child;
        this.date = date;
        this.chosenState = chosenState;
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


    public Coin.CoinState getChosenState() {
        return chosenState;
    }


    public void setChosenState(Coin.CoinState chosenState) {
        this.chosenState = chosenState;
    }


    public Coin.CoinState getResultState() {
        return resultState;
    }


    public void setResultState(Coin.CoinState resultState) {
        this.resultState = resultState;
    }


    public HistoryData clone(){
        HistoryData data = new HistoryData(this.child, this.date,
                this.chosenState, this.resultState);
        return data;
    }


    public String toString(){
        return child.toString() + date.toString() + chosenState + resultState;
    }
}
