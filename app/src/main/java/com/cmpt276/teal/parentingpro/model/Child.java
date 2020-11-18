package com.cmpt276.teal.parentingpro.model;

public class Child
{
    private String name;

    public Child(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Child otherChild){
        if(otherChild.name.equals(this.name))
            return true;
        return false;
    }
}
