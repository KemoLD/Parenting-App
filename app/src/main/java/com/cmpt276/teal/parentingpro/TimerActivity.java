package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
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

import java.io.IOException;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView pauseImg;
    private TextView timeTv;
    private EditText editText;

    private int currentTime;
    private boolean isRun = false;
    private boolean isPause;
    CountDownTimer countDownTimer;

    private Vibrator mVibrator;
    private Ringtone r;
    private int time = 0;
    private String t;
    private String s;
    private final String newintent = "notification";

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
        findViewById(R.id.sound).setOnClickListener(this);

        pauseImg = findViewById(R.id.pause);
        timeTv = findViewById(R.id.tv_timer);
        editText = findViewById(R.id.edit_timer);

        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone (this, notification);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "hint";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription("Your timeout timer has expired");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if(intent.getStringExtra("type").equals(newintent))
        {
            if(r.isPlaying()){
                r.stop();
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


    public void sendChatMsg(View view) {

        Intent resultIntent = new Intent(this, TimerActivity.class);
        resultIntent.putExtra("type", newintent);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );


        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "chat");
        notification.setContentTitle("Timeout Timer Notification");
        notification.setContentText("Timer Expired");
        notification.setSmallIcon(R.drawable.alarm_icon);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.alarm_icon));
        notification.setVibrate(new long[]{100, 1000, 100, 1000});
        notification.setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setContentIntent(resultPendingIntent);
        notification.setAutoCancel(true);
        notification.setCategory(NotificationCompat.CATEGORY_ALARM);
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        manager.notify(1, notification.build());
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(currentTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long day = millisUntilFinished / (1000 * 24 * 60 * 60);

                long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);

                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);

                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;

                // calls the time
                Log.d("TAG", String.format("count：%d hours %d minutes %d seconds", hour, minute, second));

                String days = String.format("%02d", day);
                String hours = String.format("%02d", hour);
                String minutes = String.format("%02d", minute);
                String seconds = String.format("%02d", second);

                if (day != 0) {
                    timeTv.setText(days + ":" + hours + ":" + minutes + ":" + seconds);
                } else {
                    if (hour != 0) {
                        timeTv.setText(hours + ":" + minutes + ":" + seconds);
                    } else {
                        timeTv.setText(minutes + ":" + seconds);
                    }
                }
                currentTime--;
            }

            @Override
            public void onFinish() {
                // call back
                timeTv.setText("00:00");
                isRun = false;
                time = 0;
                pauseImg.setImageResource(R.mipmap.ic_resume);
                r.play();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    r.setLooping(true);
                }
                sendChatMsg(TimerActivity.this.getCurrentFocus());

            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.min1:
                if (isRun) {
                    isRun = false;
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                time = 1;
                t = String.format("%02d", time);
                s = (t + ":00");
                timeTv.setText(s);
                currentTime = time * 60;
                pauseImg.setImageResource(R.mipmap.ic_resume);
                isPause = false;
                break;
            case R.id.min2:
                if (isRun) {
                    isRun = false;
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                time = 2;
                t = String.format("%02d", time);
                s = (t + ":00");
                timeTv.setText(s);
                currentTime = time * 60;
                pauseImg.setImageResource(R.mipmap.ic_resume);
                isPause = false;
                break;
            case R.id.min3:
                if (isRun) {
                    isRun = false;
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                time = 3;
                t = String.format("%02d", time);
                s = (t + ":00");
                timeTv.setText(s);
                currentTime = time * 60;
                pauseImg.setImageResource(R.mipmap.ic_resume);
                isPause = false;
                break;
            case R.id.min5:
                if (isRun) {
                    isRun = false;
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                time = 5;
                t = String.format("%02d", time);
                s = (t + ":00");
                timeTv.setText(s);
                currentTime = time * 60;
                pauseImg.setImageResource(R.mipmap.ic_resume);
                isPause = false;
                break;
            case R.id.min10:
                if (isRun) {
                    isRun = false;
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                time = 10;
                t = String.format("%02d", time);
                s = (t + ":00");
                timeTv.setText(s);
                currentTime = time * 60;
                pauseImg.setImageResource(R.mipmap.ic_resume);
                isPause = false;
                break;
            case R.id.start:
                if (editText.getText().length() != 0 && Integer.parseInt(editText.getText().toString()) > 0) {
                    if (isRun) {
                        isRun = false;
                        countDownTimer.cancel();
                        countDownTimer = null;
                        pauseImg.setImageResource(R.mipmap.ic_resume);
                    }
                    time = Integer.parseInt(editText.getText().toString());
                    currentTime = time * 60;

                    int days = time / 1440;
                    int hours = (time % 1440) / 60;
                    int minutes = ((time % 1440) % 60);
                    int seconds = ((time % 1440) % 60) / 60;

                    String day = String.format("%02d", days);
                    String hour = String.format("%02d", hours);
                    String minute = String.format("%02d", minutes);
                    String second = String.format("%02d", seconds);

                    if (days != 0) {
                        s = (day + ":" + hour + ":" + minute + ":" + second);
                        timeTv.setText(s);
                    } else {
                        if (hours != 0) {
                            s = (hour + ":" + minute + ":" + second);
                            timeTv.setText(s);
                        } else {
                            s = (minute + ":" + second);
                            timeTv.setText(s);
                        }
                    }

                } else {
                    Toast.makeText(this, "Time is InValid", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.reset:
                if (isRun) {
                    isRun = false;
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    currentTime = time * 60;
                    timeTv.setText(s);
                    pauseImg.setImageResource(R.mipmap.ic_resume);
                } else {
                    return;
                }
                break;
            case R.id.pause:
                if (isRun) {
                    if (!isPause) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                        pauseImg.setImageResource(R.mipmap.ic_resume);
                        isPause = true;
                    } else {
                        startTimer();
                        isPause = false;
                        pauseImg.setImageResource(R.mipmap.ic_pause);
                    }
                } else if (time == 0) {
                    return;
                } else {
                    startTimer();
                    isPause = false;
                    isRun = true;
                    pauseImg.setImageResource(R.mipmap.ic_pause);
                }
                break;
            case R.id.sound:
                if (r.isPlaying()) {
                    r.stop();
                } else {
                    return;
                }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVibrator != null) {
            mVibrator.cancel();
        }
    }

}