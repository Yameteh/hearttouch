package com.binary.heart.hearttouch.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beetle.bauhinia.api.IMHttpAPI;
import com.beetle.bauhinia.api.body.PostDeviceToken;
import com.beetle.bauhinia.db.GroupMessageHandler;
import com.beetle.bauhinia.db.PeerMessageHandler;
import com.beetle.bauhinia.db.SyncKeyHandler;
import com.beetle.im.IMService;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.smartlib.log.SmartLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaoguoju on 16-4-1.
 */
public class ImHelper {
    private static final String TAG = "ImHelper";
    //初始化IM模块
    public static void login(Context context,int uid,String token) {
        Log.d(TAG,"uid "+uid+" token "+token);
        final String account = AccountHelper.getAccount(context);
        IMService.getInstance().stop();
        long userId = Long.valueOf(uid);
        PeerMessageHandler.getInstance().setUID(userId);
        GroupMessageHandler.getInstance().setUID(userId);
        IMHttpAPI.setToken(token);
        IMService.getInstance().setToken(token);
        IMService.getInstance().setAppID(1);
        SyncKeyHandler handler = new SyncKeyHandler(context, "sync_key");
        handler.load();

        HashMap<Long, Long> groupSyncKeys = handler.getSuperGroupSyncKeys();
        IMService.getInstance().clearSuperGroupSyncKeys();
        for (Map.Entry<Long, Long> e : groupSyncKeys.entrySet()) {
            IMService.getInstance().addSuperGroupSyncKey(e.getKey(), e.getValue());
            Log.i(TAG, "group id:" + e.getKey() + "sync key:" + e.getValue());
        }
        IMService.getInstance().setSyncKey(handler.getSyncKey());
        Log.i(TAG, "sync key:" + handler.getSyncKey());
        IMService.getInstance().setSyncKeyHandler(handler);

        IMService.getInstance().start();

//        IMDemoApplication app = (IMDemoApplication)getApplication();
//        String deviceToken = app.getDeviceToken();
//        if (token != null && deviceToken != null && deviceToken.length() > 0) {
//            PostDeviceToken tokenBody = new PostDeviceToken();
//            tokenBody.xgDeviceToken = deviceToken;
//            IMHttpAPI.Singleton().bindDeviceToken(tokenBody)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Object>() {
//                        @Override
//                        public void call(Object obj) {
//                            Log.i("im", "bind success");
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            Log.i("im", "bind fail");
//                        }
//                    });
//        }
    }

    
}
