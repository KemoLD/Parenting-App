package com.cmpt276.teal.parentingpro.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmpt276.teal.parentingpro.R;

public class Child
{
    private String name;
    private String imageFileName;

    public Child(String name){
        this.name = name;
    }

    public Child(String name, String imageFileName){
        this.name = name;
        this.imageFileName = imageFileName;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public boolean equals(Child otherChild){
        if(otherChild.name.equals(this.name))
            return true;
        return false;
    }
}
