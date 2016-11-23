package com.binary.smartlib.log;

import android.util.Log;

/**
 * Created by yaoguoju on 15-12-23.
 */
public class SmartLog {
    public final static int version = 0;
    public final static int debug   = 1;
    public final static int info    = 2;
    public final static int error   = 3;
    

    public static int level   = version;

    public static void i(String TAG,String msg) {
        if(info >= level) {
            Log.i(TAG,msg);
        }
    }

    public static void d(String TAG,String msg) {
        if(debug >= level) {
            Log.d(TAG,msg);
        }
    }

    public static void e(String TAG,String msg) {
        if(error >= level) {
            Log.e(TAG,msg);
        }
    }
    
    public static void setLogLevel(int l) {
    	level = l;
    }
}
