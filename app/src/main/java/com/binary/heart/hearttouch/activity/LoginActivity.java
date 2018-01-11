package com.binary.heart.hearttouch.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.MainActivity;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.application.HttApplication;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.im.ImHelper;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.LoginRsp;
import com.binary.heart.hearttouch.widget.LoadingView;
import com.binary.smartlib.container.SmartList;
import com.binary.smartlib.container.SmartMap;
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
 * Created by yaoguoju on 15-12-29.
 */
public class LoginActivity extends SmartActivity{

    private final static String TAG = "LoginActivity";

    private EditText editvAccount;
    private EditText editvPwd;
    private Button btnAccountDel;
    private Button btnPwdDel;

    private SmartCornerButton btnLoginOk;

    private Map<Integer,Integer> params = new HashMap<Integer,Integer>();

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
        title.setText(R.string.account_login);
        ImageButton back = (ImageButton)findViewById(R.id.btn_actionbar_back);
        back.setOnClickListener(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        initActionBar();
        btnAccountDel = (Button) findViewById(R.id.btn_loginAccount_delete);
        btnAccountDel.setOnClickListener(this);
        btnPwdDel     = (Button) findViewById(R.id.btn_loginpwd_delete);
        btnPwdDel.setOnClickListener(this);
        editvAccount = (EditText) findViewById(R.id.edit_login_account);
        editvAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    outParam(R.id.edit_login_account);
                    btnAccountDel.setVisibility(View.INVISIBLE);
                } else {
                    inParam(R.id.edit_login_account);
                    btnAccountDel.setVisibility(View.VISIBLE);
                }
            }
        });
        if(!TextUtils.isEmpty(AccountHelper.getAccount(context))) {
            editvAccount.setText(AccountHelper.getAccount(context));
        }
        editvPwd     = (EditText) findViewById(R.id.edit_login_pwd);
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
                    outParam(R.id.edit_login_pwd);
                    btnPwdDel.setVisibility(View.INVISIBLE);
                }else {
                    inParam(R.id.edit_login_pwd);
                    btnPwdDel.setVisibility(View.VISIBLE);
                }
            }
        });

        btnLoginOk = (SmartCornerButton) findViewById(R.id.btn_login_ok);
        btnLoginOk.setOnClickListener(this);
        btnLoginOk.setEnabled(false);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.btn_loginAccount_delete:
                editvAccount.setText("");
                break;
            case R.id.btn_loginpwd_delete:
                editvPwd.setText("");
                break;
            case R.id.btn_login_ok:
                actionLogin();
                break;
            case R.id.btn_actionbar_back:
                finish();
                break;
        }
    }

    private void inParam(int key) {
        params.put(key,1);
        if(params.size() >= 2) {
            btnLoginOk.setEnabled(true);
        }
    }

    private void outParam(int key) {
        params.remove(key);
        if(params.size() < 2) {
            btnLoginOk.setEnabled(false);
        }
    }
    private void actionLogin() {
        final LoadingView loadingView = (LoadingView) findViewById(R.id.v_loading);
        loadingView.setMsg(R.string.login_loading);
        final String account = editvAccount.getText().toString();
        String password = editvPwd.getText().toString();
        Account a = new Account();
        a.setAccount(account);
        a.setPassword(password);
        String body = JSON.toJSONString(a);
        ApacheHttp.post(WebUrls.LOGIN, body.getBytes(), null, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                SmartLog.d(Configure.TAG,"login rsp "+new String(response));
                LoginRsp rsp = JSON.parseObject(new String(response),LoginRsp.class);
                switch(rsp.getCode()) {
                    case LoginRsp.CODE_LOGINGOK:
                        SmartPref.put(context,PrefKeys.ACCOUNT,account);
                        SmartPref.put(context, PrefKeys.USERID,rsp.getUserid());
                        toActivity(context, MainActivity.class,null);
                        finish();
                        break;
                    case LoginRsp.CODE_LOGINERR:
                        loadingView.dismiss();
                        SmartToast.show(context,R.string.login_error,2000);
                        break;
                }
            }

            @Override
            public void onError(int code) {
                loadingView.dismiss();
                SmartToast.show(context,R.string.login_error,2000);
            }

            @Override
            public void onStart() {
                loadingView.show();
            }
        });
    }


}
