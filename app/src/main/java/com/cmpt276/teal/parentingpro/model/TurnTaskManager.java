package com.cmpt276.teal.parentingpro.model;


import android.content.Context;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;

import java.util.ArrayList;

/**
 * the class manager a list of the TurnTask
 */
public class TurnTaskManager
{
    private static TurnTaskManager manager;
    private ArrayList<TurnTask> turnTasks;


    public static TurnTaskManager getInstance(){
        if(manager == null){
            manager = new TurnTaskManager();
        }
        return manager;
    }


    private TurnTaskManager(){
        this.turnTasks = new ArrayList<>();
    }


    public void addTask(TurnTask task){
        turnTasks.add(task);
    }


    public TurnTask get(int i){
        return turnTasks.get(i);
    }


    public int length(){
        return turnTasks.size();
    }


    public boolean isEmpty() { return turnTasks.size() == 0; }


    public void remove(TurnTask task){
        turnTasks.remove(task);
    }


    public void remove(int index){
        turnTasks.remove(index);
    }


    public void loadFromLocal(Context context, ChildManager childManager){
        turnTasks.clear();
        String val = DataUtil.getStringData(context, AppDataKey.CHILDREN_TURN_TASKS);
        if (!val.equals(DataUtil.DEFAULT_STRING_VALUE)) {
            String[] names = val.split("###");
            for(String n : names){
                String[] ss = n.split("@@@");
                if(!n.isEmpty()){
                    int correctIndex = getCorrectIndex(Integer.parseInt(ss[1]), childManager);
                    TurnTask task = new TurnTask(ss[0], correctIndex);
                    turnTasks.add(task);
                }
            }
        }
    }


    public void saveToLocal(Context context){
        StringBuilder sb = new StringBuilder();
        for(TurnTask task : turnTasks){
            // sb.append(task.getDescription() + "@@@" + task.getChild() + "@@@" + task.getStatus() + "###");
            sb.append(task.getDescription() + "@@@" + task.getChildIndex() + "@@@" + "123" + "###");
        }
        DataUtil.writeOneStringData(context, AppDataKey.CHILDREN_TURN_TASKS, sb.toString());
    }


    public int getCorrectIndex(int childIndex, ChildManager childManager){
        int correctIndex = childIndex;
        if(childIndex < 0){
            correctIndex = 0;
        }
        else if(childIndex >= childManager.length()){
            correctIndex = childManager.length() - 1;
        }
        return correctIndex;
    }
}
