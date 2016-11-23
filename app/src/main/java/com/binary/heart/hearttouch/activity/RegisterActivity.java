package com.binary.heart.hearttouch.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.application.HttApplication;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.RegRsp;
import com.binary.heart.hearttouch.widget.LoadingView;
import com.binary.heart.hearttouch.widget.ProgressDialog;
import com.binary.heart.hearttouch.widget.heartlayout.AbstractPathAnimator;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.SmartHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.ui.activity.SmartActivity;
import com.binary.smartlib.ui.toast.SmartToast;
import com.binary.smartlib.ui.widget.SmartCornerButton;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaoguoju on 15-12-30.
 */
public class RegisterActivity extends SmartActivity {

    private EditText editvAccout;
    private EditText editvPwd;
    private EditText editvConfirmPwd;

    private Button btnAccountDel;
    private Button btnPwdDel;
    private Button btnConfirmPwdDel;

    private SmartCornerButton btnRegOk;

    private Map<Integer,Integer> params = new HashMap<Integer,Integer>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        initActionBar();
        editvAccout = (EditText) findViewById(R.id.edit_reg_account);
        editvAccout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    outParam(R.id.edit_reg_account);
                    btnAccountDel.setVisibility(View.INVISIBLE);
                } else {
                    inParam(R.id.edit_reg_account);
                    btnAccountDel.setVisibility(View.VISIBLE);
                }
            }
        });

        editvPwd    = (EditText) findViewById(R.id.edit_reg_pwd);
        editvPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())) {
                    outParam(R.id.edit_reg_pwd);
                    btnPwdDel.setVisibility(View.INVISIBLE);
                }else {
                    inParam(R.id.edit_reg_pwd);
                    btnPwdDel.setVisibility(View.VISIBLE);
                }
            }
        });

        editvConfirmPwd = (EditText) findViewById(R.id.edit_reg_confirmpwd);
        editvConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())) {
                    outParam(R.id.edit_reg_confirmpwd);
                    btnConfirmPwdDel.setVisibility(View.INVISIBLE);
                }else {
                    inParam(R.id.edit_reg_confirmpwd);
                    btnConfirmPwdDel.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAccountDel = (Button) findViewById(R.id.btn_regAccount_delete);
        btnAccountDel.setOnClickListener(this);
        btnPwdDel     = (Button) findViewById(R.id.btn_regpwd_delete);
        btnPwdDel.setOnClickListener(this);
        btnConfirmPwdDel = (Button) findViewById(R.id.btn_regconfirmpwd_delete);
        btnConfirmPwdDel.setOnClickListener(this);

        btnRegOk = (SmartCornerButton)findViewById(R.id.btn_reg_ok);
        btnRegOk.setOnClickListener(this);
        btnRegOk.setEnabled(false);
    }

    private void inParam(int key) {
        params.put(key,1);
        if(params.size() >= 3) {
            btnRegOk.setEnabled(true);
        }
    }

    private void outParam(int key) {
        params.remove(key);
        if(params.size() < 3) {
            btnRegOk.setEnabled(false);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_actionbar_back:
                finish();
                break;
            case R.id.btn_regAccount_delete:
                editvAccout.setText("");
                break;
            case R.id.btn_regpwd_delete:
                editvPwd.setText("");
                break;
            case R.id.btn_regconfirmpwd_delete:
                editvConfirmPwd.setText("");
                break;
            case R.id.btn_reg_ok:
                actionRegister();
                break;
        }
    }


    private void actionRegister() {

        String account = editvAccout.getText().toString();
        String pwd     = editvPwd.getText().toString();
        String confirmPwd = editvConfirmPwd.getText().toString();
        if(!pwd.equals(confirmPwd)) {
            SmartToast.show(this,R.string.toast_reg_pwderr,2000);
            return ;
        }
        regist(account,pwd);
    }


    private void regist(final String account,String password) {
        final LoadingView loadingView = (LoadingView) findViewById(R.id.v_loading);
        loadingView.setMsg("注册中");
        Account data = new Account();
        data.setAccount(account);
        data.setPassword(password);
        String body = JSON.toJSONString(data);
        Log.d(Configure.TAG,"url "+WebUrls.REGISTER);
        ApacheHttp.post(WebUrls.REGISTER, body.getBytes(), null, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                SmartLog.d(Configure.TAG,new String(response));
                RegRsp rsp = JSON.parseObject(new String(response),RegRsp.class);
                switch (rsp.getCode()) {
                    case RegRsp.CODE_ISREGED:
                        SmartToast.show(context,"该用户已注册",2000);
                        SmartPref.put(context, PrefKeys.ACCOUNT, account);
                        toActivity(getApplicationContext(), LoginActivity.class, null);
                        finish();
                        break;
                    case RegRsp.CODE_REGERR:
                        loadingView.dismiss();
                        SmartToast.show(context,"注册失败",2000);
                        break;
                    case RegRsp.CODE_REGOK:
                        SmartToast.show(context,"注册成功",2000);
                        SmartPref.put(context, PrefKeys.ACCOUNT, account);
                        toActivity(context, LoginActivity.class, null);
                        finish();
                        break;
                }
            }

            @Override
            public void onError(int code) {
                SmartLog.d(Configure.TAG,"rsp code "+code);
                loadingView.dismiss();
                SmartToast.show(context,"注册失败",2000);
            }

            @Override
            public void onStart() {
                loadingView.show();
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
        title.setText(R.string.account_register);
        ImageButton back = (ImageButton)findViewById(R.id.btn_actionbar_back);
        back.setOnClickListener(this);
    }


}
