package com.cmpt276.teal.parentingpro.model;

/**
 * the class represent eash task that need to do which child
 */
public class TurnTask{
    private String description;
    private int childIndex;


    public TurnTask(String description, int childIndex) {
        this.description = description;
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


    public void setToNextChild(ChildManager childManager){
        childIndex = (childIndex + 1) % childManager.length();
    }


    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

}
