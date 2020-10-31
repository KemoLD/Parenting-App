package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FlipCoin extends AppCompatActivity
{
    private final int FLIP_BTN = R.id.flip_btn;
    private final int HISTORY_BTN = R.id.history_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin2);

        Button flipBtn = findViewById(FLIP_BTN);
        Button historyBtn = findViewById(HISTORY_BTN);

        flipBtn.setOnClickListener(new FlipCoinClickListener());


        historyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FlipCoin.this, HistoryPage.class);
                startActivity(intent);
            }
        });
    }


    class FlipCoinClickListener implements View.OnClickListener
    {
        public void onClick(View view){

        }
    }

}