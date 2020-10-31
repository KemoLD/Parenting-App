package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.Coin;
import com.cmpt276.teal.parentingpro.ui.CoinUI;

import java.util.Date;

public class FlipCoinPage extends AppCompatActivity
{
    private final int FLIP_BTN_ID = R.id.flip_btn;
    private final int HISTORY_BTN_ID = R.id.history_btn;
    private final int FLIP_COIN_IMAGE_ID = R.id.flip_coin_iimage;

    Button flipBtn;     // button to flip coin
    Button historyBtn;  // button to goto history page
    TextView lastChildText; // text view to display last child flipping the coin

    private CoinUI coin;
    private Child currentChild;     // the current child that is fliping the coin
    private Child lastChild;    // the last child who flip the coin
    private Coin.CoinState choosedState;    // the state the child currently choosing

    private History historyList;    // the history record contain history data

    public static Intent getIntent(Context context){
        return new Intent(context, FlipCoinPage.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin2);

        setupVariable();
    }


    private void setupVariable()
    {
        flipBtn = findViewById(FLIP_BTN_ID);
        historyBtn = findViewById(HISTORY_BTN_ID);
        coin = new CoinUI(FlipCoinPage.this, FLIP_COIN_IMAGE_ID);
        historyList = History.getInstance();

        // !!!!!!!! this is just for testing
        currentChild = new Child("Ben");    // the current child set for now first
        choosedState = Coin.CoinState.HEAD;
        // !!!!!!!!!!

        historyList.loadFromLocal(FlipCoinPage.this);
        flipBtn.setOnClickListener(new FlipCoinClickListener());


        historyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = HistoryPage.getIntent(FlipCoinPage.this);
                startActivity(intent);
            }
        });

        displayLastChildFlip();
    }


    private void displayLastChildFlip(){
        int numOfHistory = historyList.numOfHistory();
        if(numOfHistory == 0){
            lastChildText.setText(getString(R.string.no_play));
        }

        updateLastChildPlay();
        lastChildText.setText(lastChild.getName());
    }

    private void updateLastChildPlay(){
        int numOfHistory = historyList.numOfHistory();
        int lastHistoryIndex = numOfHistory - 1;
        HistoryData lastHistoryData = historyList.getHistoryData(lastHistoryIndex);
        lastChild = lastHistoryData.getChild();
    }


    //

    class FlipCoinClickListener implements View.OnClickListener
    {
        public void onClick(View view){
            coin.flipCoin();    // play animation and sound and get randomCoin state
            HistoryData data = new HistoryData(currentChild, new Date(), choosedState, coin.getState());
            historyList.addHistory(data);
            historyList.saveToLocal(FlipCoinPage.this);
            displayLastChildFlip();
        }
    }

}