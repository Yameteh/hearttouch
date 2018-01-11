package com.binary.heart.hearttouch.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.smartlib.log.SmartLog;
/**
 * Created by yaoguoju on 16-4-1.
 */
public class ImHelper {

    //初始化IM模块
    public static void initIm(Context context) {
        final String account = AccountHelper.getAccount(context);
//        ECDeviceKit.init(account, context, new OnInitSDKListener() {
//            @Override
//            public void onInitialized() {
//                ECAuthParameters parameters = new ECAuthParameters();
//                parameters.setAppKey(appKey);
//                parameters.setUserId(account);
//                parameters.setAppToken(appToken);
//                parameters.setLoginMode(ECInitParams.LoginMode.AUTO);
//                parameters.setLoginType(ECInitParams.LoginAuthType.NORMAL_AUTH);
//                ECDeviceKit.login(parameters, new OnConnectSDKListener() {
//                    @Override
//                    public void onConnect() {
//                        SmartLog.d(Configure.TAG,"im connect");
//                    }
//
//                    @Override
//                    public void onDisconnect(ECError error) {
//                        SmartLog.d(Configure.TAG,"im disconnect "+error);
//                    }
//
//                    @Override
//                    public void onConnectState(ECDevice.ECConnectState state, ECError error) {
//
//                    }
//                });
//                IMKitManager.setAutoReceiverOfflineMsg(true);//true 则会自动接收离线消息入库、false则不会
//            }
//
//            @Override
//            public void onError(Exception exception) {
//
//            }
//        });
    }

    
}
