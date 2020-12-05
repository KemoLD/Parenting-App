package com.cmpt276.teal.parentingpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//timer activity that rings once it expires

/**
 * the class represent the timer activity for the app
 */
public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView pauseImg;
    private TextView timeTv;
    private TextView speedTv;
    private EditText editText;
    private ProgressBar progressBar;

    private int currentTime;
    private boolean isRun = false;
    private boolean isPause;
    CountDownTimer countDownTimer;
    int totalTime;

    private Vibrator mVibrator;
    private static Ringtone r;
    private int time = 0;
    private String s;
    private final String newIntent = "notification";

    private boolean isSpeedRun = false;
    private boolean isRestarted = false;
    private boolean isReset = false;
    private boolean optionClicked;
    private int currentSpeedIndex = 3;
    private int tentativeSpeedIndex;
    final String[] options = {"25%", "50%", "75%", "100%", "200%", "300%", "400%"};
    final long[] countDownIntervals = {4000, 2000, 1333, 1000, 500, 333, 250};
    final double[] scaleFactors = {4.0, 2.0, 1.333, 1.0, 0.5, 0.333, 0.25};


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableUpButton();

        setClickListener();
        setFindView();

        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(this, notification);

        setNotificationChannel();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null)
            return;
        super.onNewIntent(intent);
        String intentType = intent.getStringExtra("type");
        if (intentType != null && intentType.equals(newIntent)) {
            if (r.isPlaying()) {
                r.stop();
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

    private void setClickListener() {
        findViewById(R.id.min1).setOnClickListener(this);
        findViewById(R.id.min2).setOnClickListener(this);
        findViewById(R.id.min3).setOnClickListener(this);
        findViewById(R.id.min5).setOnClickListener(this);
        findViewById(R.id.min10).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.reset).setOnClickListener(this);
        findViewById(R.id.sound).setOnClickListener(this);
    }

    private void setFindView() {
        pauseImg = findViewById(R.id.pause);
        timeTv = findViewById(R.id.tv_timer);
        speedTv = findViewById(R.id.tv_speed);
        editText = findViewById(R.id.edit_timer);
        progressBar = findViewById(R.id.timer_progress);
    }

    private void setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "hint";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName, importance);
            channel.setDescription(getString(R.string.timer_expire_text));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_timer_speed:
                if (isRun && !isPause) {
                    showTimerSpeedOptions();
                }
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enableUpButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void sendChatMsg(View view) {

        Intent resultIntent = new Intent(this, TimerActivity.class);
        resultIntent.putExtra("type", newIntent);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "chat");
        notification.setContentTitle(getText(R.string.timeout_title));
        notification.setContentText(getText(R.string.timeout_content));
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

        setSpeedText();

        countDownTimer = new CountDownTimer(currentTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("tag", "millisUnitFinished = " + millisUntilFinished);
                Log.i("tag", "current time = " + currentTime);
                long day = millisUntilFinished / (1000 * 24 * 60 * 60);

                long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);

                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);

                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;

                // calls the time
                displayTimeText(day, hour, minute, second, false);

                // -1000 is subtracting the time interval
                int progress = (int) (((double) (millisUntilFinished - 1000) / totalTime) * progressBar.getMax());
                progressBar.setProgress(progressBar.getMax() - progress);
                currentTime--;
            }

            @Override
            public void onFinish() {
                setTimerFinishParameters();
            }
        };
        countDownTimer.start();
    }

    private void showTimerSpeedOptions() {
        optionClicked = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Timer Speed")
                .setSingleChoiceItems(options, currentSpeedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tentativeSpeedIndex = i;
                        optionClicked = true;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (optionClicked && currentSpeedIndex != tentativeSpeedIndex) {
                            currentSpeedIndex = tentativeSpeedIndex;
                            if (isRun) {
                                startChangedSpeedTimer();
                            }
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void delayBeforeStart() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!isRestarted && !isReset && !isPause) {
                    startChangedSpeedTimer();
                }
            }
        };

        long delayMillis;
        switch (currentSpeedIndex) {
            case 0:
                delayMillis = countDownIntervals[currentSpeedIndex] - 400;
                break;
            case 1:
            case 2:
                delayMillis = countDownIntervals[currentSpeedIndex] - 500;
                break;
            default:
                delayMillis = 0;
        }

        Handler handler = new Handler();
        handler.postDelayed(runnable, delayMillis);
    }

    private void startChangedSpeedTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        isSpeedRun = true;
        isReset = false;
        isRestarted = false;
        setSpeedText();

        long millisInFuture = Math.round(currentTime * scaleFactors[currentSpeedIndex] * 1000);
        countDownTimer = new CountDownTimer(millisInFuture, countDownIntervals[currentSpeedIndex]) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisUntilFinished = Math.round(millisUntilFinished / scaleFactors[currentSpeedIndex]);
                long day = millisUntilFinished / (1000 * 24 * 60 * 60);

                long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);

                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);

                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;

                displayTimeText(day, hour, minute, second, false);
                int progress = (int) (((double) (millisUntilFinished - 1000) / totalTime) * progressBar.getMax());
                progressBar.setProgress(progressBar.getMax() - progress);
                currentTime--;
            }

            @Override
            public void onFinish() {
                setTimerFinishParameters();
            }
        };
        countDownTimer.start();
    }

    private void setSpeedText() {
        String text = "Time @" + options[currentSpeedIndex];
        speedTv.setText(text);
    }

    private void setTimerFinishParameters() {
        timeTv.setText("00:00");
        speedTv.setText("");
        progressBar.setProgress(progressBar.getMax());  // set it to full
        isRun = false;
        isSpeedRun = false;
        currentSpeedIndex = 3;
        time = 0;
        pauseImg.setImageResource(R.mipmap.ic_resume);
        r.play();
        Thread stopRing = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r.stop();
            }
        });
        stopRing.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(true);
        }
        sendChatMsg(TimerActivity.this.getCurrentFocus());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.min1:
                setButtonClickTime(1);
                break;
            case R.id.min2:
                setButtonClickTime(2);
                break;
            case R.id.min3:
                setButtonClickTime(3);
                break;
            case R.id.min5:
                setButtonClickTime(5);
                break;
            case R.id.min10:
                setButtonClickTime(10);
                break;

            case R.id.start:
                startButtonClicked();
                break;
            case R.id.reset:
                resetButtonCLick();
                break;
            case R.id.pause:
                pauseButtonClick();
                break;
            case R.id.sound:
                if (r.isPlaying()) {
                    r.stop();
                } else {
                    return;
                }
        }
    }

    private void setButtonClickTime(int inputTime) {
        if (isRun) {
            stopTimer();
        }
        time = inputTime;
        String t = String.format("%02d", time);
        s = (t + ":00");
        timeTv.setText(s);
        currentTime = time * 60;
        pauseImg.setImageResource(R.mipmap.ic_resume);
        isPause = false;
    }

    private void startButtonClicked() {
        if (editText.getText().length() != 0 &&
                Integer.parseInt(editText.getText().toString()) > 0) {
            if (isRun) {
                stopTimer();
                pauseImg.setImageResource(R.mipmap.ic_resume);
            }
            progressBar.setProgress(0);
            speedTv.setText("");
            time = Integer.parseInt(editText.getText().toString());
            currentTime = time * 60;
            totalTime = currentTime * 1000;
            int days = time / 1440;
            int hours = (time % 1440) / 60;
            int minutes = ((time % 1440) % 60);
            int seconds = ((time % 1440) % 60) / 60;
            displayTimeText(days, hours, minutes, seconds, true);
        } else {
            Toast.makeText(this, getText(R.string.time_invalid), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetButtonCLick() {
        if (isRun) {
            isReset = true;
            isRun = false;
            isSpeedRun = false;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            currentTime = time * 60;
            currentSpeedIndex = 3;
            Log.i("tag", "s = " + s);
            timeTv.setText(s);
            speedTv.setText("");
            progressBar.setProgress(0);
            pauseImg.setImageResource(R.mipmap.ic_resume);
        }
    }

    private void pauseButtonClick() {
        if (isRun) {
            if (!isPause) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                pauseImg.setImageResource(R.mipmap.ic_resume);
                isPause = true;
            } else {
                progressBar.setProgress(0);
                if (isSpeedRun) {
                    if (currentSpeedIndex < 3) {
                        delayBeforeStart();
                    } else {
                        startChangedSpeedTimer();
                    }
                } else {
                    startTimer();
                }
                isPause = false;
                pauseImg.setImageResource(R.mipmap.ic_pause);
            }

        } else if (time != 0) {
            totalTime = currentTime * 1000;
            startTimer();
            isPause = false;
            isRun = true;
            pauseImg.setImageResource(R.mipmap.ic_pause);
        }
    }

    private void stopTimer() {
        isRestarted = true;
        isRun = false;
        isSpeedRun = false;
        currentSpeedIndex = 3;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void displayTimeText(long days, long hours, long minutes, long seconds, boolean isSetGobalString) {
        String day = String.format("%02d", days);
        String hour = String.format("%02d", hours);
        String minute = String.format("%02d", minutes);
        String second = String.format("%02d", seconds);

        String str;
        if (days != 0) {
            str = (day + ":" + hour + ":" + minute + ":" + second);
        } else {
            if (hours != 0) {
                str = (hour + ":" + minute + ":" + second);
            } else {
                str = (minute + ":" + second);

            }
        }

        if (isSetGobalString) {
            s = str;
        }
        timeTv.setText(str);
    }
}
