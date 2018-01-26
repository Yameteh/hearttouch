package com.binary.heart.hearttouch.application;


import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.beetle.bauhinia.api.IMHttpAPI;
import com.beetle.bauhinia.db.CustomerMessageDB;
import com.beetle.bauhinia.db.CustomerMessageHandler;
import com.beetle.bauhinia.db.GroupMessageDB;
import com.beetle.bauhinia.db.GroupMessageHandler;
import com.beetle.bauhinia.db.PeerMessageDB;
import com.beetle.bauhinia.db.PeerMessageHandler;
import com.beetle.bauhinia.tools.FileCache;
import com.beetle.im.IMService;
import com.beetle.im.VOIPControl;
import com.beetle.im.VOIPObserver;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.im.ImHelper;
import com.binary.heart.hearttouch.im.notify.HeartNotify;
import com.binary.heart.hearttouch.im.notify.NotifyManager;
import com.binary.heart.hearttouch.map.LocationHelper;
import com.binary.smartlib.handler.SmartHandler;
import com.binary.smartlib.io.SmartDb;
import com.binary.smartlib.io.SmartFile;
import com.binary.smartlib.log.SmartLog;
import com.binary.webrtc.AppRTCClient;
import com.binary.webrtc.ConnectActivity;
import com.binary.webrtc.ImRTCClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by yaoguoju on 16-3-30.
 */
public class HttApplication extends Application implements ImRTCClient.ImInterface{
    private static int statusBarHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        statusBarHeight = getStatusBarHeight(this);
       // LocationHelper.get(this).start();
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

        initIM();
    }

    private void initIM() {
        IMService mIMService = IMService.getInstance();
        //app可以单独部署服务器，给予第三方应用更多的灵活性
        mIMService.setHost(WebUrls.IM_ROOT_HOST);

        String androidID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        //设置设备唯一标识,用于多点登录时设备校验
        mIMService.setDeviceID(androidID);

        //监听网路状态变更
        mIMService.registerConnectivityChangeReceiver(getApplicationContext());

        //可以在登录成功后，设置每个用户不同的消息存储目录
        FileCache fc = FileCache.getInstance();
        fc.setDir(this.getDir("cache", MODE_PRIVATE));

        PeerMessageDB db = PeerMessageDB.getInstance();
        db.setDir(this.getDir("peer", MODE_PRIVATE));
        GroupMessageDB groupDB = GroupMessageDB.getInstance();
        groupDB.setDir(this.getDir("group", MODE_PRIVATE));
        CustomerMessageDB csDB = CustomerMessageDB.getInstance();
        csDB.setDir(this.getDir("customer_service", MODE_PRIVATE));

        mIMService.setPeerMessageHandler(PeerMessageHandler.getInstance());
        mIMService.setGroupMessageHandler(GroupMessageHandler.getInstance());
        mIMService.setCustomerMessageHandler(CustomerMessageHandler.getInstance());
        mIMService.pushVOIPObserver(new VOIPObserver() {
            @Override
            public void onVOIPControl(VOIPControl ctl) {
                AppRTCClient.ImRTCSession session = ImRTCClient.getImRTCClient().getImRTCSession();
                if(session == null) {
                    session = new AppRTCClient.ImRTCSession();
                    session.receiver = ctl.receiver;
                    session.sender = ctl.sender;
                    session.direct = AppRTCClient.ImRTCSession.DIRECT_IN;
                    session.state = AppRTCClient.ImRTCSession.STATE_RING;
                    String msg = new String(ctl.content);
                    Log.d(Configure.TAG,"direct_in receiver "+session.receiver+" sender "+session.sender +
                                               " state "+session.state+" msg "+msg);
                    ImRTCClient.getImRTCClient().addImRTCSession(session);
                    ConnectActivity.connectToRoom(getApplicationContext(),"172.25.1.12",false,false,msg,0);
                }else {
                    if(session.direct == AppRTCClient.ImRTCSession.DIRECT_IN) {
                        if(session.receiver == ctl.receiver && session.sender == ctl.sender) {
                            String msg = new String(ctl.content);
                            Log.d(Configure.TAG,"direct_in receiver "+session.receiver+" sender "+session.sender +
                                    " state "+session.state+" msg "+msg);
                            ImRTCClient.getImRTCClient().onImMessageReceived(msg);
                        }else {
                            Log.d(Configure.TAG,"direct_in receiver "+ctl.receiver+" sender "+ctl.sender);
                        }
                    }else if(session.direct == AppRTCClient.ImRTCSession.DIRECT_OUT) {
                        if(session.sender == ctl.receiver && session.receiver == ctl.sender) {
                            String msg = new String(ctl.content);
                            Log.d(Configure.TAG,"direct_out receiver "+session.receiver+" sender "+session.sender +
                                    " state "+session.state+" msg "+msg);
                            ImRTCClient.getImRTCClient().onImMessageReceived(msg);
                        }else {
                            Log.d(Configure.TAG,"direct_out receiver "+ctl.receiver+" sender "+ctl.sender);
                        }
                    }

                }
            }
        });
        ImRTCClient.getImRTCClient().setImInterface(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.binary.hearttouch.VIDEO_CALL");
        registerReceiver(videoCall,filter);
        //预先做dns查询
        //refreshHost();
    }

    private BroadcastReceiver videoCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long sender = intent.getLongExtra("sender",0);
            long receiver = intent.getLongExtra("receiver",0);
            AppRTCClient.ImRTCSession session =  ImRTCClient.getImRTCClient().getImRTCSession();
            if(session == null) {
                session.receiver = receiver;
                session.sender = sender;
                session.direct = AppRTCClient.ImRTCSession.DIRECT_OUT;
                session.state = AppRTCClient.ImRTCSession.STATE_HOOK;
                ImRTCClient.getImRTCClient().addImRTCSession(session);
                ConnectActivity.connectToRoom(getApplicationContext(),"172.25.1.12",false,false,"",0);
            }
        }
    };



    private void refreshHost() {
        new AsyncTask<Void, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Void... urls) {
                for (int i = 0; i < 10; i++) {
                    String imHost = lookupHost("imnode.gobelieve.io");
                    String apiHost = lookupHost("api.gobelieve.io");
                    if (TextUtils.isEmpty(imHost) || TextUtils.isEmpty(apiHost)) {
                        try {
                            Thread.sleep(1000 * 1);
                        } catch (InterruptedException e) {
                        }
                        continue;
                    } else {
                        break;
                    }
                }
                return 0;
            }

            private String lookupHost(String host) {
                try {
                    InetAddress inetAddress = InetAddress.getByName(host);
                    Log.i("beetle", "host name:" + inetAddress.getHostName() + " " + inetAddress.getHostAddress());
                    return inetAddress.getHostAddress();
                } catch (UnknownHostException exception) {
                    exception.printStackTrace();
                    return "";
                }
            }
        }.execute();
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


    @Override
    public void sendMessage(String msg) {
        Log.d(Configure.TAG,"send message "+msg);
        AppRTCClient.ImRTCSession session = ImRTCClient.getImRTCClient().getImRTCSession();
        if(session != null) {
            VOIPControl ctl = new VOIPControl();
            if(session.direct == AppRTCClient.ImRTCSession.DIRECT_IN) {
                ctl.sender = session.receiver;
                ctl.receiver = session.sender;
            }else if(session.direct == AppRTCClient.ImRTCSession.DIRECT_OUT){
                ctl.sender = session.sender;
                ctl.receiver = session.receiver;
            }
            try {
                ctl.content = msg.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            IMService.getInstance().sendVOIPControl(ctl);
        }
    }
}
