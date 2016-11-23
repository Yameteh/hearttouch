package com.binary.smartlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
	
	/**
	 * 获取系统时间
	 */
	public static String getSystemTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
	
	public static String getSystemTimeTStyle() {
		SimpleDateFormat Formater = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		Calendar calendar = Calendar.getInstance();// 获取当前日历对象
		long unixTime = calendar.getTimeInMillis();// 获取当前时区下日期时间对应的时间戳
		long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();// 获取标准格林尼治时间下日期时间对应的时间戳
		Date curDate = new Date(unixTimeGMT);// 获取当前时间
		String str = Formater.format(curDate);
		return str;
	}

}
