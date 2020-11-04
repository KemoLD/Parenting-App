package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView pauseImg;
    private TextView timeTv;
    private EditText editText;

    private int currentTime;
    private boolean isRun;
    private boolean isPause;
    CountDownTimer countDownTimer;

    private Vibrator mVibrator;
    private Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.min1).setOnClickListener(this);
        findViewById(R.id.min2).setOnClickListener(this);
        findViewById(R.id.min3).setOnClickListener(this);
        findViewById(R.id.min5).setOnClickListener(this);
        findViewById(R.id.min10).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.reset).setOnClickListener(this);

        pauseImg = findViewById(R.id.pause);
        timeTv = findViewById(R.id.tv_timer);
        editText = findViewById(R.id.edit_timer);

        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(TimerActivity.this, notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "hint";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendChatMsg(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("Alarm Notification")
                .setContentText("Timer Expires")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    private void startTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer  = new CountDownTimer(currentTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long day = millisUntilFinished / (1000 * 24 * 60 * 60);
                long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);
                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);
                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;

                // cal time
                Log.d("TAG", String.format("剩余时间：%d时%d分%d秒", hour, minute, second));

                if(hour != 0){
                    timeTv.setText(String.format("%d %d:%d", hour, minute, second));
                }else{
                    timeTv.setText(String.format("%d:%d", minute, second));
                }
                currentTime--;
            }

            @Override
            public void onFinish() {
                // callback
                mVibrator.vibrate(new long[]{1000, 10000, 1000, 10000}, -1);
                r.play();
                sendChatMsg(TimerActivity.this.getCurrentFocus());
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.min1:
                if(isRun){
                    Toast.makeText(this, "Timer Is Running", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentTime = 1 * 60;
                startTimer();
                break;
            case R.id.min2:
                if(isRun){
                    Toast.makeText(this, "Timer Is Running", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentTime = 2* 60;
                startTimer();
                break;
            case R.id.min3:
                if(isRun){
                    Toast.makeText(this, "Timer Is Running", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentTime = 3* 60;
                startTimer();
                break;
            case R.id.min5:
                if(isRun){
                    Toast.makeText(this, "Timer Is Running", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentTime = 5* 60;
                startTimer();
                break;
            case R.id.min10:
                if(isRun){
                    Toast.makeText(this, "Timer Is Running", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentTime = 10* 60;
                startTimer();
                break;
            case R.id.start:
                if(isRun){
                    Toast.makeText(this, "Timer Is Running", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editText.getText().length() != 0 && Integer.parseInt(editText.getText().toString()) > 0){
                    currentTime = Integer.parseInt(editText.getText().toString()) * 60;
                    startTimer();
                }else{
                    Toast.makeText(this, "Edit Text is InValid", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.reset:
                isRun = false;
                countDownTimer.cancel();
                countDownTimer = null;
                timeTv.setText("0:0");
                break;
            case R.id.pause:
                if(!isPause){
                    countDownTimer.cancel();
                    countDownTimer = null;
                    pauseImg.setImageResource(R.mipmap.ic_resume);
                    isPause = true;
                }else{
                    startTimer();
                    isPause = false;
                    pauseImg.setImageResource(R.mipmap.ic_pause);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mVibrator!=null) {
            mVibrator.cancel();
        }
    }
}