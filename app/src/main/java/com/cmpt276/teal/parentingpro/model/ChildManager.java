package com.cmpt276.teal.parentingpro.model;


import android.content.Context;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.data.HistoryData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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
        try{
            return childrenList.get(i);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }
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

    public void removeAll(){
        childrenList.removeAll(childrenList);
    }

    public void move(int srcIndex, int desIndex){
        if(srcIndex == desIndex)
            return;
        Child temp = getChild(srcIndex);
        if(srcIndex < desIndex){
            for(int i = srcIndex; i < desIndex; i++){
                childrenList.set(i, childrenList.get(i + 1));
            }
        }
        else{
            for(int i = srcIndex; i > desIndex; i--){
                childrenList.set(i, childrenList.get(i - 1));
            }
        }

        childrenList.set(desIndex, temp);
    }


    public void loadFromLocal(Context context){
        removeAll();
        String childrenDataString = DataUtil.getStringData(context, AppDataKey.CHILDRENS);
        if(childrenDataString.equals(DataUtil.DEFAULT_STRING_VALUE))
            return;
        try {
            ArrayList<Child> dataList = (ArrayList<Child>) gson.fromJson(childrenDataString, new TypeToken<ArrayList<Child>>() {
            }.getType());
            childrenList = dataList;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getChilds(){
        List<String> s = new ArrayList<>();
        for(Child c : childrenList){
            s.add(c.getName());
        }
        return s;
    }

    public void saveToLocal(Context context){
        String savedDataStr = gson.toJson(childrenList);
        DataUtil.writeOneStringData(context, AppDataKey.CHILDRENS, savedDataStr);
    }
}
