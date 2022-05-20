package com.example.contacts.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {
    private final String MY_PREFS_NAME = "MyPrefsFile";
    private SharedPreferences prefs;
    private static MSP me = null;

    private MSP(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
    }

    public static MSP getMe(Context context) {
        if(me == null) {
            me = new MSP(context);
        }
        return me;
    }

    public void putStringToSP(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringFromSP(String key, String def) {
        return prefs.getString(key, def);
    }

    public void putBooleanToSP(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }


    public boolean getBooleanFromSP(String key, boolean def) { return prefs.getBoolean(key,def); }
}
