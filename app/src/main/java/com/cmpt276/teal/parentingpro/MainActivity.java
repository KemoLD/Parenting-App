package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * the class is the main activity for the app
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_config).setOnClickListener(this);
        findViewById(R.id.btn_flip).setOnClickListener(this);
        findViewById(R.id.btn_timer).setOnClickListener(this);
        findViewById(R.id.btn_help).setOnClickListener(this);
        findViewById(R.id.btn_turns).setOnClickListener(this);
        findViewById(R.id.btn_take_breath).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_config: {
                Intent integer = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(integer);
                break;
            }
            case R.id.btn_flip: {
                Intent integer = new Intent(MainActivity.this, FlipCoinActivity.class);
                startActivity(integer);
                break;
            }
            case R.id.btn_timer: {
                Intent integer = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(integer);
                break;
            }
            case R.id.btn_help: {
                Intent integer = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(integer);
                break;
            }
            case R.id.btn_turns: {
                Intent integer = new Intent(MainActivity.this, WhoseTurnActivity.class);
                startActivity(integer);
                break;
            }
            case R.id.btn_take_breath:{
                Intent integer = new Intent(MainActivity.this, TakeBreathActivity.class);
                startActivity(integer);
                break;
            }
        }
    }
}