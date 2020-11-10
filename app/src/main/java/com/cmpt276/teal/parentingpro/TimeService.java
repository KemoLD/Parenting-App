package com.cmpt276.teal.parentingpro;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import static com.cmpt276.teal.parentingpro.TimerActivity.COUNTDOWN_END;
import static com.cmpt276.teal.parentingpro.TimerActivity.COUNTDOWN_INNER;
import static com.cmpt276.teal.parentingpro.TimerActivity.COUNTDOWN_TIME;

public class TimeService extends Service {

    CountDownTimer mCountDownTimer;
    int countDown;

    public static int TIMELOOP = 60;

    @Override
    public void onCreate() {
        super.onCreate();
        mCountDownTimer = new CountDownTimer(TIMELOOP * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Intent intent = new Intent(COUNTDOWN_INNER);
                intent.putExtra(COUNTDOWN_TIME, (int) millisUntilFinished / 1000);
                sendBroadcast(intent);
                countDown = (int) millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(COUNTDOWN_END);
                sendBroadcast(intent);
                stopSelf();
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroy();
    }
}