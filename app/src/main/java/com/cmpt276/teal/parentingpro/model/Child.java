package com.cmpt276.teal.parentingpro.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmpt276.teal.parentingpro.R;


/**
 * the class represent a child which have a name and id and image file naeme for it
 */
public class Child
{
    private int id;
    private String name;
    private String imageFileName;
    private static int generateId;


    public Child(String name){
        this(name, null);
    }


    public Child(String name, String imageFileName){
        this(++generateId, name, imageFileName);
    }


    public Child(int id, String name, String imageFileName){
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void updateFromChild(Child child){
        this.id = child.id;
        this.name = child.name;
        this.imageFileName = child.imageFileName;
    }

    public static void setGenerateId(int generateId) {
        Child.generateId = generateId;
    }

    public static int getGenerateId(){
        return Child.generateId;
    }

    public boolean equals(Child otherChild){
        if(otherChild.id == this.id)
            return true;
        return false;
    }

}
