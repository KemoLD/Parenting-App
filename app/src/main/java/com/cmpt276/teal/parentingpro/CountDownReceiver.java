package com.cmpt276.teal.parentingpro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.cmpt276.teal.parentingpro.TimerActivity.COUNTDOWN_END;
import static com.cmpt276.teal.parentingpro.TimerActivity.COUNTDOWN_INNER;
import static com.cmpt276.teal.parentingpro.TimerActivity.COUNTDOWN_TIME;

public class CountDownReceiver extends BroadcastReceiver {

    private TimerActivity activity;

    public CountDownReceiver() {
    }

    public void add(TimerActivity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (COUNTDOWN_INNER.equals(action)) {
            activity.trickTime(intent.getIntExtra(COUNTDOWN_TIME, 0));
        } else if (COUNTDOWN_END.equals(action)) {
            //codeTimerTxt.setText(R.string.reset_verify_code);
            activity.trickTime(0);
        }
    }
}
