package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.ui.ColorAnimator;
import com.cmpt276.teal.parentingpro.ui.ScaleAnimator;

import java.util.ArrayList;

public class TakeBreathActivity extends AppCompatActivity {

    private boolean isRun;
    private TextView tvDesc;
    private TextView tvHelp;
    private Button breathBtn;
    private SeekBar seekbar;
    private TextView seekbarText;

    private ScaleAnimator scaleUpAnimator;
    private ScaleAnimator scaleDownAnimator;
    private ColorAnimator breathInColorAnimator;
    private ColorAnimator breathOutColorAnimator;

    private AnimatorSet animationIn;
    private AnimatorSet animationOut;

    public static final int MIN_BREATH = 1;
    public static final int MAX_BREATH = 10;
    // public static final int DEFAULT_BREATH = 3;

    private static final float MIN_BUTTON_SCALE = 1;
    private static final float MAX_BUTTON_SCALE = 4;
    private static final long SCALE_DURATION = 10000;
    private static final int START_COLOR = Color.parseColor("#00ab09");
    private static final int END_COLOR = Color.parseColor("#00597d");

    private SoundPool mSoundPool;
    private int audioIn;
    private int audioOut;
    private int streamId;

    private int N;

    class BreathState{
        public boolean isPress;
        public boolean inState;
        private long pressTime;

        public void startInhale() {
            mSoundPool.stop(streamId);
            if(isPress){
                finish(true);
            }
            isPress = true;
            pressTime = System.currentTimeMillis();
            inState = true;
            disableSeekbar();
            breathBtn.setText(getText(R.string.in));
            transFormAnimation(animationOut, animationIn, MAX_BUTTON_SCALE, END_COLOR);
            streamId = mSoundPool.play(audioIn, 1, 1, 8, 0, 1);
        }

        public void finish(boolean cancelSound){
            cancelAnimationIn();
            isPress = false;
            if(cancelSound)
                mSoundPool.stop(streamId);
        }

        public void stopInhale(){
            if(inState) {
                finish(true);
            }
            if(breathState.satifyState()){
                startExhale();
            }
        }

        public boolean satifyState(){
            long time = System.currentTimeMillis();
            return time - pressTime > 3000;
        }

        public void startExhale(){
            mSoundPool.stop(streamId);
            isPress = true;
            inState = false;
            pressTime = System.currentTimeMillis();
            breathBtn.setText(R.string.out);
            breathBtn.setEnabled(false);
            N--;
            String outputText = N == 0 ? getString(R.string.good_job) : getString(R.string.let_take_breath, N);
            tvDesc.setText(outputText);
            tvHelp.setText(R.string.btn_disable);
            transFormAnimation(animationIn, animationOut, MIN_BUTTON_SCALE, START_COLOR);
            streamId = mSoundPool.play(audioOut, 1, 1, 8, 0, 1);
        }

        public void inBeathing(long time){
            long cost = time - breathState.pressTime;
            if(cost > 3000){
                if(inState) {
                    tvHelp.setText(R.string.release_breath_out);
                    breathBtn.setText(R.string.out);
                }else{
                    breathBtn.setText(R.string.in);
                    tvHelp.setText(R.string.hold_breath_in);
                    if(!breathBtn.isEnabled()) {
                        breathBtn.setEnabled(true);
                        // tvDesc.setText(getString(R.string.let_take_breath, N));
                        if (N == 0) {
                            N = seekbar.getProgress() + MIN_BREATH;
                            tvDesc.setText(getString(R.string.let_take_breath, N));
                            enableSeekbar();
                            breathBtn.setText(R.string.good_job);
                            finish(false);
                        }
                    }

                }
            }
            if(cost > 10000){
                finish(true);
            }
        }

        public void reset(){
            tvDesc.setText(getString(R.string.let_take_breath, N));
            breathBtn.setText(R.string.begin);
            enableSeekbar();
            inState = true;
            isPress = false;
        }
    }

