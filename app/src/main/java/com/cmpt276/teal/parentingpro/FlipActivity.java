package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class FlipActivity extends AppCompatActivity {


    private Handler mHandler = new Handler();
    ShakeLayout shakeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        shakeLayout = (ShakeLayout)findViewById(R.id.shake_layout);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shakeLayout.anim();
            }
        }, 600);

    }
}