package com.cmpt276.teal.parentingpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmpt276.teal.parentingpro.model.TurnTask;
import com.cmpt276.teal.parentingpro.model.TurnTaskManager;
import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;
import com.cmpt276.teal.parentingpro.ui.ChildUI;
import com.cmpt276.teal.parentingpro.ui.CustomDialog;

/**
 * the class is the adapter that is used by the list view in whose turn activity
 */
public class WhoseTurnAdapter extends BaseAdapter {

    private final Context mContext;
    private final TurnTaskManager manager;
    private final ChildManagerUI childManager;
    private final int popupWidth = 800;
    private final int popupHeight = 1200;


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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemEv = convertView.findViewById(R.id.edit_name);
            viewHolder.itemTv = convertView.findViewById(R.id.tv_name);
            viewHolder.descTv = convertView.findViewById(R.id.tv_desc);
            viewHolder.editBtn = convertView.findViewById(R.id.btn_edit);
            viewHolder.delBtn = convertView.findViewById(R.id.btn_del);
            viewHolder.profile = convertView.findViewById(R.id.profile);

            viewHolder.layout = convertView.findViewById(R.id.item_layer);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        int childIndex = manager.get(position).getChildIndex();
        final ChildUI child = childManager.getChild(childIndex);
        final String childName;
        final Bitmap childImage;
        if(child == null){
            childName = mContext.getString(R.string.empty);
            childImage = ChildManagerUI.defaultChildImage;
        }
        else{
            childName = child.getName();
            childImage = child.getProfile();
        }

        final TurnTask task = manager.get(position);


        viewHolder.itemEv.setText(task.getDescription());
        viewHolder.descTv.setText(task.getDescription());
        viewHolder.itemTv.setText(childName);
        viewHolder.profile.setImageBitmap(childImage);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setMessage(task.getDescription());
                builder.setTitle(childName + " TO DO");
                Log.i("tag", "child image = " + childImage);
                builder.setImage(childImage);
                builder.setPositiveButton("finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        task.setToNextChild(childManager);
                        dialog.dismiss();
                        WhoseTurnAdapter.this.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("cancel",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                Dialog dialog = builder.create();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = popupWidth;
                params.height = popupHeight;
                dialog.getWindow().setAttributes(params);
                dialog.show();
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
                    task.setDescription(finalViewHolder.itemEv.getText().toString());
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
        LinearLayout layout;
        TextView descTv;
        TextView itemTv;
        EditText itemEv;
        Button editBtn;
        Button delBtn;
        ImageView profile;
    }
}
