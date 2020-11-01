package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_config).setOnClickListener(this);
        findViewById(R.id.btn_flip).setOnClickListener(this);
        findViewById(R.id.btn_timer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_config:
                break;
            case R.id.btn_flip:
                break;
            case R.id.btn_timer: {
                Intent integer = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(integer);
                break;
            }
        }
    }

    // TEST GIT PUSH AND PULL
    // test2
    //test3
}