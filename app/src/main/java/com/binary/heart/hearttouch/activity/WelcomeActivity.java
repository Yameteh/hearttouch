package com.binary.heart.hearttouch.activity;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binary.heart.hearttouch.MainActivity;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.application.HttApplication;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.smartlib.container.SmartList;
import com.binary.smartlib.handler.SmartHandler;
import com.binary.smartlib.handler.SmartMessage;
import com.binary.smartlib.handler.SmartRunnable;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.SmartImageLoader;
import com.binary.smartlib.ui.activity.SmartActivity;
import com.binary.smartlib.ui.toast.SmartToast;

/**
 * Created by yaoguoju on 15-12-28.
 */
public class WelcomeActivity extends SmartActivity{

    private ImageView mImg;
    private LinearLayout mSelect;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        //沉浸ActionBar
        if(Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mSelect = (LinearLayout) findViewById(R.id.welcome_select);
        mSelect.setVisibility(View.GONE);
        findViewById(R.id.btn_wel_login).setOnClickListener(this);
        findViewById(R.id.btn_wel_reg).setOnClickListener(this);

    }

    private void showSelectView() {
        mSelect.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(mSelect,"alpha",0f,1.0f).setDuration(2000).start();

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SmartHandler.get(context).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!AccountHelper.isRegisted(context)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showSelectView();
                        }
                    });
                } else if (!AccountHelper.isLogined(context)) {
                    toActivity(context, LoginActivity.class, null);
                    finish();
                } else {
                    toActivity(context, MainActivity.class, null);
                    finish();
                }

            }
        },2000);
    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_wel_login:
                toActivity(context,LoginActivity.class,null);
                finish();
                break;
            case R.id.btn_wel_reg:
                toActivity(context,RegisterActivity.class,null);
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mloader.recycleLoader();
    }


}

