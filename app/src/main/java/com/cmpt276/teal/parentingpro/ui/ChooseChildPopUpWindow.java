package com.cmpt276.teal.parentingpro.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmpt276.teal.parentingpro.FlipCoinActivity;
import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;

public class ChooseChildPopUpWindow extends PopupWindow
{
    private View windowView;
    private Activity activity;
    private ChildManager childManager;
    private Button noChildButton;
    private ListView childListView;
    private int lastFlipChildIndex;

    public ChooseChildPopUpWindow(Activity activity) {
        super();
        this.activity = activity;
        this.childManager = ChildManager.getInstance();
        windowView = activity.getLayoutInflater().from(activity).inflate(R.layout.flip_coin_child_order, null);
        setupWindow();
    }


    private void setupWindow(){
        setuppopUpView();
        this.setContentView(this.windowView);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight((activity.getWindowManager().getDefaultDisplay().getHeight() >> 3) * 5);
        this.setFocusable(true);
    }


    private void setuppopUpView(){
        childListView = this.windowView.findViewById(R.id.popup_child_list);
        ChildListAdapter adapter = new ChildListAdapter(childManager);
        childListView.setAdapter(adapter);
        childListView.setOnItemClickListener(new ChildClickListener(adapter));
        setupNoChildButton();
    }

    private void setupNoChildButton(){
        noChildButton = this.windowView.findViewById(R.id.pop_no_child_btn);
        noChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtil.writeOneIntData(activity, AppDataKey.IS_NO_CHILD, 0);
                dismiss();
            }
        });
    }

    private class ChildClickListener implements AdapterView.OnItemClickListener
    {
        private BaseAdapter adapter;

        public ChildClickListener(BaseAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int arrayIndex = (int)view.getTag();
             if(arrayIndex < lastFlipChildIndex){
                 childManager.move(arrayIndex, lastFlipChildIndex);
                 lastFlipChildIndex--;
                 DataUtil.writeOneIntData(activity, AppDataKey.LAST_CHILD_FLIPPED_INDEX, lastFlipChildIndex);
             }
             else if(arrayIndex == lastFlipChildIndex){
                 lastFlipChildIndex = lastFlipChildIndex - 1 < 0 ? childManager.length() - 1 : --lastFlipChildIndex;
                 DataUtil.writeOneIntData(activity, AppDataKey.LAST_CHILD_FLIPPED_INDEX, lastFlipChildIndex);
             }
             else{  // arrayIndex > last flip child index
                 childManager.move(arrayIndex, lastFlipChildIndex + 1);
             }

             adapter.notifyDataSetChanged();
             DataUtil.writeOneIntData(activity, AppDataKey.IS_NO_CHILD, FlipCoinActivity.HAS_CHILD_CHOOSE);
        }
    }


    private class ChildListAdapter extends BaseAdapter
    {
        private int currentChildFlipIndex;
        private ChildManager manager;

        public ChildListAdapter(ChildManager childManager){
            this.manager = childManager;
            updateIndex();
        }

        @Override
        public int getCount() {
            return manager.length();
        }

        @Override
        public Object getItem(int position) {
            return manager.getChild(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View outputView = convertView;
            updateIndex();
            if(outputView == null){
                outputView = activity.getLayoutInflater().inflate(R.layout.child_order_item, parent, false);
            }

            int listPosition = (position + currentChildFlipIndex) % childManager.length();
            Child child = manager.getChild(listPosition);
            setListItemView(outputView, child, position);
            outputView.setTag(listPosition);

            return outputView;
        }


        private void setListItemView(View outputView, Child child, int position){
            TextView nameView = outputView.findViewById(R.id.flip_child_name);
            nameView.setText(child.getName());
            TextView orderView = outputView.findViewById(R.id.flip_child_order);
            orderView.setText("" + position);
        }

        private void updateIndex(){
            lastFlipChildIndex = DataUtil.getIntData(activity, AppDataKey.LAST_CHILD_FLIPPED_INDEX);
            currentChildFlipIndex = lastFlipChildIndex != -1 && lastFlipChildIndex < childManager.length() ? lastFlipChildIndex + 1 : 0;
        }
    }

}
