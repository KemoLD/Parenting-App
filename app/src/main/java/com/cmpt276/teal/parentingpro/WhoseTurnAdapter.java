package com.cmpt276.teal.parentingpro;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.model.TurnTaskManager;
import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;
import com.cmpt276.teal.parentingpro.ui.ChildUI;
import com.cmpt276.teal.parentingpro.ui.CustomDialog;

import java.util.List;

public class WhoseTurnAdapter extends BaseAdapter {

    private Context mContext;

    private TurnTaskManager manager;
    private ChildManagerUI childManager;

    public WhoseTurnAdapter(Context context, TurnTaskManager manager, ChildManagerUI childManager) {
        this.mContext = context;
        this.manager = manager;
        this.childManager = childManager;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        manager.saveToLocal(mContext);
    }

    @Override
    public int getCount() {
        // return names.size();
        return manager.length();
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
        ViewHolder viewHolder;
        final ChildUI child = childManager.getChild(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemEv = convertView.findViewById(R.id.edit_name);
            viewHolder.itemTv = convertView.findViewById(R.id.tv_name);
            viewHolder.descTv = convertView.findViewById(R.id.tv_desc);
            viewHolder.editBtn = convertView.findViewById(R.id.btn_edit);
            viewHolder.delBtn = convertView.findViewById(R.id.btn_del);

            viewHolder.layout = convertView.findViewById(R.id.item_layer);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemEv.setText(manager.get(position).getDescription());
        viewHolder.descTv.setText(manager.get(position).getDescription());
        // viewHolder.itemTv.setText(manager.get(position).getChild());
       viewHolder.itemTv.setText(child.getName());

//        if(manager.get(position).getStatus() == 1){
//            viewHolder.editBtn.setEnabled(false);
//            viewHolder.itemTv.setText(manager.get(position).getChild() + " Finished");
//        }

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(manager.get(position).getStatus() != 0)return;
                //Toast.makeText(mContext, "Toast", Toast.LENGTH_SHORT).show();

                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setMessage(manager.get(position).getDescription());
                builder.setTitle(child.getName() + " TO DO");
                builder.setPositiveButton("finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // manager.get(position).setStatus(1);
                        WhoseTurnAdapter.this.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("cancel",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });

        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalViewHolder.descTv.getVisibility() == View.VISIBLE){
                    finalViewHolder.descTv.setVisibility(View.INVISIBLE);
                    finalViewHolder.itemEv.setVisibility(View.VISIBLE);
                    finalViewHolder.editBtn.setText(R.string.save_button_text);
                }else{
                    if(finalViewHolder.itemEv.getText().toString().length() == 0){
                        return;
                    }
                    finalViewHolder.descTv.setVisibility(View.VISIBLE);
                    finalViewHolder.itemEv.setVisibility(View.INVISIBLE);
                    Log.e("TAG", finalViewHolder.itemEv.getText().toString());
                    finalViewHolder.editBtn.setText(R.string.edit_button_text);
                    manager.get(position).setDescription(finalViewHolder.itemEv.getText().toString());
                    WhoseTurnAdapter.this.notifyDataSetChanged();
                }
            }
        });

        viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.remove(position);
                WhoseTurnAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        RelativeLayout layout;
        TextView descTv;
        TextView itemTv;
        EditText itemEv;
        Button editBtn;
        Button delBtn;
    }
}
