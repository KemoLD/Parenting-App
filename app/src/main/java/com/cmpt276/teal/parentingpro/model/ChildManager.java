package com.cmpt276.teal.parentingpro.model;


import android.content.Context;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;

import java.util.ArrayList;

public class ChildManager
{
    private static ChildManager manager;

    private ArrayList<Child> childrenList;

    public static ChildManager getInstance(){
        if(manager == null){
            manager = new ChildManager();
        }
        return manager;
    }


    private ChildManager(){
        this.childrenList = new ArrayList<>();
    }

    public void addChild(Child child){
        childrenList.add(child);
    }

    public Child getChild(int i){
        return childrenList.get(i);
    }

    public int length(){
        return childrenList.size();
    }

    public boolean isEmpty() { return childrenList.size() == 0; }

    public void remove(Child child){
        childrenList.remove(child);
    }

    public void remove(int index){
        childrenList.remove(index);
    }

    public void loadFromLocal(Context context){
        childrenList.removeAll(childrenList);
        ArrayList<Child> val = DataUtil.getChildData(context);
        childrenList.addAll(val);
    }

    public void saveToLocal(Context context){

        ArrayList<Child> childrenToSave = new ArrayList<Child>();
        childrenToSave.addAll(childrenList);
        DataUtil.writeChildData(context, AppDataKey.CHILDREN_NAMES, childrenToSave);

    }
}
