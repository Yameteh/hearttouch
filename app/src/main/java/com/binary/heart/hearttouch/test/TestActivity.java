package com.binary.heart.hearttouch.test;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

import com.binary.heart.hearttouch.R;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.receiver.SmartReceiver;
import com.binary.smartlib.ui.activity.SmartActivity;

import java.util.List;

/**
 * Created by yaoguoju on 15-12-30.
 */
public class TestActivity extends SmartActivity{

    private final static String TAG = "YYY";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifilist = wm.getConfiguredNetworks();
        if(wifilist != null) {
            for(WifiConfiguration conf : wifilist) {
                SmartLog.d(TAG,"wifi configuration "+conf.SSID+" netid "+conf.networkId);
            }
        }
        SmartReceiver receiver = new SmartReceiver(this,new String[] {WifiManager.WIFI_STATE_CHANGED_ACTION
        ,WifiManager.SUPPLICANT_STATE_CHANGED_ACTION}){
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                SmartLog.d(TAG,"wifi action"+action);

            }
        };
    }

    @Override
    protected void onViewClick(View v) {

    }
}
