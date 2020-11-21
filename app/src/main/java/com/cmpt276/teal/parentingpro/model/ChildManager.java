package com.cmpt276.teal.parentingpro.model;


import android.content.Context;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ChildManager
{
    private static ChildManager manager;
    private Gson gson;

    private ArrayList<Child> childrenList;

    public static ChildManager getInstance(){
        if(manager == null){
            manager = new ChildManager();
        }
        return manager;
    }


    protected ChildManager(){
        this.childrenList = new ArrayList<>();
        this.gson = new Gson();
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
        String childrenDataString = DataUtil.getStringData(context, AppDataKey.CHILDRENS);
        if(childrenDataString.equals(DataUtil.DEFAULT_STRING_VALUE))
            return;
        ArrayList<Child> dataList = (ArrayList<Child>)gson.fromJson(childrenDataString, new TypeToken<ArrayList<Child>>(){}.getType());
        childrenList = dataList;
    }

    public void saveToLocal(Context context){
        String savedDataStr = gson.toJson(childrenList);
        DataUtil.writeOneStringData(context, AppDataKey.CHILDRENS, savedDataStr);
    }
}
