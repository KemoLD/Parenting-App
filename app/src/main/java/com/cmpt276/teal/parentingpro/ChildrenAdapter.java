package com.cmpt276.teal.parentingpro;

import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.model.ChildManager;

import java.util.List;

public class ChildrenAdapter extends BaseAdapter {

    private Context mContext;
    // private List<String> names;

    private ChildManager childManager;

    public ChildrenAdapter(Context context, ChildManager childManager) {
        this.mContext = context;
        // this.names = names;
        this.childManager = childManager;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        childManager.saveToLocal(mContext);
//        StringBuilder sb = new StringBuilder();
//        for(String name : names){
//            sb.append(name + "###");
//        }
//        DataUtil.writeOneStringData(mContext, AppDataKey.CHILDREN_NAMES, sb.toString());
    }

    @Override
    public int getCount() {
        // return names.size();
        return childManager.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemEv = convertView.findViewById(R.id.edit_name);
            viewHolder.itemTv = convertView.findViewById(R.id.tv_name);
            viewHolder.editBtn = convertView.findViewById(R.id.btn_edit);
            viewHolder.delBtn = convertView.findViewById(R.id.btn_del);

//            viewHolder.itemEv.setText(names.get(position));
//            viewHolder.itemTv.setText(names.get(position));
            viewHolder.itemEv.setText(childManager.getChild(position).getName());
            viewHolder.itemTv.setText(childManager.getChild(position).getName());

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            viewHolder.itemEv.setText(names.get(position));
//            viewHolder.itemTv.setText(names.get(position));
            viewHolder.itemEv.setText(childManager.getChild(position).getName());
            viewHolder.itemTv.setText(childManager.getChild(position).getName());
        }

        final int pos = position;
        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalViewHolder.itemTv.getVisibility() == View.VISIBLE){
                    finalViewHolder.itemTv.setVisibility(View.INVISIBLE);
                    finalViewHolder.itemEv.setVisibility(View.VISIBLE);
                    finalViewHolder.editBtn.setText("SAVE");
                }else{
                    if(finalViewHolder.itemEv.getText().toString().length() == 0){
                        return;
                    }
                    finalViewHolder.itemTv.setVisibility(View.VISIBLE);
                    finalViewHolder.itemEv.setVisibility(View.INVISIBLE);
                    //finalViewHolder.itemTv.setText(finalViewHolder.itemEv.getText().toString());
                    Log.e("TAG", finalViewHolder.itemEv.getText().toString());
                    finalViewHolder.editBtn.setText("EDIT");
                    // names.set(pos, finalViewHolder.itemEv.getText().toString());
                    childManager.getChild(pos).setName(finalViewHolder.itemEv.getText().toString());
                    ChildrenAdapter.this.notifyDataSetChanged();
                }
            }
        });

        viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // names.remove(pos);
                childManager.remove(pos);
                ChildrenAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView itemTv;
        EditText itemEv;
        Button editBtn;
        Button delBtn;
    }
}
