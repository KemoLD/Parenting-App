package com.cmpt276.teal.parentingpro.model;

public class TurnTask{
    private String description;
    private String child;
    private int status; // 0 create 1 finish

    public TurnTask(String description, String child) {
        this.description = description;
        this.child = child;
        this.status = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
