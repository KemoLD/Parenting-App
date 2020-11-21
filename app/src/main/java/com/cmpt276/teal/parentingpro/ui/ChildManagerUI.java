package com.cmpt276.teal.parentingpro.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
    private static ChildManagerUI manager;

    public static ChildManagerUI getInstance(Context context){
        if(manager == null){
            manager = new ChildManagerUI(context);
        }
        return manager;
    }

    protected ChildManagerUI(Context context){
        super();
        this.context = context;
        // loading the default image first as static to reduce the work load for the system
        defaultChildImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_profile_pic);    //
    }


    public void addChild(ChildUI child){
        super.addChild(child);

        // check if the image profile already exist if not add one
        if(child.getProfile() == null){
            // check if the child set the image name before if not set to default image
            String imageFileName = child.getImageFileName();
            if(imageFileName == null || imageFileName.equals("")){
                child.setProfile(defaultChildImage);
            }
            else{
                Thread setImageTask = new Thread(new LoadImageTask(child, imageFileName));
                setImageTask.start();
            }
        }
    }


    public void remove(int index){
        ChildUI child = getChild(index);
        // remove the image in the disk first
        final String imageName = child.getImageFileName();
        if(imageName != null && !imageName.equals("")){
            Thread deleteFileTask = new Thread(new Runnable() {
                @Override
                public void run() {
                    DataUtil.removeInternalFile(context, imageName);
                }
            });
            deleteFileTask.start();
        }
        super.remove(index);
    }

    public ChildUI getChild(int index){
        Child child = super.getChild(index);
        if(child instanceof ChildUI){
            return (ChildUI)child;
        }
        else{
            throw new RuntimeException("child in manager is not instance of ChildUI class");
        }
    }

    public void loadFromLocal(Context context){
        removeAll();
        String childListData = DataUtil.getStringData(context, AppDataKey.CHILDRENS);
        if(childListData == null || childListData.length() == 0 || childListData.equals(DataUtil.DEFAULT_STRING_VALUE)){
            return;
        }
        String[] childList = childListData.split(CHILD_LIST_SERERATOR);
        Log.i("tag", "childListData = " + childListData);
        Log.i("tag", "child list length = " + childList.length);
        for(String childData : childList){
            Log.i("tag", "child data = " + childData);
            if(childData != null && !childData.equals(DataUtil.DEFAULT_STRING_VALUE)){
                ChildUI child = ChildUI.buildChildFromData(context, childData, CHILD_FILE_SEPERATOR);
                manager.addChild(child);
                String imageFileName = child.getImageFileName();
                if(imageFileName == null || imageFileName.equals("")){
                    child.setProfile(defaultChildImage);
                }
                else{
                    // create other thread so to reduce the work load in main thread
                    // also able to load the image while the program is running
                    Thread setImageTask = new Thread(new LoadImageTask(child, imageFileName));
                    setImageTask.start();
                }


            }
        }
    }

    public void saveToLocal(Context context){
        StringBuilder childListString = new StringBuilder();
        for(int i = 0; i < manager.length(); i++){
            ChildUI child = this.getChild(i);
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
            Log.i("tag", "in run");
            Log.i("tag", "context = " + context + " fileName = " + fileName);
            byte[] imageData = DataUtil.getInteralFileInBytes(context, fileName);
            if(imageData == null || imageData.length == 0){
                child.setProfile(defaultChildImage);
            }
            else{
                Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                child.setProfile(image);
            }
        }
    }
}
