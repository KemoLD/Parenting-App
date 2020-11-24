package com.cmpt276.teal.parentingpro;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.model.TurnTask;
import com.cmpt276.teal.parentingpro.model.TurnTaskManager;
import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;

import java.util.ArrayList;
import java.util.List;

/**
 * the class represent the whose turn page for the app
 */
public class WhoseTurnActivity extends AppCompatActivity {

     private TurnTaskManager manager = TurnTaskManager.getInstance();
     private ChildManagerUI childManager;
     private int choseChildIndex;
     private WhoseTurnAdapter adapter;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whoseturn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        childManager = ChildManagerUI.getInstance(this);
        childManager.loadFromLocal(this, handler);
        manager.loadFromLocal(WhoseTurnActivity.this, childManager);
        final ListView listView = findViewById(R.id.list);
        adapter = new WhoseTurnAdapter(this, manager, childManager);


        listView.setAdapter(adapter);
        final EditText editText0 = findViewById(R.id.edit_desc);


        List<String> childStrs = childManager.getChilds();
        if(childStrs.size() == 0){
            Toast.makeText(this, "Must Config Childrens First", Toast.LENGTH_LONG).show();
            return;
        }
        choseChildIndex = 0;
        Spinner spinnertext =  findViewById(R.id.spinner);
        final ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, childStrs);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertext.setAdapter(arrAdapter);
        spinnertext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choseChildIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childIndex = choseChildIndex;
                String desc = editText0.getText().toString();
                if(childIndex >= 0 && childIndex < childManager.length() && desc.length() > 0){
                    manager.addTask(new TurnTask(desc, childIndex));
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


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // super.handleMessage(msg);
            switch(msg.what){
                case ChildManagerUI.UPDATE_LISTVIEW: adapter.notifyDataSetChanged();

            }
        }
    };
}