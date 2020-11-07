package com.cmpt276.teal.parentingpro.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.Coin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryPage extends AppCompatActivity
{
    private History historyList;

    public static Intent getIntent(Context context){
        return new Intent(context, HistoryPage.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        //addSomeData(5);
        historyList = History.getInstance();
        ListView historyListView = findViewById(R.id.history_listview);

        // for historyList adapter pass a list we need to show
        // pass all history
        historyListView.setAdapter(new HistoryListAdapter(historyList.getAllHistoryList()));

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

           historyList.addHistory(new HistoryData(child, date, coin.getRandomState(), coin.getRandomState()));

       }
    }

    /**
     * the inner class is for creating a UI view in listVew for history
     */
    private class HistoryListAdapter extends ArrayAdapter<HistoryData>
    {
        // for formatting the Date in History date
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
            TextView chooseText = itemView.findViewById(R.id.history_content_chose);
            ImageView resultImage = itemView.findViewById(R.id.history_content_result);

            HistoryData data = historyList.getHistoryData(position);
            String paredDate = dateFormat.format(data.getDate());
            String childName = data.getChild().getName();
            Coin.CoinState chosenState = data.getChosenState();
            Coin.CoinState resultState = data.getResultState();

            String state = (chosenState == Coin.CoinState.HEADS ? "HEADS" : "TAILS");

            // Check mark source: https://freeiconshop.com/icon/checkmark-icon-flat/
            // Cross source: https://freeiconshop.com/icon/cross-icon-flat/
            int resultImageID = (chosenState == resultState ? R.drawable.check_mark : R.drawable.cross);

            dateText.setText(paredDate);
            nameText.setText(childName);
            chooseText.setText(state);
            resultImage.setImageResource(resultImageID);

            return itemView;
        }
    }
}