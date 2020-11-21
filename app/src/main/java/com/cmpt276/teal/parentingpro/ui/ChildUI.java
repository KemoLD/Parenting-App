package com.cmpt276.teal.parentingpro.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cmpt276.teal.parentingpro.R;
import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.model.Child;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChildUI extends Child
{
    private Bitmap profile;

    public ChildUI(String name){
        this(name, null);
    }

    public ChildUI(String name, String imageFileName){
        super(name, imageFileName);
        profile = null;
    }

    public ChildUI(Child child, Context context){
        this(child.getName(), child.getImageFileName());
    }

    public void setProfile(Bitmap bitmap){
        profile = bitmap;
    }

    public Bitmap getProfile(){
        return profile;
    }

    public String getChildDataString(String seperator){
        return this.getName() + seperator + this.getImageFileName();
    }


    public static ChildUI buildChildFromData(Context context, String data, String seperator){
        String[] fields = data.split(seperator);
        return new ChildUI(fields[0], fields[1]);
    }


    public byte[] converProfileToBytes(){
        if(this.profile == null)
            return null;
        return coverBitmapToBytes(this.profile);
    }

    public static byte[] coverBitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        return bStream.toByteArray();
    }
/*
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
    */


/*
    public void deleteImage(){
        File file = new File(context.getFilesDir(), super.getImageFileName());
        if(file.exists()){
            file.delete();
        }
    }


 */
}
