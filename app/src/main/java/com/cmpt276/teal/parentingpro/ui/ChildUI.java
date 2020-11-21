package com.cmpt276.teal.parentingpro.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.model.Child;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChildUI extends Child
{
    private Bitmap profile;

    private Context context;

    public ChildUI(String name, Context context){
        this(name, null, context);
    }

    public ChildUI(String name, String imageFileName, Context context){
        super(name, imageFileName);
        this.context = context;
        profile = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_profile_pic);
    }

    public ChildUI(Child child, Context context){
        this(child.getName(), child.getImageFileName(), context);
    }

    public void setProfile(Bitmap bitmap){
        profile = bitmap;
    }

    public Bitmap getProfile(){
        return profile;
    }

    public void updateImage(){
        FileInputStream input = null;
        String fileName = super.getImageFileName();
        if(fileName == null)
            return;
        Log.i("tag", "in update image filename = " + fileName);
        try {
            input = context.openFileInput(fileName);
            byte[] imageData = new byte[input.available()];
            input.read(imageData);
            this.profile = BitmapFactory.decodeByteArray(imageData,0, imageData.length);
            Log.i("tag", "in update finish loading image");
        } catch (FileNotFoundException e) {
            Log.i("tag", "can not found the file: " + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("tag", "can not read the data");
            e.printStackTrace();
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void deleteImage(){
        File file = new File(context.getFilesDir(), super.getImageFileName());
        if(file.exists()){
            file.delete();
        }
    }
}
