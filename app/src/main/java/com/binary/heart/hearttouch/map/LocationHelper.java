package com.binary.heart.hearttouch.map;

import android.content.Context;

import com.alibaba.fastjson.JSON;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.LocReq;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.utils.MathUtil;

import java.util.Arrays;

/**
 * Created by yaoguoju on 16-4-10.
 */
public class LocationHelper {

    private static volatile LocationHelper mInstance;

    private Context context;
    //private AMapLocationClient mLocationClient;

    private double latitude;
    private double longitude;

    private long   lastTime = 0;
    private long   delay  = 60*1000;

    private String country;
    private String province;
    private String city;

    public long interval = 10*60*1000;

    public String getLocale() {
        return country+" "+province+city;
    }

//    private AMapLocationListener listener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            latitude = aMapLocation.getLatitude();
//            longitude = aMapLocation.getLongitude();
//            province = aMapLocation.getProvince();
//            city = aMapLocation.getCity();
//            country = aMapLocation.getCountry();
//            AccountHelper.setLocale(context,getLocale());
//            uploadLocation();
//        }
//    };
//
//    private void uploadLocation() {
//        if(System.currentTimeMillis() - lastTime > delay ) {
//            SmartLog.d(Configure.TAG, "uploadLocation ");
//            lastTime = System.currentTimeMillis();
//            LocReq req = new LocReq();
//            req.setLatitude(MathUtil.d2s(latitude));
//            req.setLongitude(MathUtil.d2s(longitude));
//            req.setUserid(AccountHelper.getUserId(context));
//            req.setTime(String.valueOf(System.currentTimeMillis()));
//            String json = JSON.toJSONString(req);
//            SmartLog.d(Configure.TAG, "location info:" + json);
//            ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(context)).build();
//            ApacheHttp.post(WebUrls.LOCAIOTN, json.getBytes(), headers, null, new ApacheHttp.Callback() {
//                @Override
//                public void onSuccess(byte[] response, int code) {
//
//                }
//
//                @Override
//                public void onError(int code) {
//
//                }
//
//                @Override
//                public void onStart() {
//
//                }
//            });
//        }
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//
//    private LocationHelper(Context context) {
//        this.context = context.getApplicationContext();
//        init();
//    }
//
//    /**
//     * 初始化的定位设置
//     */
//    private void init() {
//        mLocationClient = new AMapLocationClient(this.context);
//        mLocationClient.setLocationListener(listener);
//        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(interval);
//            //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//    }
//
//    public static LocationHelper get(Context context) {
//        if(mInstance == null) {
//            synchronized (LocationHelper.class) {
//                if(mInstance == null) {
//                    mInstance = new LocationHelper(context);
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    public void start() {
//        if(mLocationClient != null) {
//            mLocationClient.startLocation();
//        }
//    }
//
//    public void stop(){
//        if(mLocationClient != null) {
//            mLocationClient.stopLocation();
//        }
//    }
//
//    public void destory() {
//        if(mLocationClient != null) {
//            mLocationClient.onDestroy();
//        }
//    }
}
