package com.example.activigo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.Map;

public class Session {
    /*private SharedPreferences pref;
    Context ctx;
    public Session(Context cntx) {
        pref= PreferenceManager.getDefaultSharedPreferences(cntx);
        ctx=cntx;
    }*/
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static boolean getLoggedIn(Context ctx){
        String loggedin="loggedin";
       // SharedPreferences p = cntx.getSharedPreferences(loggedin, Context.MODE_PRIVATE);
        boolean b=getSharedPreferences(ctx).getBoolean(loggedin, true);
        return b;
    }

    public static void setLoggedIn(boolean loggedin, Context ctx){
        getSharedPreferences(ctx).edit().putBoolean("loggedin", loggedin).apply();
    }

    public static String getid(Context ctx){
        return getSharedPreferences(ctx).getString("id","");
    }

    public static void setid(String id,Context ctx){
        getSharedPreferences(ctx).edit().putString("id", id).apply();
    }

}
