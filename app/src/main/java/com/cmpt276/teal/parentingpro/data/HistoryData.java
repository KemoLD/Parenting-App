package com.cmpt276.teal.parentingpro.data;

import com.cmpt276.teal.parentingpro.model.Coin;

import java.util.Date;

public class HistoryData
{
    private Child child;
    private Date date;
    private Coin coin;

    public HistoryData(Child child, Date date, Coin coin){
        this.child;
        this.date = date;
        this.coin = coin;
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

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public HistoryData clone(){
        HistoryData data = new HistoryData(this.child, this.date, this.coin);
        return data;
    }
}
