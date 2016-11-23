package com.binary.heart.hearttouch.im.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.activity.CaiQuanActivity;
import com.yuntongxun.kitsdk.core.ECKitConstant;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;

/**
 * Created by yaoguoju on 16-4-20.
 */
public class NotifyManager {

    public static final int CAIQUAN = 10;
    public static final int BINDREQ = 100;
    public static void notifyCaiquanInvite (Context context){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_caiquan, "收到猜拳邀请",
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        Intent intent = new Intent(context, CaiQuanActivity.class);
        intent.putExtra(CaiQuanActivity.FROM_NOTIFY,true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context,
                "猜拳邀请",
                null,
                contentIntent);

        nm.notify(CAIQUAN,notification);
    }
}
