package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;
import com.cmpt276.teal.parentingpro.ui.ChildUI;

import java.io.IOException;


public class ConfigActivity extends AppCompatActivity {

     private ChildManagerUI manager;
     private ChildrenAdapter adapter;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ListView listView = findViewById(R.id.list);
        manager = ChildManagerUI.getInstance(this);
        manager.loadFromLocal(ConfigActivity.this);

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
//           if(data.getByteArrayExtra("profile") != null){
//               byte[] byteArray = data.getByteArrayExtra("profile");
//
//                manager.getChild(pos).setProfile(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
//           }
            adapter.notifyDataSetChanged();

        }

    }
}