    private BreathState breathState = new BreathState();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long time = System.currentTimeMillis();
                    breathState.inBeathing(time);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        N = getNumberOfBreath();
        breathState.reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
        isRun = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_take_breath);

        N = getNumberOfBreath();

        tvDesc = findViewById(R.id.tv_desc);
        tvHelp = findViewById(R.id.tv_help);
        seekbar = findViewById(R.id.breath_seekbar);
        seekbarText = findViewById(R.id.seekbar_text);
        breathBtn = findViewById(R.id.btn_breath);

        tvDesc.setText(getString(R.string.let_take_breath, N));
        tvHelp.setText(R.string.hold_breath_in);

        setupSeekbar();
        setAnimations();
        setSound();

        breathState.reset();

        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    if (breathState.isPress) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        setBreathButtonTouch();


    }


    private int getNumberOfBreath(){
        int breathNum = DataUtil.getIntData(this, AppDataKey.BREATH_INHALE);
        if(breathNum <= -1){
            breathNum = 3;
            DataUtil.writeOneIntData(this, AppDataKey.BREATH_INHALE, breathNum);
        }
        return breathNum;
    }


    private void setupSeekbar(){
        seekbar.setMax(MAX_BREATH - MIN_BREATH);
        seekbar.setProgress(N - MIN_BREATH);
        seekbarText.setText("" + N);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = MIN_BREATH + progress;
                seekbarText.setText("" + progress);
                N = progress;
                tvDesc.setText(getString(R.string.let_take_breath, N));
                DataUtil.writeOneIntData(TakeBreathActivity.this, AppDataKey.BREATH_INHALE, N);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void setAnimations(){

        scaleUpAnimator = new ScaleAnimator(breathBtn, MIN_BUTTON_SCALE, MAX_BUTTON_SCALE);
        scaleDownAnimator = new ScaleAnimator(breathBtn, MAX_BUTTON_SCALE, MIN_BUTTON_SCALE);

        breathInColorAnimator = new ColorAnimator(breathBtn, START_COLOR, END_COLOR);
        breathOutColorAnimator = new ColorAnimator(breathBtn, END_COLOR, START_COLOR);

        animationIn = new AnimatorSet();
        animationOut = new AnimatorSet();
        animationIn.playTogether(scaleUpAnimator, breathInColorAnimator);
        animationIn.setDuration(SCALE_DURATION);

        animationOut.playTogether(scaleDownAnimator, breathOutColorAnimator);
        animationOut.setDuration(SCALE_DURATION);
    }


    private void transFormAnimation(AnimatorSet from, AnimatorSet to, float scaleValue, int colorValue){
        ArrayList<Animator> fromAnimatorList = from.getChildAnimations();
        ArrayList<Animator> toAnimatorList = to.getChildAnimations();

        ScaleAnimator fromScale = (ScaleAnimator) fromAnimatorList.get(0);
        ScaleAnimator toScale = (ScaleAnimator) toAnimatorList.get(0);
        ColorAnimator fromColor = (ColorAnimator) fromAnimatorList.get(1);
        ColorAnimator toColor = (ColorAnimator) toAnimatorList.get(1);

        float scaleSize = getScaleSizeFromAnimation(fromScale);
        int color = (int)fromColor.getAnimatedValue();
        from.cancel();      // cancel value doest matter this point because to animation is going to be start
        toScale.setFloatValues(scaleSize, scaleValue);
        toColor.setIntValues(color, colorValue);
        to.start();
    }


    private void cancelAnimationIn(){
        int color = (int) breathOutColorAnimator.getAnimatedValue();
        float scaleSize = getScaleSizeFromAnimation(scaleDownAnimator);
        scaleUpAnimator.setCancelScaleValue(scaleSize);
        breathInColorAnimator.setCancleColorValue(color);
        animationIn.cancel();
    }


    private float getScaleSizeFromAnimation(ScaleAnimator animation){
        float scaleSize = (float)animation.getAnimatedValue();
        scaleSize = scaleSize > MAX_BUTTON_SCALE ? MAX_BUTTON_SCALE : scaleSize;
        scaleSize = scaleSize < MIN_BUTTON_SCALE ? MIN_BUTTON_SCALE : scaleSize;
        return scaleSize;
    }


    private void setSound(){
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setLegacyStreamType(AudioManager.STREAM_NOTIFICATION);
        builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);

        SoundPool.Builder builder2 = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(builder.build());
        mSoundPool = builder2.build();
        audioIn = mSoundPool.load(this, R.raw.breath_in, 1);
        audioOut = mSoundPool.load(this, R.raw.breath_out, 1);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setBreathButtonTouch(){
        breathBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN && N > 0){
                    breathState.startInhale();
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(breathState.inState) {
                        breathState.stopInhale();
                    }
                }
                return false;
            }
        });
    }


    private void disableSeekbar(){
        seekbar.setEnabled(false);
        seekbar.setFocusable(false);
        seekbar.setSelected(false);
        seekbar.setClickable(false);
    }

    private void enableSeekbar(){
        seekbar.setEnabled(true);
        seekbar.setFocusable(true);
        seekbar.setSelected(true);
        seekbar.setClickable(true);
    }
}