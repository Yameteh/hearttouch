package com.binary.smartlib.ui.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yaoguoju on 15-12-25.
 */
public class SmartToast {

    /**
     * 显示String消息
     * @param context
     * @param msg
     * @param duration
     */
    public static void show(Context context,String msg,int duration) {
        Toast.makeText(context,msg,duration).show();
    }

    /**
     * 显示res string消息
     * @param context
     * @param resid
     * @param duration
     */
    public static void show(Context context,int resid,int duration) {
        Toast.makeText(context,resid,duration).show();
    }


}
