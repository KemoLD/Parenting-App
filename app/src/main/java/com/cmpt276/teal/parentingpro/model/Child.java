package com.cmpt276.teal.parentingpro.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmpt276.teal.parentingpro.R;

public class Child
{
    private String name;
    private Bitmap profile;
    private Context context;

    public Child(String name, Context context){
        this.name = name;
        this.context = context;
        profile = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_profile_pic);
    }

    public void setProfile(Bitmap bitmap){
        profile = bitmap;
    }

    public Bitmap getProfile(){
        return profile;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Child otherChild){
        if(otherChild.name.equals(this.name))
            return true;
        return false;
    }
}
