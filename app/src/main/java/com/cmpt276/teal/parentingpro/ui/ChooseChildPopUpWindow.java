package com.cmpt276.teal.parentingpro.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;

public class ChooseChildPopUpWindow extends PopupWindow
{
    private PopupWindow popupWindow;
    private View windowView;
    private Activity activity;
    private ChildManager childManager;

    public ChooseChildPopUpWindow(Activity activity) {
        this.activity = activity;
        this.childManager = ChildManager.getInstance();
        this.popupWindow = new PopupWindow();
        windowView = LayoutInflater.from(activity).inflate(R.layout.flip_coin_child_order, null);
        setupWindow();
    }


    private void setupWindow(){
        this.popupWindow.setContentView(this.windowView);

        ListView childListView = this.windowView.findViewById(R.id.popup_child_list);
        childListView.setAdapter(new ChildListAdapter());
    }



    private class ChildListAdapter extends ArrayAdapter<Child>
    {
        private int currentChildFlipIndex;
        public ChildListAdapter(){
            // !!!!!!!!
            super(activity, R.layout.child_item);       // !!!!!! fix the layout after !!!!!!!
            // !!!!!!

            int lastFlipChildIndex = DataUtil.getIntData(activity, AppDataKey.LAST_CHILD_FLIPPED_INDEX);
            currentChildFlipIndex = lastFlipChildIndex != -1 && lastFlipChildIndex < childManager.length() ? ++lastFlipChildIndex : 0;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View outputView = convertView;
            if(outputView == null){
                outputView = activity.getLayoutInflater().inflate(R.layout.child_item, parent);
            }

            int listPosition = (position + currentChildFlipIndex) % childManager.length();
            Child child = childManager.getChild(listPosition);
            setListItemView(outputView, child);
            return outputView;
        }


        private void setListItemView(View outputView, Child child){
            TextView textView = outputView.findViewById(R.id.tv_name);
            textView.setText(child.getName());
        }
    }
}
