package com.cmpt276.teal.parentingpro.data;

import com.cmpt276.teal.parentingpro.model.Child;

import java.util.ArrayList;

public class History
{
    private ArrayList<HistoryData> historyArray;

    private static History history;

    public static History getInstance() {
        if(history == null) {
            history = new History();
        }
        return history;
    }


    private History(){
        this.historyArray = new ArrayList<>();
    }


    public void addHistory(HistoryData data){
        if(data == null)
            return;
        historyArray.add(data);
    }

    public HistoryData getHistoryData(int index){
        return historyArray.get(index);
    }

    public ArrayList getAllHistoryList(){
        return copyList(this.historyArray);
    }


    public ArrayList getHistoryListWithChild(Child child){
        ArrayList<HistoryData> output = new ArrayList<>();
        for(HistoryData data : this.historyArray){
            Child childInList = data.getChild();
            if(child.equals(childInList)){
                output.add(data.clone());
            }
        }
        return output;
    }


    private ArrayList copyList(ArrayList<HistoryData> dataList){
        ArrayList<HistoryData> list = new ArrayList(dataList.size());
        for(HistoryData data : dataList){
            list.add(data.clone());
        }
        return list;
    }

}
