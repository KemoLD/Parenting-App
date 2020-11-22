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
import java.nio.ByteBuffer;

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        return bStream.toByteArray();
    }

}
