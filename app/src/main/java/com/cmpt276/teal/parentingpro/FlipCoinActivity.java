package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.cmpt276.teal.parentingpro.ui.ChooseChildPopUpWindow;
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
    private int currentChildIndex;
    private Child currentChildFlipping;

    private ValueAnimator flipAnimator;
    private SoundPool soundPool;
    private int flipSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setParameters();
        setUpFlipButton();
        setUpFlipSound();
        setUpFlipAnimation();
        setUpFlipChoice();
        setupTextPopupMenu();

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
        // DataUtil.writeOneIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX, --lastChildFlippedIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            lastChildFlippedIndex = DataUtil.getIntData(this, AppDataKey.LAST_CHILD_FLIPPED_INDEX);
            setCurrentChildFlipping(lastChildFlippedIndex);
        }
    }

    private void setCurrentChildFlipping(int lastChildFlippedIndex) {
            currentChildIndex = (lastChildFlippedIndex + 1) % childManager.length();
            Log.i("tah", "" + lastChildFlippedIndex);

//            if (lastChildFlippedIndex != -1 && lastChildFlippedIndex < childManager.length() - 1) {
//                lastChildFlippedIndex++;
//            } else {
//                lastChildFlippedIndex = 0;
//            }

           // currentChildFlipping = childManager.getChild(lastChildFlippedIndex);

        currentChildFlipping = childManager.getChild((currentChildIndex));

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
                Intent intent = HistoryActivity.makeLaunchIntent(FlipCoinActivity.this, currentChildIndex);
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

                    setCurrentChildFlipping(++lastChildFlippedIndex);
                    lastChildFlippedIndex = lastChildFlippedIndex % childManager.length();
                    DataUtil.writeOneIntData(FlipCoinActivity.this, AppDataKey.LAST_CHILD_FLIPPED_INDEX, lastChildFlippedIndex);

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
            setupRadioButtonLayout(button);

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

    private void setupRadioButtonLayout(RadioButton button)
    {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{

                        Color.WHITE,
                        Color.WHITE
                }
        );

        button.setButtonTintList(colorStateList);
        button.setTextSize(20);
        button.setTextColor(Color.WHITE);
    }

    private void displayFlipChoice(){
        TextView currentChildText = findViewById(R.id.text_view_flip_choice);
        currentChildText.setText(getString(R.string.flip_choice_text, currentChildFlipping.getName()));
    }

    private void setupTextPopupMenu(){
        final TextView chooseView = findViewById(R.id.text_view_flip_choice);
        chooseView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ChooseChildPopUpWindow popUpWindow = new ChooseChildPopUpWindow(FlipCoinActivity.this);
                View parent = FlipCoinActivity.this.getWindow().getDecorView();
                popUpWindow.showAtLocation(parent, Gravity.CENTER,0,0);

                return false;
            }
        });


    }
}