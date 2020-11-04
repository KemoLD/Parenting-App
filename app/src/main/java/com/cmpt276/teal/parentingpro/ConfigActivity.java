package com.cmpt276.teal.parentingpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {

    private List<String> childs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ListView listView = findViewById(R.id.list);

        //get names from prefer
        String val = DataUtil.getStringData(this, AppDataKey.CHILDREN_NAMES);
        if (!val.equals("NaN")) {
            String[] names = val.split("###");
            for(String n : names){
                if(!n.isEmpty()){
                    childs.add(n);
                }
            }
        }

        final ChildrenAdapter adapter = new ChildrenAdapter(this, childs);
        listView.setAdapter(adapter);
        final EditText editText = findViewById(R.id.edit_name);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if(name.length() > 0){
                    childs.add(name);
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