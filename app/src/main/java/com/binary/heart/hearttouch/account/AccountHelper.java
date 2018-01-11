package com.binary.heart.hearttouch.account;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.ProfileRsp;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.net.thirdlib.ApacheHttpUrlParams;

/**
 * Created by yaoguoju on 16-3-30.
 */
public class AccountHelper {

    public static boolean isLogined(Context context) {
        String accout = getAccount(context);
        int userid = getUserId(context);
        if(!TextUtils.isEmpty(accout) && userid != -1) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean isRegisted(Context context) {
        String account = getAccount(context);
        if(TextUtils.isEmpty(account)){
            return false;
        }else {
            return true;
        }
    }

    public static String getAccount(Context context) {
        return (String) SmartPref.get(context, PrefKeys.ACCOUNT,"");
    }

    public static int getUserId(Context context) {
        return (int) SmartPref.get(context,PrefKeys.USERID,-1);
    }


    public static int getBindUserId(Context context) {
        return (int) SmartPref.get(context,PrefKeys.BINDUSERID,-1);
    }

    public static void setBindUserId(Context context,int bindid) {
        SmartPref.put(context,PrefKeys.BINDUSERID,bindid);
    }

    public static void setBindAccount(Context context,String account) {
        SmartPref.put(context,PrefKeys.BINDACCOUNT,account);
    }

    public static void setBindNick(Context context,String nick) {
        SmartPref.put(context,PrefKeys.BINDNICK,nick);
    }

    public static String getBindNick(Context context) {
        return (String) SmartPref.get(context,PrefKeys.BINDNICK,"");
    }
    public static String getBindAccount(Context context) {
        return (String) SmartPref.get(context,PrefKeys.BINDACCOUNT,"");
    }

    public static String getAuthValue(Context context) {
        String value = getUserId(context) +":"+getAccount(context);
        String v = Base64.encodeToString(value.getBytes(),Base64.DEFAULT);
        //会有一个换行，去掉
        return v.substring(0,v.length()-1);
    }


    public static void setNick(Context context, String nick) {
        SmartPref.put(context,PrefKeys.NICK,nick);
    }

    public static String getNick(Context context) {
        return (String) SmartPref.get(context,PrefKeys.NICK,"");
    }

    public static void setSex(Context context,String sex) {
        SmartPref.put(context,PrefKeys.SEX,sex);
    }

    public static String getSex(Context context) {
        return (String) SmartPref.get(context,PrefKeys.SEX,"");
    }

    public static void setLocale(Context context,String locale) {
        SmartPref.put(context,PrefKeys.LOCALE,locale);
    }

    public static String getLocale(Context context) {
        return (String) SmartPref.get(context,PrefKeys.LOCALE,"");
    }

    public static void setSign(Context context,String sign) {
        SmartPref.put(context,PrefKeys.SIGN,sign);
    }

    public static String getSign(Context context) {
        return (String)SmartPref.get(context,PrefKeys.SIGN,"");
    }

    public static void  login() {

    }



    public static void  changeHead() {

    }

    public static void checkProfile(final Context context) {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(context)).build();
        ApacheHttpUrlParams params = new ApacheHttpUrlParams.Builder().add("user",AccountHelper.getUserId(context)).build();
        ApacheHttp.get(WebUrls.PROFILE, headers, params, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                ProfileRsp rsp = JSON.parseObject(new String(response),ProfileRsp.class);
                if(rsp.getCode() == 0) {
                    AccountHelper.setNick(context, rsp.getNick());
                    AccountHelper.setSign(context,rsp.getSignature());
                    AccountHelper.setSex(context,rsp.getSex());
                    AccountHelper.setBindUserId(context,rsp.getCurbind());
                }
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {

            }
        });
    }
}
