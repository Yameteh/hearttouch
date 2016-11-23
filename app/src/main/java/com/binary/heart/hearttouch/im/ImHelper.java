package com.binary.heart.hearttouch.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.smartlib.log.SmartLog;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.kitsdk.ECDeviceKit;
import com.yuntongxun.kitsdk.beans.ECAuthParameters;
import com.yuntongxun.kitsdk.listener.OnConnectSDKListener;
import com.yuntongxun.kitsdk.listener.OnInitSDKListener;
import com.yuntongxun.kitsdk.IMKitManager;
/**
 * Created by yaoguoju on 16-4-1.
 */
public class ImHelper {
    public static final String appKey = "aaf98f8953cadc690153d1041d5c328d";
    public static final String appToken = "4355498bde31985766708687ebf7302c";

    //初始化IM模块
    public static void initIm(Context context) {
        final String account = AccountHelper.getAccount(context);
        ECDeviceKit.init(account, context, new OnInitSDKListener() {
            @Override
            public void onInitialized() {
                ECAuthParameters parameters = new ECAuthParameters();
                parameters.setAppKey(appKey);
                parameters.setUserId(account);
                parameters.setAppToken(appToken);
                parameters.setLoginMode(ECInitParams.LoginMode.AUTO);
                parameters.setLoginType(ECInitParams.LoginAuthType.NORMAL_AUTH);
                ECDeviceKit.login(parameters, new OnConnectSDKListener() {
                    @Override
                    public void onConnect() {
                        SmartLog.d(Configure.TAG,"im connect");
                    }

                    @Override
                    public void onDisconnect(ECError error) {
                        SmartLog.d(Configure.TAG,"im disconnect");
                    }

                    @Override
                    public void onConnectState(ECDevice.ECConnectState state, ECError error) {

                    }
                });
                IMKitManager.setAutoReceiverOfflineMsg(true);//true 则会自动接收离线消息入库、false则不会
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    
}
