package com.cmpt276.teal.parentingpro.model;


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


    public void remove(Child child){
        childrenList.remove(child);
    }
}
