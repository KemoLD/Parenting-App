package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.Coin;
import com.cmpt276.teal.parentingpro.ui.CoinUI;
import com.cmpt276.teal.parentingpro.ui.FlipListener;
import com.cmpt276.teal.parentingpro.ui.FlipResult;

import java.util.Date;

public class FlipCoinPage extends AppCompatActivity
{
    private final int FLIP_BTN_ID = R.id.flip_btn;
    private final int HISTORY_BTN_ID = R.id.history_btn;
    private final int COIN_HEAD_IMAGE_ID = R.id.coin_head_image;
    private final int COIN_TAIL_IMAGE_ID = R.id.coin_tail_image;
    private final int LAST_CHILD_ID = R.id.flip_coin_last_play;

    private Button flipBtn;     // button to flip coin
    private Button historyBtn;  // button to goto history page
    private TextView lastChildText; // text view to display last child flipping the coin

    private Coin coin;
    private Child currentChild;     // the current child that is flipping the coin
    private Child lastChild;    // the last child who flip the coin
    private Coin.CoinState flipChoice;    // the state the child currently choosing
    private Coin.CoinState[] validFlipChoices = {Coin.CoinState.HEAD, Coin.CoinState.TAIL};
    private History historyList;    // the history record contain history data

    private ValueAnimator mFlipAnimator;


    public static Intent getIntent(Context context){
        return new Intent(context, FlipCoinPage.class);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin2);

        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        ImageView imageViewHeads = findViewById(COIN_HEAD_IMAGE_ID);
        ImageView imageViewTails = findViewById(COIN_TAIL_IMAGE_ID);
        mFlipAnimator.addUpdateListener(new FlipListener(imageViewHeads, imageViewTails));
        mFlipAnimator.addListener(new FlipResult(imageViewHeads, imageViewTails));
        mFlipAnimator.setRepeatCount(8);
        setupVariable();
        createFlipChoice();
    }



    private void setupVariable()
    {
        flipBtn = findViewById(FLIP_BTN_ID);
        historyBtn = findViewById(HISTORY_BTN_ID);
        lastChildText = findViewById(LAST_CHILD_ID);
        coin = new Coin();
        historyList = History.getInstance();

        // !!!!!!!! this is just for testing
        currentChild = new Child("Ben");    // the current child set for now first
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

        // no history is stored
        if(numOfHistory <= 0){
            lastChildText.setText(getString(R.string.no_play));
            return;
        }
        updateLastChildPlay();
        lastChildText.setText(lastChild.getName() + "?");
    }



    private void updateLastChildPlay(){
        int numOfHistory = historyList.numOfHistory();
        int lastHistoryIndex = numOfHistory - 1;
        HistoryData lastHistoryData = historyList.getHistoryData(lastHistoryIndex);
        lastChild = lastHistoryData.getChild();
    }


    /**
     * this inner class is for action that when click the flip coin button
     */
    class FlipCoinClickListener implements View.OnClickListener
    {
        public void onClick(View view){
            coin.flipCoin();    // play animation and sound and get randomCoin state
            HistoryData data = new HistoryData(currentChild, new Date(), flipChoice, coin.getState());
            historyList.addHistory(data);
            historyList.saveToLocal(FlipCoinPage.this);
            mFlipAnimator.start();
            displayLastChildFlip();

            // !!!! testing what state is flip
            Toast.makeText(FlipCoinPage.this, "" + coin.getState(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createFlipChoice() {
        RadioGroup group = findViewById(R.id.radio_group_flip_choice);
        String[] flipChoices = getResources().getStringArray(R.array.flip_choices);

        for (int i = 0; i < flipChoices.length; i++) {
            final int finalI = i;
            RadioButton button = new RadioButton(this);
            button.setText(flipChoices[i]);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flipChoice = validFlipChoices[finalI];
                }
            });

            group.addView(button);

        }
    }

}