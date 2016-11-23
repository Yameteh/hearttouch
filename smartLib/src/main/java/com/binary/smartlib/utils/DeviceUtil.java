package com.binary.smartlib.utils;

import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.rft.SmartRft;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 
 * @author yaoguoju
 *
 */
public class DeviceUtil {
	
	private final static String TAG = "DeviceUtil";
	/**
	 * 获取设备唯一标识符
	 * @param context
	 * @return
	 */
	public static String getDeviceIMEI(Context context,boolean hasTwoCard) {
		String imei = "";
		if(hasTwoCard) {
			imei = getDeviceIdRft(context,0)+getDeviceIdRft(context, 1);
		}else {
			imei = getDeviceIdRft(context,0);
		}
		return imei;
	}
	
	/**
	 * 通过反射获取设备ID
	 * @param context
	 * @param subid
	 * @return
	 */
	public static String getDeviceIdRft(Context context,int subid) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			return (String)SmartRft.invokeMethod(tm, "getDeviceId", subid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SmartLog.e(TAG,"getDeviceId rft error!");
			e.printStackTrace();
		}
		return tm.getDeviceId();
	}
	
	/**
	 * 获取Sim卡号
	 * @param context
	 * @param hasTwoCard
	 * @return
	 */
	public static String getDeviceSimNumber(Context context,boolean hasTwoCard) {
		String sim = "";
		if(hasTwoCard) {
			return getSimSerialNumberRft(context,0) +getSimSerialNumberRft(context, 1);
		}else {
			return getSimSerialNumberRft(context, 0);
		}
	}
	
	/**
	 * 通过反射获取设备sim卡
	 * @param context
	 * @param subid
	 * @return
	 */
    public static String getSimSerialNumberRft(Context context,int subid) {
    	TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	try {
			return (String)SmartRft.invokeMethod(tm, "getSimSerialNumber", subid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SmartLog.e(TAG,"getSimSerialNumber rft error!");
			e.printStackTrace();
		}
    	return tm.getSimSerialNumber();
    }
}
