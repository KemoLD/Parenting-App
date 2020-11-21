package com.cmpt276.teal.parentingpro;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.model.TurnTask;
import com.cmpt276.teal.parentingpro.model.TurnTaskManager;


public class WhoseTurnActivity extends AppCompatActivity {

     private TurnTaskManager manager = TurnTaskManager.getInstance();
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whoseturn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ListView listView = findViewById(R.id.list);

        manager.loadFromLocal(WhoseTurnActivity.this);

        final WhoseTurnAdapter adapter = new WhoseTurnAdapter(this, manager);
        listView.setAdapter(adapter);
        final EditText editText = findViewById(R.id.edit_name);
        final EditText editText0 = findViewById(R.id.edit_desc);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                String desc = editText0.getText().toString();
                if(name.length() > 0 && desc.length() > 0){
                    manager.addTask(new TurnTask(desc, name));
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