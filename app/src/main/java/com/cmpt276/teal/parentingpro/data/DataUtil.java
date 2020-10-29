package com.cmpt276.teal.parentingpro.data;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * the class contain methods for general saving and reading data from android app
 * the goal for this is having a standard for handling data from the app
 */
public class DataUtil
{
    private final static String APP_SHARE = "parenting_pro";
    private final static String DEFAULT_STRING_VALUE = "NaN";





    /**
     * get the SharedPreferences for the app
     * so we have a standrad sharedPreferences
     * @param context can be any subclass for the context like Activity
     * @return  SharedPreferences for the app
     */
    public static SharedPreferences getSharedPreferences(Context context)
    {
        if(context == null)
            return null;
        return context.getSharedPreferences(APP_SHARE, Context.MODE_PRIVATE);
    }



    /**
     * a way to get editor for the app
     * @param context can be any subclass for the context like Activity
     * @return an editor for SharePreference
     */
    public static SharedPreferences.Editor getSharedEditor(Context context)
    {
        if(context == null)
            return null;
        SharedPreferences sp = getSharedPreferences(context);
        return sp.edit();
    }


    /**
     * writing one stored data in the app
     * data type is String
     * @param context can be any subclass for the context like Activity
     * @param key a Stirng as key too loop in for the data
     * @param value data to store into the app
     */
    public static void writeOneStringData(Context context, String key, String value)
    {
        if(context == null)
            throw new IllegalArgumentException("context can not be null");

        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * get a stored data from app
     * @param context can be any subclass for the context like Activity
     * @param key use as index for getting the data
     * @return  String format of data
     */
    public static String getStringData(Context context, String key)
    {
        if(context == null)
            throw new IllegalArgumentException("context can not be null");

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, DEFAULT_STRING_VALUE);
    }
}
