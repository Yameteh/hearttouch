package com.binary.heart.hearttouch.fragment;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.LocRsp;
import com.binary.heart.hearttouch.jsons.UserRsp;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.ui.fragment.SmartFragment;
import com.binary.smartlib.utils.MathUtil;
import com.yuntongxun.kitsdk.utils.TextUtil;

/**
 * Created by yaoguoju on 16-4-18.
 */
public class MapFragment extends SmartFragment{

    public final static String SHOW_TYPE = "showtype";
    public final static String SHOW_USERID = "showuserid";

    public final static int TYPE_AT = 1;
    public final static int TYPE_TRACE = 2;
    private AMap aMap;
    private MapView mapView;

    private int userid ;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, null);
        mapView = (MapView) v.findViewById(R.id.mapv_msg);
        mapView.onCreate(saveInstanceState);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        return v;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null) {
            int showType = bundle.getInt(SHOW_TYPE);
            if(showType == TYPE_AT) {
                userid = bundle.getInt(SHOW_USERID);
                getToucherLocation(userid);
            }
        }

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    private void getToucherLocation(int userid) {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(context)).build();
        ApacheHttp.get(WebUrls.LOCAIOTN + "/" + userid, headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                LocRsp rsp = JSON.parseObject(new String(response), LocRsp.class);
                if (TextUtil.isEmpty(rsp.getLatitude()) || TextUtil.isEmpty(rsp.getLongitude())) {
                    return;
                }
                double latitude = MathUtil.s2d(rsp.getLatitude());
                double longitude = MathUtil.s2d(rsp.getLongitude());
                SmartLog.d(Configure.TAG, "marker toucher " + latitude + ":" + longitude);
                markerToucher(latitude, longitude);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {

            }
        });
    }

    private void markerToucher(double latitude,double longitude) {
        MarkerOptions toucher = new MarkerOptions();
        BitmapDescriptor marker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        toucher.icon(marker);
        LatLng p = new LatLng(latitude,longitude);
        SmartLog.d(Configure.TAG, "latitude = " + p.latitude + ",longitude = " + p.longitude);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(p, 15)));
        toucher.position(p);
        aMap.addMarker(toucher);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }
}
