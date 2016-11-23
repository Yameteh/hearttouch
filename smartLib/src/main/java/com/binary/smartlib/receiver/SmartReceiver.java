package com.binary.smartlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import com.binary.smartlib.container.SmartList;

import java.util.List;

/**
 * Created by yaoguoju on 15-12-29.
 */
public abstract class SmartReceiver extends BroadcastReceiver{
    private final static String TAG = "SmartReceiver";
    private Context context;

    public SmartReceiver(Context context, String[] actions) {
        this.context = context.getApplicationContext();
        IntentFilter filter = new IntentFilter();
        if(actions != null && actions.length > 0) {
            for(String action : actions) {
                filter.addAction(action);
            }
        }
        context.registerReceiver(this, filter);
    }

    /**
     * 回收receiver
     */
    public void recycleReceiver() {
        this.context.unregisterReceiver(this);
    }

}

