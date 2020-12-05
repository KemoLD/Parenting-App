package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;

import java.util.concurrent.TimeUnit;

public class TakeBreathActivity extends AppCompatActivity {

    private boolean isRun;
    private TextView tvDesc;
    private ImageView logo;
    private ScaleAnimation animationIn;
    private ScaleAnimation animationOut;
    private Button breathBtn;

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
            breathBtn.setText("In");
            tvDesc.setText("Hold Button and breath in");
            logo.startAnimation(animationIn);
            streamId = mSoundPool.play(audioIn, 1, 1, 8, 0, 1);
        }

        public void finish(){
            logo.clearAnimation();
            mSoundPool.stop(streamId);
//            if(!inState){
//                isPress = false;
//            }
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
            breathBtn.setText("Out");
            tvDesc.setText("Release Button and breath out");
            logo.startAnimation(animationOut);
            streamId = mSoundPool.play(audioOut, 1, 1, 8, 0, 1);
        }

        public void inBeathing(long time){
            long cost = time - breathState.pressTime;
            if(cost > 3000){
                if(inState) {
                    breathBtn.setText("Out");
                }else{
                    breathBtn.setText("In");
                    N--;
                    tvDesc.setText(String.format("Left %d breaths", N));
                    isPress = false;
                    if(N == 0) {
                        breathBtn.setText("Good job");
                        finish();
                    }
                }
            }
            if(cost > 10000){
                finish();
            }
        }

        public void reset(){
            tvDesc.setText(String.format("Let's take %d breaths together", N));
            breathBtn.setText("Begin");

            isPress = false;
            inState = true;
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
        if(N == -1){
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
        if(N == -1){
            N = 3;
            DataUtil.writeOneIntData(this, AppDataKey.BREATH_INHALE, N);
        }

        final EditText editText = findViewById(R.id.edit_breath);
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                if(!s.isEmpty()){
                    N = Integer.parseInt(s);
                    DataUtil.writeOneIntData(TakeBreathActivity.this, AppDataKey.BREATH_INHALE, N);
                    breathState.reset();
                }
            }
        });

        logo = findViewById(R.id.imv_circle);
        breathBtn = findViewById(R.id.btn_breath);

        animationIn = new ScaleAnimation(1, 3, 1, 3,
                Animation.RELATIVE_TO_SELF, 0.5f,1, 0.5f);
        animationIn.setDuration(3000);
        animationIn.setFillAfter(true);
        animationIn.setRepeatCount(0);


        animationOut = new ScaleAnimation(3, 1, 3, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,1, 0.5f);
        animationOut.setDuration(3000);
        animationOut.setFillAfter(true);
        animationOut.setRepeatCount(0);

        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setLegacyStreamType(AudioManager.STREAM_NOTIFICATION);
        builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);

        SoundPool.Builder builder2 = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(builder.build());
        mSoundPool = builder2.build();
        audioIn = mSoundPool.load(this, R.raw.breath_in, 1);
        audioOut = mSoundPool.load(this, R.raw.breath_out, 1);



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
}