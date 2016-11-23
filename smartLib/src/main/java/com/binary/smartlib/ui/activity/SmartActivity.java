package com.binary.smartlib.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoguoju on 15-12-23.
 */
public abstract class SmartActivity extends Activity implements View.OnClickListener{

    protected Context context;
    private List<BroadcastReceiver> mReceivers;
    protected FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getApplicationContext();
        initView(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    /**
     * 初始化主界面和界面控件
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化界面数据
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 处理界面点击事件
     * @param v
     */
    protected abstract void onViewClick(View v) ;

    /**
     * 启动应用内Activty
     * @param context
     * @param toClass
     * @param bundle
     */
    protected void toActivity(Context context,Class<?> toClass,Bundle bundle) {
        if(context == null || toClass == null) {
            return ;
        }
        Intent intent = new Intent(context,toClass);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 启动应用外Activty
     * @param pkgname
     * @param classname
     * @param bundle
     */
    protected void toActivty(String pkgname,String classname,Bundle bundle) {
        if(TextUtils.isEmpty(pkgname) || TextUtils.isEmpty(classname)) {
            return ;
        }
        ComponentName componentName = new ComponentName(pkgname,classname);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 装载广播接受器
     * @param bc
     * @param filter
     */
    protected void setupBroadcast(BroadcastReceiver bc,IntentFilter filter) {
        if(mReceivers == null) {
            mReceivers = new ArrayList<BroadcastReceiver>();
        }

        if(!mReceivers.contains(bc)) {
            this.registerReceiver(bc,filter);
            mReceivers.add(bc);
        }
    }

    /**
     * 卸载广播
     * @param bc
     */
    protected void unSetupBroadcast(BroadcastReceiver bc) {
        if(mReceivers.contains(bc)) {
            mReceivers.remove(bc);
        }
        this.unregisterReceiver(bc);
    }

    /**
     * 获取系统服务
     * @param name
     * @return
     */
    protected Object getService(String name) {
        return getSystemService(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceivers != null) {
            for(BroadcastReceiver bc : mReceivers) {
                this.unregisterReceiver(bc);
            }
            mReceivers.clear();
        }
    }

    /**
     * 获取EditText控件的输入
     * @param edit
     * @return
     */
    protected String getEditTextInput(int edit) {
        EditText editText = (EditText) findViewById(edit);
        if(editText != null) {
            return editText.getText().toString();
        }else {
            return "";
        }
    }

    /**
     * 添加fragment
     * @param container
     * @param fragment
     */
    protected void assignFragment(int container,Fragment fragment) {
        try {
            if (fragmentTransaction == null) {
                fragmentTransaction = getFragmentManager().beginTransaction();
            }
            fragmentTransaction.replace(container, fragment);
            fragmentTransaction.commit();
            fragmentTransaction = null;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
