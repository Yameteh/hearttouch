package com.binary.heart.hearttouch;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.binary.heart.hearttouch.activity.LoginActivity;
import com.binary.heart.hearttouch.activity.WelcomeActivity;
import com.binary.heart.hearttouch.application.HttApplication;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.fragment.AdDialogFragment;
import com.binary.heart.hearttouch.fragment.DiscoverFragment;
import com.binary.heart.hearttouch.fragment.MsgFragment;
import com.binary.heart.hearttouch.fragment.ProfileFragment;
import com.binary.heart.hearttouch.im.ImHelper;
import com.binary.heart.hearttouch.map.LocationHelper;
import com.binary.smartlib.container.SmartList;
import com.binary.smartlib.handler.SmartHandler;
import com.binary.smartlib.io.SmartGraphic;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.SmartHttp;
import com.binary.smartlib.net.SmartImageLoader;
import com.binary.smartlib.receiver.SmartReceiver;
import com.binary.smartlib.ui.activity.SmartActivity;
import com.binary.smartlib.ui.toast.SmartToast;
import com.binary.smartlib.ui.widget.SmartCornerImageView;
import com.binary.smartlib.ui.widget.SmartIndicator;
import com.binary.smartlib.utils.AppUtil;
import com.binary.smartlib.utils.SdCardUtil;

import java.io.File;
import java.util.List;

/**
 * Created by yaoguoju on 15-12-23.
 */
public class MainActivity extends SmartActivity{

    private ProfileFragment  profileFragment;
    private DiscoverFragment discoverFragment;
    private MsgFragment      msgFragment;

    private RadioGroup       radioGroup;
    private TextView         tvTitle;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        initActionBar();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rbtn_profile);
        if(profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        tvTitle.setText(R.string.tab_profile);
        showAdDialog();
        assignFragment(R.id.fl_container, profileFragment);
    }


    private void showAdDialog() {
         AdDialogFragment mAdDialog = new AdDialogFragment();
         mAdDialog.show(getFragmentManager(), "Ad");

    }

    public void reloadMsgFragment() {
        msgFragment = new MsgFragment();
        tvTitle.setText(R.string.tab_msg);
        assignFragment(R.id.fl_container, msgFragment);
    }
    /**
     * 初始化actionBar
     */
    private void initActionBar() {
        View actionbar = findViewById(R.id.rl_actionbar);
        int actionbar_height = getResources().getDimensionPixelSize(R.dimen.height_actionbar);
        int actionbar_width  = ViewGroup.LayoutParams.MATCH_PARENT;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actionbar_width,actionbar_height);
        //沉浸ActionBar
        if(Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            params.setMargins(0,HttApplication.getStatusBarHeight(),0,0);
        }
        actionbar.setLayoutParams(params);
        tvTitle = (TextView) findViewById(R.id.tv_actionbar_title);
        ImageButton  back = (ImageButton)findViewById(R.id.btn_actionbar_back);
        back.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SmartHandler.get(this).post(new Runnable() {
            @Override
            public void run() {
                ImHelper.initIm(context);
            }
        });
    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.btn_actionbar_back:
                finish();
                break;
        }
    }

    /**
     * 处理radiobtn点击事件
     * @param v
     */
    public void onRadioButtonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rbtn_discover:
                if(discoverFragment == null) {
                    discoverFragment = new DiscoverFragment();
                }
                tvTitle.setText(R.string.tab_discover);
                assignFragment(R.id.fl_container,discoverFragment);
                break;
            case R.id.rbtn_msg:
                if(msgFragment == null) {
                    msgFragment = new MsgFragment();
                }
                tvTitle.setText(R.string.tab_msg);
                assignFragment(R.id.fl_container,msgFragment);
                break;
            case R.id.rbtn_profile:
                if(profileFragment == null) {
                    profileFragment = new ProfileFragment();
                }
                tvTitle.setText(R.string.tab_profile);
                assignFragment(R.id.fl_container,profileFragment);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(profileFragment != null) {
            profileFragment.onActivityResult(requestCode,resultCode,data);
        }

    }
}
