package com.cmpt276.teal.parentingpro.model;

import android.content.Context;
import android.graphics.Bitmap;

public class Child
{
    private String name;
    private Bitmap profile;
    private final Context context ;

    public Child(String name, Context context){
        this.name = name;
        this.context = context;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfile(Bitmap bitmap){
        profile = bitmap;
    }

    public Bitmap getProfile(){
        return profile;
    }

    public boolean equals(Child otherChild){
        if(otherChild.name.equals(this.name))
            return true;
        return false;
    }
}
