package com.binary.smartlib.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.binary.smartlib.container.SmartList;
import com.binary.smartlib.container.SmartMap;
import com.binary.smartlib.log.SmartLog;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用初始化的时候调用get创建应用全局的Handler
 * Created by yaoguoju on 15-12-23.
 */
public final class SmartHandler extends Handler{

    private final static String TAG = "SmartHandler";
    private Context context;
    private static SmartHandler mInstance;
    private Handler mHandler;
    private List<SmartMessage> mMsgList;

    /**
     * 创建单例
     * @param context
     * @return
     */
    public static SmartHandler get(Context context) {
        if(mInstance == null) {
            mInstance = new SmartHandler(context);
        }
        return mInstance;
    }

    /**
     * 构造方法
     * @param context
     */
    private SmartHandler(Context context) {
        super(context.getMainLooper());
        this.context = context.getApplicationContext();
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int code = msg.what;
        SmartMessage sMsg;

        if(mMsgList != null) {
            SmartLog.d(TAG,"mMsgList start size "+mMsgList.size());
            for (int i = 0; i < SmartList.size(mMsgList); i++) {
                sMsg = SmartList.get(mMsgList, i);
                if (sMsg.getMsg().what == code) {
                    if (sMsg.handleMessage(sMsg.getClassTag(), msg)) {
                        SmartList.del(mMsgList, sMsg);
                    }
                }
            }
            SmartLog.d(TAG,"mMsgList end size "+mMsgList.size());
        }

    }


    /**
     * Hanlder 推送message
     * @param msg
     */
    public void postSmartMessage(SmartMessage msg,long delayTime) {
        if(mMsgList == null) {
            mMsgList = SmartList.newList();
        }

        if(!SmartList.contains(mMsgList,msg)) {
            SmartList.add(mMsgList,msg);
        }

        msg.create();
        if(delayTime <=0) {
            this.sendMessage(msg.getMsg());
        }else {
            this.sendMessageDelayed(msg.getMsg(),delayTime);
        }
    }

    /**
     * Handler 推送Runnable
     * @param r
     * @param delayTime
     */
    public void postSmartRunnable(SmartRunnable r,long delayTime) {
        if(delayTime <= 0) {
            this.post(r.getRunable());
        }else {
            this.postDelayed(r.getRunable(), delayTime);
        }
    }

    /**
     * Handler取消推送消息
     * @param r
     */
    public void removeRunnable(SmartRunnable r) {
        this.removeCallbacks(r.getRunable());
    }
}
