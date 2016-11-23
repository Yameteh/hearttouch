package com.binary.smartlib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by yaoguoju on 15-12-25.
 */
public class AppUtil {

    /**
     * 获取安装app的信息
     * @param context
     * @param pkgName
     * @return
     */
    public static PackageInfo getPackageInfo(Context context,String pkgName) {
        PackageInfo pi = null;
        PackageManager pm = context.getPackageManager();
        if(pm != null) {
            try {
                pi = pm.getPackageInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pi;
    }

    /**
     * 获取App版本名
     * @param context
     * @param pkgName
     * @return
     */
    public static String getAppVersionName(Context context,String pkgName) {
        String versionName = "";
        PackageInfo packageInfo = getPackageInfo(context, pkgName);
        if(packageInfo != null) {
            versionName = packageInfo.versionName;
        }
        return versionName;
    }

    /**
     * 获取App版本号
     * @param context
     * @param pkgName
     * @return
     */
    public static int getAppVersionCode(Context context,String pkgName) {
        int versionCode = -1;
        PackageInfo packageInfo = getPackageInfo(context,pkgName);
        if(packageInfo != null) {
            versionCode = packageInfo.versionCode;
        }
        return versionCode;
    }

    /**
     * 获取应用名称
     * @param context
     * @param pkgName
     * @return
     */
    public static String getAppName(Context context,String pkgName) {
        String name = "";
        PackageInfo packageInfo = getPackageInfo(context,pkgName);
        if(packageInfo != null) {
            ApplicationInfo ai = packageInfo.applicationInfo;
            if(ai != null) {
               name = String.valueOf(ai.loadLabel(context.getPackageManager()));
            }
        }
        return name;
    }

    /**
     * 获取App图标
     * @param context
     * @param pkgName
     * @return
     */
    public static Drawable getAppIcon(Context context,String pkgName) {
        Drawable icon = null;
        PackageInfo packageInfo = getPackageInfo(context,pkgName);
        if(packageInfo != null) {
            ApplicationInfo ai = packageInfo.applicationInfo;
            if(ai != null) {
                icon = ai.loadIcon(context.getPackageManager());
            }
        }
        return icon;
    }
}
