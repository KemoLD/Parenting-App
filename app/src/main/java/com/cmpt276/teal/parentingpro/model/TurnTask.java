package com.cmpt276.teal.parentingpro.model;

public class TurnTask{
    private String description;
    private int childIndex;
    // private String child;
    // private int status; // 0 create 1 finish

    public TurnTask(String description, int childIndex) {
        this.description = description;
//        this.child = child;
//        this.status = 0;
        this.childIndex = childIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }
//    public String getChild() {
//        return child;
//    }

//    public void setChild(String child) {
//        this.child = child;
//    }

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
}
