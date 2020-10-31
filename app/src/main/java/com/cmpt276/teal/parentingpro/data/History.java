package com.cmpt276.teal.parentingpro.data;

import android.content.Context;

import com.cmpt276.teal.parentingpro.model.Child;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class History
{
    private ArrayList<HistoryData> historyArray;
    private Gson gson;

    private static History history;

    public static History getInstance() {
        if(history == null) {
            history = new History();
        }
        return history;
    }



    private History(){
        this.historyArray = new ArrayList<>();
        this.gson = new Gson();
    }



    public void addHistory(HistoryData data){
        if(data == null)
            return;
        historyArray.add(data);
    }



    public int numOfHistory(){
        return historyArray.size();
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



    public void saveToLocal(Context context){
        String savedDataStr = gson.toJson(historyArray);
        DataUtil.writeOneStringData(context, AppDataKey.COIN_HISTORY, savedDataStr);
    }



    public void loadFromLocal(Context context){
        String savedDataStr = DataUtil.getStringData(context, AppDataKey.COIN_HISTORY);

        // do not have any history yet
        if(savedDataStr.equals(DataUtil.DEFAULT_STRING_VALUE))
           return;
        System.out.println(savedDataStr);
        ArrayList<HistoryData> localHistoryArray = (ArrayList<HistoryData>)gson.fromJson(savedDataStr, new TypeToken<ArrayList<HistoryData>>(){}.getType());

        this.historyArray = localHistoryArray;
    }



    public String toString(){
        String output = "";
        for(HistoryData data : historyArray){
            output += data.toString() + "\n";
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
