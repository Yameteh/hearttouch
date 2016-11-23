package com.binary.heart.hearttouch.im;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.binary.heart.hearttouch.im.imessage.MessageFactory;
import com.binary.heart.hearttouch.im.notify.HeartNotify;
import com.binary.heart.hearttouch.im.notify.NotifyManager;
import com.binary.smartlib.io.SmartDb;
import com.binary.smartlib.ui.toast.SmartToast;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

/**
 * Created by yaoguoju on 16-4-20.
 */
public class HeartMessageRecevier extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ECMessage msg = intent.getParcelableExtra("msg");
        String msgUserData = msg.getUserData();
        String[] m = msgUserData.split(":");
        int type = Integer.parseInt(m[1]);
        switch ( type) {
            case MessageFactory.GAME_INVITE:
                SmartToast.show(context,"收到猜拳邀请",1000);
                NotifyManager.notifyCaiquanInvite(context);
                break;
            case MessageFactory.BIND_REQ:
                HeartNotify notify = new HeartNotify();
                ECTextMessageBody body = (ECTextMessageBody) msg.getBody();
                notify.setContent(body.getMessage());
                notify.setType(NotifyManager.BINDREQ);
                notify.setSender(msg.getSessionId());
                notify.setTime(String.valueOf(System.currentTimeMillis()));
                try {
                    SmartDb.getDb(context).getSmartDao(HeartNotify.class).insert(notify);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }


}
