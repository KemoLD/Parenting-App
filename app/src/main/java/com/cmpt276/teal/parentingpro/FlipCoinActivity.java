package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
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
    public static Coin coin;
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

        setParameters();
        setUpFlipButton();
        setUpFlipSound();
        setUpFlipAnimation();
        setUpFlipChoice();

        if (!childManager.isEmpty()) {
            displayFlipChoice();
            setUpHistoryButtons();
        } else {
            setUpHistoryButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        DataUtil.writeOneIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX, --lastChildFlippedIndex);
    }

    private void setParameters() {
        // Coin object which the animation will be modelled on
        coin = new Coin();
        flipChoice = Coin.CoinState.HEADS;

        // Child manager containing the list of children (if any)
        childManager = ChildManager.getInstance();
        childManager.loadFromLocal(this);

        // History to keep a record of coin flips
        historyList = History.getInstance();
        historyList.loadFromLocal(this);

        // If there are children, use the index of the child who flipped last in order to get the current child flipping
        if (!childManager.isEmpty()) {
            setCurrentChildFlipping();
        }
    }

    private void setCurrentChildFlipping() {
            lastChildFlippedIndex = DataUtil.getIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX);

            if (lastChildFlippedIndex != -1 && lastChildFlippedIndex < childManager.length() - 1) {
                lastChildFlippedIndex++;
            } else {
                lastChildFlippedIndex = 0;
            }

            currentChildFlipping = childManager.getChild(lastChildFlippedIndex);
            DataUtil.writeOneIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX, lastChildFlippedIndex);
    }

    private void setUpHistoryButton() {
        Button historyButton = findViewById(R.id.history_button_no_children);
        historyButton.setVisibility(View.VISIBLE);
        historyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = HistoryActivity.makeLaunchIntent(FlipCoinActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setUpHistoryButtons() {
        Button historyButton = findViewById(R.id.full_history_button);
        historyButton.setVisibility(View.VISIBLE);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = HistoryActivity.makeLaunchIntent(FlipCoinActivity.this);
                startActivity(intent);
            }
        });

        Button currentChildHistoryButton = findViewById(R.id.current_child_history_button);
        currentChildHistoryButton.setText(getString(R.string.current_child_history_text, currentChildFlipping.getName()));
        currentChildHistoryButton.setVisibility(View.VISIBLE);
        currentChildHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = HistoryActivity.makeLaunchIntent(FlipCoinActivity.this, lastChildFlippedIndex);
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
                flipAnimator.start();

                if (!childManager.isEmpty()) {
                    HistoryData data = new HistoryData(currentChildFlipping, new Date(), flipChoice, coin.getState());
                    historyList.addHistory(data);
                    historyList.saveToLocal(FlipCoinActivity.this);

                    setCurrentChildFlipping();

                    Button currentChildHistoryButton = findViewById(R.id.current_child_history_button);
                    currentChildHistoryButton.setText(getString(R.string.current_child_history_text, currentChildFlipping.getName()));

                    displayFlipChoice();
                }
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
        RadioGroup radioGroup = findViewById(R.id.radio_group_flip_choice);
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

            radioGroup.addView(button);

            if (finalI == 0) {
                button.setChecked(true);
            }
        }
    }

    private void displayFlipChoice(){
        TextView currentChildText = findViewById(R.id.text_view_flip_choice);
        currentChildText.setText(getString(R.string.flip_choice_text, currentChildFlipping.getName()));
    }
}