package com.binary.heart.hearttouch.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.binary.heart.hearttouch.MainActivity;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.im.notify.HeartNotify;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.LocReq;
import com.binary.heart.hearttouch.jsons.LocRsp;
import com.binary.heart.hearttouch.jsons.ProfileRsp;
import com.binary.heart.hearttouch.jsons.UnbindReq;
import com.binary.heart.hearttouch.jsons.UserRsp;
import com.binary.heart.hearttouch.map.LocationHelper;
import com.binary.heart.hearttouch.widget.BindSelectDialog;
import com.binary.heart.hearttouch.widget.LoadingView;
import com.binary.smartlib.io.SmartDb;
import com.binary.smartlib.io.SmartGraphic;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.SmartImageLoader;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.net.thirdlib.ApacheHttpUrlParams;
import com.binary.smartlib.ui.fragment.SmartFragment;
import com.binary.smartlib.utils.MathUtil;
import com.yuntongxun.kitsdk.ECDeviceKit;
import com.yuntongxun.kitsdk.fragment.ConversationListFragment;
import com.yuntongxun.kitsdk.utils.TextUtil;

import java.io.File;
import java.util.HashMap;

/**
 * Created by yaoguoju on 16-3-30.
 */
public class MsgFragment extends SmartFragment{
    private TextView tvAccout;
    private ImageView head;
    private SmartImageLoader smartImageLoader;

    private MapFragment mapFragment;

    private LoadingView loadingView;
    private RelativeLayout msgNav;
    private FrameLayout  subMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmartLog.d("YYY", "discover oncreate");
        AccountHelper.checkProfile(context);
    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_msg,null);
        msgNav = (RelativeLayout) v.findViewById(R.id.rl_msgnav);
        subMsg = (FrameLayout)v.findViewById(R.id.fl_msgcontainer);
        tvAccout = (TextView) v.findViewById(R.id.tv_msg_accout);
        tvAccout.setOnClickListener(this);
        v.findViewById(R.id.btn_msg_new).setOnClickListener(this);
        v.findViewById(R.id.btn_msg_map).setOnClickListener(this);
        v.findViewById(R.id.btn_msg_at).setOnClickListener(this);
        v.findViewById(R.id.btn_msg_game).setOnClickListener(this);
        v.findViewById(R.id.btn_lock).setOnClickListener(this);
        head = (ImageView) v.findViewById(R.id.imgv_msg_head);
        loadingView = (LoadingView) v.findViewById(R.id.msg_loading);
        return v;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //findOne();
        //getBindProfile();
        int bindid = AccountHelper.getBindUserId(context);
        if(bindid > 0) {
            getBindProfile();
        }else {
            msgNav.setVisibility(View.GONE);
            //subMsg.setVisibility(View.GONE);
            actionMsgNew();
        }
    }


    private ProfileRsp bindProfile;

    private void getBindProfile(){
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(context)).build();
        ApacheHttpUrlParams params = new ApacheHttpUrlParams.Builder().add("user",AccountHelper.getBindUserId(context)).build();
        ApacheHttp.get(WebUrls.PROFILE, headers, params, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                bindProfile = JSON.parseObject(new String(response),ProfileRsp.class);
                AccountHelper.setBindAccount(context, bindProfile.getAccount());
                AccountHelper.setBindNick(context,bindProfile.getNick());
                refreshUI();
                loadingView.dismiss();
                actionMsgNew();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {
                loadingView.setMsg(R.string.loading_getbindprofile);
                loadingView.show();
            }
        });

    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_msg_new:
                 actionMsgNew();
                break;
            case R.id.btn_msg_map:
                break;
            case R.id.btn_msg_at:
                actionMsgAt();
                break;
            case R.id.btn_msg_game:
                actionMsgGame();
                break;
            case R.id.btn_lock:
                showLock();
                break;
        }
    }

    private void showLock() {
        BindSelectDialog dialog = new BindSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putString(BindSelectDialog.ME,AccountHelper.getAccount(context));
        bundle.putString(BindSelectDialog.TO,AccountHelper.getBindAccount(context));
        dialog.setArguments(bundle);
        dialog.setOnSelectListener(new BindSelectDialog.OnSelectListener() {
            @Override
            public void onSelected(boolean bind) {
                if(!bind) {
                    actionUnBind(AccountHelper.getBindAccount(context));
                }
            }
        });
        dialog.show(getFragmentManager(),"lock");
    }


    private void actionUnBind(final String to) {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(getActivity())).build();
        UnbindReq req = new UnbindReq();
        req.setUserid(AccountHelper.getUserId(getActivity()));
        req.setUnbindAccount(to);
        String body = JSON.toJSONString(req);
        ApacheHttp.post(WebUrls.UNBIND, body.getBytes(), headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                SmartDb.getDb(context).getSmartDao(HeartNotify.class).delete("id > 0 and sender = '" + to + "'");
                AccountHelper.setBindUserId(context, 0);
                MainActivity activity = (MainActivity) getActivity();
                activity.reloadMsgFragment();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {

            }
        });
    }

    private void actionMsgGame() {
        VsFragment fragment = new VsFragment();
        asignFragment(R.id.fl_msgcontainer,fragment);
    }

    private void actionMsgNew() {
        SubMsgFragment fragment = new SubMsgFragment();
        asignFragment(R.id.fl_msgcontainer, fragment);
    }

    private void actionMsgAt() {
        mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MapFragment.SHOW_TYPE,MapFragment.TYPE_AT);
        bundle.putInt(MapFragment.SHOW_USERID,AccountHelper.getBindUserId(context));
        mapFragment.setArguments(bundle);
        asignFragment(R.id.fl_msgcontainer, mapFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if(smartImageLoader != null) {
            smartImageLoader.recycleLoader();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    private void refreshUI() {
        String displayName = AccountHelper.getBindAccount(context);
        if(!TextUtil.isEmpty(bindProfile.getNick())) {
            displayName = bindProfile.getNick();
            AccountHelper.setBindNick(context,displayName);
        }
        tvAccout.setText(displayName);
        refeshPhoto();

    }

    private void refeshPhoto() {
        if (smartImageLoader == null) {
            smartImageLoader = new SmartImageLoader.Builder(context).setMemRate(0.5f).setCacheEnable(true).build();
        }
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("authHeader", AccountHelper.getAuthValue(context));
        smartImageLoader.fillImage(WebUrls.PHOTO + "?user=" + AccountHelper.getBindUserId(context), headers, head, new SmartImageLoader.ImageLoadCallback() {

            @Override
            public void onLoaderStart() {

            }

            @Override
            public void onLoaderSuccess(Bitmap bm) {
                File bindHead = new File(context.getFilesDir(), "bindhead.png");
                SmartGraphic.newFileByBitmap(bm, bindHead);

            }

            @Override
            public void onLoaderError(int code) {

            }
        });
    }




}
