package com.cmpt276.teal.parentingpro.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.model.Child;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ChildManagerUI extends ChildManager
{
    private Context context;
    private final String CHILD_FILE_SEPERATOR = "#####";
    private final String CHILD_LIST_SERERATOR = "@@@@@";
    public static Bitmap defaultChildImage = null;
    private ChildManagerUI manager;

    public ChildManagerUI getInstance(Context context){
        if(manager == null){
            manager = new ChildManagerUI(context);
        }
        return manager;
    }

    protected ChildManagerUI(Context context){
        super();
        // loading the default image first as static to reduce the work load for the system
        defaultChildImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_profile_pic);    //
    }

    public void loadFromLocal(Context context){
        String childListData = DataUtil.getStringData(context, AppDataKey.CHILDRENS);
        String[] childList = childListData.split(CHILD_LIST_SERERATOR);
        for(String childData : childList){
            if(childData != null && !childData.equals("")){
                ChildUI child = ChildUI.buildChildFromData(context, childData, CHILD_FILE_SEPERATOR);
                manager.addChild(child);
                String imageFileName = child.getImageFileName();
                if(imageFileName == null || imageFileName.equals("")){
                    child.setProfile(defaultChildImage);
                }
                else{
                    // create other thread so to reduce the work load in main thread
                    // also able to load the image while the program is running
                    Thread loadImageTask = new Thread(new LoadImageTask(child, imageFileName));
                    loadImageTask.start();
                }


            }
        }
    }

    public void saveToLocal(Context context){
        StringBuilder childListString = new StringBuilder();
        for(int i = 0; i < manager.length(); i++){
            ChildUI child = (ChildUI) manager.getChild(i);
            childListString.append(child.getChildDataString(CHILD_FILE_SEPERATOR));
            childListString.append(CHILD_LIST_SERERATOR);
        }
        DataUtil.writeOneStringData(context, AppDataKey.CHILDRENS, childListString.toString());
    }





    private class LoadImageTask implements Runnable
    {
        ChildUI child;
        String fileName;
        public LoadImageTask(ChildUI child, String fileName){
            this.child = child;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            byte[] imageData = DataUtil.getInteralFileInBytes(context, fileName);
            Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            child.setProfile(image);
        }
    }
}
