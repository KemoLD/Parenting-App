package com.cmpt276.teal.parentingpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.History;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.model.Coin;
import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;
import com.cmpt276.teal.parentingpro.ui.ChildUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * the class represent the hisotry page in the flip coin activity
 */
public class HistoryActivity extends AppCompatActivity
{
    private ArrayList<HistoryData> historyArray;
    private int currentChildIndex;
    private static final String EXTRA_CURRENT_CHILD_INDEX = "Index of the child currently flipping";

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, HistoryActivity.class);
    }

    public static Intent makeLaunchIntent(Context context, int currentChildIndex){
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putExtra(EXTRA_CURRENT_CHILD_INDEX, currentChildIndex);
        return intent;
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        currentChildIndex = intent.getIntExtra(EXTRA_CURRENT_CHILD_INDEX, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ChildManagerUI childManager = FlipCoinActivity.getFilpCoinChildManager();
        extractIntentData();

        History historyList = History.getInstance();
        ListView historyListView = findViewById(R.id.history_listview);

        if (currentChildIndex == -1) {
            historyArray = historyList.getAllHistoryList();
        } else {
            ChildUI currentChild = childManager.getChild(currentChildIndex);
            historyArray = historyList.getHistoryListWithChild(currentChild);
        }
        historyListView.setAdapter(new HistoryListAdapter(historyArray));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * the inner class is for creating a UI view in listVew for history
     */
    private class HistoryListAdapter extends ArrayAdapter<HistoryData>
    {
        // for formatting the Date in History date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d HH:mm");

        public HistoryListAdapter(ArrayList<HistoryData> dataList){
            super(HistoryActivity.this, R.layout.history_listview_content, dataList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.history_listview_content,
                        parent, false);
            }

            TextView dateText = itemView.findViewById(R.id.history_content_date);
            TextView nameText = itemView.findViewById(R.id.history_content_name);
            TextView chooseText = itemView.findViewById(R.id.history_content_chose);
            ImageView resultImage = itemView.findViewById(R.id.history_content_result);

            HistoryData data = historyArray.get(position);
            String paredDate = dateFormat.format(data.getDate());
            String childName = data.getChild().getName();
            Coin.CoinState chosenState = data.getChosenState();
            Coin.CoinState resultState = data.getResultState();

            CharSequence state = (chosenState == Coin.CoinState.HEADS) ? getText(R.string.head) : getText(R.string.tail);

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