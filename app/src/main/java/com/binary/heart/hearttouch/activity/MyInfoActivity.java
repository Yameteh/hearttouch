package com.binary.heart.hearttouch.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.application.HttApplication;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.ProfileReq;
import com.binary.heart.hearttouch.jsons.ProfileRsp;
import com.binary.heart.hearttouch.map.LocationHelper;
import com.binary.heart.hearttouch.widget.InputDialog;
import com.binary.heart.hearttouch.widget.SexSelectDialog;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.ui.activity.SmartActivity;
import com.binary.smartlib.ui.toast.SmartToast;

/**
 * Created by yaoguoju on 16-4-16.
 */
public class MyInfoActivity extends SmartActivity{

    private TextView nick;
    private TextView sex;
    private TextView location;
    private EditText sign;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_myinfo);
        initActionBar();
        nick = (TextView) findViewById(R.id.tv_myinfo_nick);
        sex  = (TextView) findViewById(R.id.tv_myinfo_sex);
        location = (TextView)findViewById(R.id.tv_myinfo_loc);
        sign = (EditText) findViewById(R.id.edit_myinfo_sign);
        findViewById(R.id.ll_nick).setOnClickListener(this);
        findViewById(R.id.ll_sex).setOnClickListener(this);

        location.setText(AccountHelper.getLocale(context));
        if("male".equals(AccountHelper.getSex(context))) {
            sex.setText(R.string.info_male);
        }else if("female".equals(AccountHelper.getSex(context))) {
            sex.setText(R.string.info_female);
        }
        nick.setText(AccountHelper.getNick(context));
        sign.setText(AccountHelper.getSign(context));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    private void uploadMyInfo() {
        ProfileReq req = new ProfileReq();
        req.setUserid(AccountHelper.getUserId(context));
        req.setNick(nick.getText().toString());
        if(getResources().getString(R.string.info_male).equals(sex.getText().toString())){
            req.setSex("male");
        }else if(getResources().getString(R.string.info_female).equals(sex.getText().toString())){
            req.setSex("female");
        }
        req.setSignature(sign.getText().toString());
        String body = JSON.toJSONString(req);
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add(WebUrls.AUTH_KEY, AccountHelper.getAuthValue(context)).build();
        ApacheHttp.post(WebUrls.PROFILE, body.getBytes(), headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                ProfileRsp rsp = JSON.parseObject(new String(response),ProfileRsp.class);
                if(rsp.getCode() == 0) {
                    finish();
                }else {
                    SmartToast.show(context,R.string.toast_myinfo_uploaderr,2000);
                }
            }

            @Override
            public void onError(int code) {
                SmartToast.show(context,R.string.toast_myinfo_uploaderr,2000);
            }

            @Override
            public void onStart() {

            }
        });


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
            params.setMargins(0, HttApplication.getStatusBarHeight(),0,0);
        }
        actionbar.setLayoutParams(params);

        TextView title = (TextView) findViewById(R.id.tv_actionbar_title);
        title.setText(R.string.title_profile);
        ImageButton back = (ImageButton)findViewById(R.id.btn_actionbar_back);
        back.setOnClickListener(this);
        ImageButton ok = (ImageButton) findViewById(R.id.btn_actionbar_ok);
        ok.setVisibility(View.VISIBLE);
        ok.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_actionbar_ok:
                uploadMyInfo();
                break;
            case R.id.btn_actionbar_back:
                finish();
                break;
            case R.id.ll_nick:
                showNickDialog();
                break;
            case R.id.ll_sex:
                showSexSelectDialog();
                break;
        }
    }

    private void showNickDialog() {
        InputDialog dialog = new InputDialog();
        dialog.setOnInputListener(new InputDialog.OnInputListener() {
            @Override
            public void onConfirm(String input) {
                nick.setText(input);
            }

            @Override
            public void onCancel() {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(InputDialog.INPUT,AccountHelper.getNick(context));
        bundle.putString(InputDialog.TITLE,getString(R.string.title_myinfo_nick));
        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "input");
    }

    private void showSexSelectDialog() {
        SexSelectDialog dialog = new SexSelectDialog();
        dialog.setOnSelectListener(new SexSelectDialog.OnSelectListener() {
            @Override
            public void onSexSelect(int id) {
                if(id == SexSelectDialog.MALE) {
                    sex.setText(R.string.info_male);
                }else if(id == SexSelectDialog.FEMALE){
                    sex.setText(R.string.info_female);
                }
            }
        });
        Bundle bundle = new Bundle();
        if("male".equals(AccountHelper.getSex(context))) {
            bundle.putInt(SexSelectDialog.SEX,R.id.rbtn_male);
        }else if("female".equals(AccountHelper.getSex(context))) {
            bundle.putInt(SexSelectDialog.SEX, R.id.rbtn_female);
        }
        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "sex");
    }
}
