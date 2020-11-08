package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.model.Coin;
import com.cmpt276.teal.parentingpro.ui.FlipListener;
import com.cmpt276.teal.parentingpro.ui.FlipResultListener;
import com.cmpt276.teal.parentingpro.ui.FlipSoundListener;

import java.util.Date;

public class FlipCoinActivity extends AppCompatActivity
{
    private Coin coin;
    private Coin.CoinState flipChoice;
    private Coin.CoinState[] validFlipChoices = {Coin.CoinState.HEADS, Coin.CoinState.TAILS};

    private History historyList;
    private ChildManager childManager;
    private int lastChildFlippedIndex;
    private Child currentChildFlipping;

    private ValueAnimator flipAnimator;
    private SoundPool soundPool;
    private int flipSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setUpParameters();
        setUpHistoryButton();

        setUpFlipButton();
        setUpFlipSound();
        setUpFlipAnimation();
        setUpFlipChoice();

        if (!childManager.isEmpty()) {
            displayFlipChoice();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }

    private void setUpParameters() {
        // Coin object which the animation will be modelled on
        coin = new Coin();

        // Child manager containing the list of children (if any)
        childManager = ChildManager.getInstance();

        // History to keep a record of coin flips
        historyList = History.getInstance();
        historyList.loadFromLocal(FlipCoinActivity.this);

        // *************NEED TO GET LIST OF CHILDREN IN ORDER TO KEEP TRACK OF CURRENT CHILD FLIPPING AND NEXT CHILD WHO WILL GET FLIP CHOICE*************
        // if list not empty:
        if (!childManager.isEmpty()) {
            lastChildFlippedIndex = DataUtil.getIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX);

            if (lastChildFlippedIndex != -1 && lastChildFlippedIndex != childManager.length() - 1) {
                lastChildFlippedIndex++;
            } else {
                lastChildFlippedIndex = 0;
            }

            currentChildFlipping = childManager.getChild(lastChildFlippedIndex);
            DataUtil.writeOneIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX, lastChildFlippedIndex);
        }
    }

    private void setUpHistoryButton() {
        Button historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = HistoryActivity.getIntent(FlipCoinActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setUpFlipButton() {
        Button flipButton = findViewById(R.id.flip_button);
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coin.flipCoin();
                HistoryData data = new HistoryData(currentChildFlipping, new Date(), flipChoice, coin.getState());
                historyList.addHistory(data);
                historyList.saveToLocal(FlipCoinActivity.this);
                flipAnimator.start();
                displayFlipChoice();
            }
        });
    }

    private void setUpFlipSound() {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .build();

        //   Sound source: https://freesound.org/people/bone666138/sounds/198877/
        // & license link: https://creativecommons.org/licenses/by/3.0/
        flipSound = soundPool.load(FlipCoinActivity.this, R.raw.coin_flip_sound, 1);
    }

    private void setUpFlipAnimation() {
        ImageView imageViewCoin = findViewById(R.id.image_view_coin);
        int repeatCount = 4;

        flipAnimator = ValueAnimator.ofFloat(0f, 1f);
        flipAnimator.addUpdateListener(new FlipListener(imageViewCoin));
        flipAnimator.addListener(new FlipResultListener(FlipCoinActivity.this, imageViewCoin));
        flipAnimator.addListener(new FlipSoundListener(soundPool, flipSound));
        flipAnimator.setDuration(200);
        flipAnimator.setRepeatCount(repeatCount);
    }

    private void setUpFlipChoice() {
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

    private void displayFlipChoice(){
        TextView lastChildText = findViewById(R.id.text_view_flip_choice);
        //updateLastChildPlay(); // ************ NOT NEEDED WHEN LIST OF CHILDREN READY (JUST HAVE TO CYCLE THROUGH THE LIST)
        lastChildText.setText(getString(R.string.flip_choice_text, currentChildFlipping.getName()));
    }

    // ************* NEED TO CHANGE THIS METHOD AFTER GETTING LIST OF CHILDREN **********************
//    private void updateLastChildPlay() {
//        int numOfHistory = historyList.numOfHistory();
//        int lastHistoryIndex = numOfHistory - 1;
//        HistoryData lastHistoryData = historyList.getHistoryData(lastHistoryIndex);
//        nextChildFlipping = lastHistoryData.getChild();
//    }
}