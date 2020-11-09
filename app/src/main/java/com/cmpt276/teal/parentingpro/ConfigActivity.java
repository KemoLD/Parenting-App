package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;


public class ConfigActivity extends AppCompatActivity {

     private ChildManager manager = ChildManager.getInstance();
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ListView listView = findViewById(R.id.list);

        manager.loadFromLocal(ConfigActivity.this);

        final ChildrenAdapter adapter = new ChildrenAdapter(this, manager);
        listView.setAdapter(adapter);
        final EditText editText = findViewById(R.id.edit_name);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if(name.length() > 0){
                    manager.addChild(new Child(name));
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
}