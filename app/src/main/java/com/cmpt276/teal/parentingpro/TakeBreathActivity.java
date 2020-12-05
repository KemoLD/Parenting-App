package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.ui.ScaleAnimator;

import java.util.concurrent.TimeUnit;

public class TakeBreathActivity extends AppCompatActivity {

    private boolean isRun;
    private TextView tvDesc;
    private Button breathBtn;
    private SeekBar seekbar;
    private TextView seekbarText;

    private ScaleAnimator animationIn;
    private ScaleAnimator animationOut;

    public static final int MIN_BREATH = 1;
    public static final int MAX_BREATH = 10;
    public static final int DEFAULT_BREATH = 3;

    private static final float MIN_BUTTON_SCALE = 1;
    private static final float MAX_BUTTON_SCALE = 4;
    private static final long SCALE_DURATION = 10000;

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
            if(isPress){
                finish();
            }
            isPress = true;
            pressTime = System.currentTimeMillis();
            inState = true;
            disableSeekbar();
            breathBtn.setText(getText(R.string.in));
            tvDesc.setText(getText(R.string.hold_breath_in));
            transFormToScale(animationOut, animationIn, MAX_BUTTON_SCALE);
            streamId = mSoundPool.play(audioIn, 1, 1, 8, 0, 1);
        }

        public void finish(){
            cancelAnimationIn();
            mSoundPool.stop(streamId);
        }

        public void stopInhale(){
            if(inState) {
                finish();
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
            isPress = true;
            inState = false;
            pressTime = System.currentTimeMillis();
            breathBtn.setText(R.string.out);
            tvDesc.setText(R.string.release_breath_out);
            transFormToScale(animationIn, animationOut, MIN_BUTTON_SCALE);
            streamId = mSoundPool.play(audioOut, 1, 1, 8, 0, 1);
        }

        public void inBeathing(long time){
            long cost = time - breathState.pressTime;
            if(cost > 3000){
                if(inState) {
                    breathBtn.setText(R.string.out);
                }else{
                    breathBtn.setText(R.string.in);
                    N--;
                    tvDesc.setText(String.format("Left %d breaths", N));
                    isPress = false;
                    if(N == 0) {
                        enableSeekbar();
                        breathBtn.setText(R.string.good_job);
                        finish();
                    }

                }
            }
            if(cost > 10000){
                finish();
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
        N = DataUtil.getIntData(this, AppDataKey.BREATH_INHALE);
        if(N <= -1){
            N = 3;
            DataUtil.writeOneIntData(this, AppDataKey.BREATH_INHALE, N);
        }
        breathState.reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_take_breath);


        tvDesc = findViewById(R.id.tv_desc);

        N = DataUtil.getIntData(this, AppDataKey.BREATH_INHALE);
        if(N <= -1){
            N = 3;
            DataUtil.writeOneIntData(this, AppDataKey.BREATH_INHALE, N);
        }


        seekbar = findViewById(R.id.breath_seekbar);
        seekbarText = findViewById(R.id.seekbar_text);
        breathBtn = findViewById(R.id.btn_breath);

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


    private void setupSeekbar(){
        seekbar.setMax(MAX_BREATH - MIN_BREATH);
        seekbar.setProgress(DEFAULT_BREATH - MIN_BREATH);
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
        animationIn = new ScaleAnimator(breathBtn, MIN_BUTTON_SCALE, MAX_BUTTON_SCALE);
        animationIn.setDuration(SCALE_DURATION);
        animationIn.setRepeatCount(0);

        animationOut = new ScaleAnimator(breathBtn, MAX_BUTTON_SCALE, MIN_BUTTON_SCALE);
        animationOut.setDuration(SCALE_DURATION);
        animationOut.setRepeatCount(0);
    }


    private void transFormToScale(ScaleAnimator from, ScaleAnimator to, float value){
        float scaleSize = getScaleSizeFromAnimation(from);
        from.cancel();      // cancel value doest matter this point because to animation is going to be start
        to.setFloatValues(scaleSize, value);
        to.start();
    }


    private void cancelAnimationIn(){
        float scaleSize = getScaleSizeFromAnimation(animationOut);
        animationIn.setCancelScaleValue(scaleSize);
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
                Log.d("PRJ", "action：" + event.getAction());
                if(event.getAction() == MotionEvent.ACTION_DOWN){
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