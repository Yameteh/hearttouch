package com.binary.smartlib.ui.notification;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 
 * @author yaoguoju
 *
 */
public class NotificationCallback {
	
	private NotificationManager notificationManager ;
	private Notification notification;
	private int notifacionId = 0x111;
	private Context context;
	private Class<?> backClass;
	
	/**
	 * 设置要返回的界面Class
	 * @param context
	 * @param backClass
	 */
	public NotificationCallback(Context context,Class<?> backClass) {
		notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.context = context.getApplicationContext();
		this.backClass = backClass;
	}
	
	/**
	 * 添加notification
	 * @param icon
	 * @param title
	 * @param msg
	 */
	@SuppressLint("NewApi")
	public void addNotification(int icon,String title,String msg) {
		Notification.Builder builder = new Notification.Builder(this.context);
		Intent intent = new Intent();
		intent.setClass(context, backClass);
		PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setWhen(System.currentTimeMillis())
;		builder.setContentText(msg);
		builder.setContentTitle(title);
		builder.setOngoing(true);
		builder.setSmallIcon(icon);
		notifacionId = icon;
		this.notification = builder.build();
	}
	
	/**
	 * 添加自定义notification
	 * @param notifcation
	 */
	public void addNotification(Notification notifcation) {
		this.notification = notification;
	}
	
	/**
	 * 在onResume和onPause周期函数调用
	 */
	public void run() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[3];
	    String method = e.getMethodName();
		Log.d("YYY", "method name " +method);
		if("onPause".equals(method)) {
			notificationManager.notify(notifacionId, notification);
		}else if("onResume".equals(method)) {
			notificationManager.cancel(notifacionId);
		}
	}
	
	/**
	 * 获取函数调用方法
	 * @return
	 */
	private String getMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[1];
		return e.getMethodName();
	}
}
