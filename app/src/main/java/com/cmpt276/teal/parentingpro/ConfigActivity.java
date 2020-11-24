package com.cmpt276.teal.parentingpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;
import com.cmpt276.teal.parentingpro.ui.ChildUI;
import com.cmpt276.teal.parentingpro.ui.ChildrenAdapter;

public class ConfigActivity extends AppCompatActivity {

     private ChildManagerUI manager;
     private ChildrenAdapter adapter;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        manager = ChildManagerUI.getInstance(this);
        manager.loadFromLocal(ConfigActivity.this, handler);
        final ListView listView = findViewById(R.id.list);
        adapter = new ChildrenAdapter(this, manager,this);

        listView.setAdapter(adapter);
        final EditText editText = findViewById(R.id.edit_name);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if(name.length() > 0){
                    manager.addChild(new ChildUI(name));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
           int pos = data.getIntExtra("pos",0);
           if(data.getStringExtra("name") != null){
               manager.getChild(pos).setName(data.getStringExtra("name"));
           }
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // super.handleMessage(msg);
            switch(msg.what){
                case ChildManagerUI.UPDATE_LISTVIEW: adapter.notifyDataSetChanged(false);

            }
        }
    };
}