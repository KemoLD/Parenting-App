package com.cmpt276.teal.parentingpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.Coin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryPage extends AppCompatActivity
{
    private final int HISTORY_LIST_VIEW = R.id.history_listview;
    private History history = History.getInstance();



    public static Intent getIntent(Context context){
        return new Intent(context, HistoryPage.class);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        //addSomeData(5);

        ListView historyListView = findViewById(HISTORY_LIST_VIEW);

        // for historyList adapter pass a list we need to show
        // pass all history
        historyListView.setAdapter(new HistoryListAdapter(history.getAllHistoryList()));

        // pass with a child
        // historyListView.setAdapter(new HistoryListAdapter(history.getHistoryListWithChild(new Child("Ben0"))));
    }



    // a function just for test for now
    public void addSomeData(int dataSize)
    {
        Date currentDate = new Date();
       for(int i = 0; i < dataSize; i++){
           String name = "Ben" + i;

           Child child = new Child(name);
           Date date = new Date(currentDate.getTime() - i * 99999999999l);
           Coin coin = new Coin();
           System.out.println(coin.getState());

           history.addHistory(new HistoryData(child, date, coin.getRandomState(), coin.getRandomState()));

       }
    }



    /**
     * the inner class is for creating a UI view in listVew for history
     */
    private class HistoryListAdapter extends ArrayAdapter<HistoryData>
    {
        // for formating the Date in History daate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public HistoryListAdapter(ArrayList<HistoryData> dataList){
            super(HistoryPage.this, R.layout.history_listview_content, dataList);
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.history_listview_content, parent, false);
            }

            TextView dateText = itemView.findViewById(R.id.history_content_date);
            TextView nameText = itemView.findViewById(R.id.history_content_name);
            TextView chooseText = itemView.findViewById(R.id.history_content_choose);
            TextView resultText = itemView.findViewById(R.id.history_content_result);

            HistoryData data = history.getHistoryData(position);
            String paredDate = dateFormat.format(data.getDate());
            String childName = data.getChild().getName();
            Coin.CoinState choosedState = data.getChoosedState();
            Coin.CoinState resultState = data.getResultState();

            String state = choosedState == Coin.CoinState.HEAD ? "HEAD" : "TAIL";
            String result = choosedState == resultState ? "Yes" : "No";

            dateText.setText(paredDate);
            nameText.setText(childName);
            chooseText.setText(state);
            resultText.setText(result);

            return itemView;
        }
    }
}