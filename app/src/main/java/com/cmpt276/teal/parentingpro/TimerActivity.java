package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;

import java.util.Locale;

import static com.cmpt276.teal.parentingpro.data.AppDataKey.TIMER_PAUSE;
import static com.cmpt276.teal.parentingpro.data.AppDataKey.TIMER_TIME;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String COUNTDOWN_TIME = "COUNTDOWN_TIME";
    public static final String COUNTDOWN_INNER = "com.cmpt276.teal.parentingpro.COUNTDOWN_INNER";
    public static final String COUNTDOWN_END = "com.cmpt276.teal.parentingpro.COUNTDOWN_END";

    private ImageView pauseImg;
    private TextView timeTv;
    private EditText editText;

    private int currentTime;
    private boolean isRun;
    private boolean isPause;

    private Vibrator mVibrator;
    private Ringtone r;

    private CountDownReceiver mReceiver;

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

        CountDownReceiver.activity = this;
        mReceiver = new CountDownReceiver();
        mReceiver.add(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(COUNTDOWN_INNER);
        intentFilter.addAction(COUNTDOWN_END);
        registerReceiver(mReceiver, intentFilter);


        String v = DataUtil.getStringData(this, TIMER_TIME);
        String pause = DataUtil.getStringData(this, TIMER_PAUSE);
        if(pause.equals("YES")){
            isPause = true;
            pauseImg.setImageResource(R.mipmap.ic_resume);
            if (!v.equals("NaN")) {
                int c = Integer.parseInt(v);
                if (c > 0) {
                    currentTime = c;
                    timeTv.setText(String.format("%d:%d", currentTime/60, currentTime%60));
                }
            }
        }else {
            if (!v.equals("NaN")) {
                int c = Integer.parseInt(v);
                if (c > 0) {
                    currentTime = c;
                    startTimer();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    private void startTimer() {
        TimeService.TIMELOOP = currentTime;
        Intent intent = new Intent(TimerActivity.this, TimeService.class);
        startService(intent);
    }

    private void stopTimer() {
        Intent intent = new Intent(TimerActivity.this, TimeService.class);
        stopService(intent);
    }

    public void trickTime(int left) {
        timeTv.setText(String.format("%d:%d", left/60, left%60));
        currentTime = left;
        DataUtil.writeOneStringData(this, TIMER_TIME, String.valueOf(currentTime));
        if(left <= 0) {
            // callback
            mVibrator.vibrate(new long[]{1000, 10000, 1000, 10000}, -1);
            r.play();
            sendChatMsg(TimerActivity.this.getCurrentFocus());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
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
                stopTimer();
                currentTime = 0;
                DataUtil.writeOneStringData(this, TIMER_TIME, String.valueOf(currentTime));
                timeTv.setText("0:0");
                break;
            case R.id.pause:
                if(!isPause){
                    stopTimer();
                    pauseImg.setImageResource(R.mipmap.ic_resume);
                    DataUtil.writeOneStringData(this, TIMER_PAUSE, "YES");
                    isPause = true;
                }else{
                    startTimer();
                    isPause = false;
                    pauseImg.setImageResource(R.mipmap.ic_pause);
                    DataUtil.writeOneStringData(this, TIMER_PAUSE, "NO");
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