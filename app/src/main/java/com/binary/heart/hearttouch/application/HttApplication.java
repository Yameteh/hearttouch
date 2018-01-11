package com.binary.heart.hearttouch.application;


import android.app.Application;
import android.content.Context;

import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.im.ImHelper;
import com.binary.heart.hearttouch.im.notify.HeartNotify;
import com.binary.heart.hearttouch.im.notify.NotifyManager;
import com.binary.heart.hearttouch.map.LocationHelper;
import com.binary.smartlib.handler.SmartHandler;
import com.binary.smartlib.io.SmartDb;
import com.binary.smartlib.io.SmartFile;
import com.binary.smartlib.log.SmartLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by yaoguoju on 16-3-30.
 */
public class HttApplication extends Application {
    private static int statusBarHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        statusBarHeight = getStatusBarHeight(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        LocationHelper.get(this).start();
        SmartHandler.get(this).post(new Runnable() {
            @Override
            public void run() {
                SmartDb.getDb(getApplicationContext()).registerDao(HeartNotify.class);
//                HeartNotify notify = new HeartNotify();
//                notify.setContent("fefef");
//                notify.setType(NotifyManager.BINDREQ);
//                notify.setTime(String.valueOf(System.currentTimeMillis()));
//                notify.setSender("dfdfd");
//                try {
//                    SmartDb.getDb(getApplicationContext()).getSmartDao(HeartNotify.class).insert(notify);
//                } catch (Exception e) {
//                    SmartLog.d(Configure.TAG,"insert error");
//                    e.printStackTrace();
//                }
//
//                try {
//                    List<Object> a = SmartDb.getDb(getApplicationContext()).getSmartDao(HeartNotify.class).queryAll(HeartNotify.class);
//                    for (Object o : a) {
//                        HeartNotify n = (HeartNotify)o;
//                        SmartLog.d(Configure.TAG,"get notify "+n);
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    /**
     * 获取statusbar高度
     * @return
     */
    public static int getStatusBarHeight() {
        return statusBarHeight;
    }

    private int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            SmartLog.d(Configure.TAG, "the status bar height is : " + statusBarHeight);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }



}
