package com.binary.smartlib.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by yaoguoju on 15-12-24.
 */
public class SmartPref {

    public static final String PREF_NAME = "smart_pref";

    /**
     * pref存储基本数据类型
     * @param context
     * @param key
     * @param value
     */
    @SuppressLint("NewApi")
	public static void put(Context context,String key,Object value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(value instanceof Boolean) {
            editor.putBoolean(key,(Boolean)value);
        }else if(value instanceof String) {
            editor.putString(key,(String)value);
        }else if(value instanceof Float) {
            editor.putFloat(key,(Float)value);
        }else if(value instanceof Integer) {
            editor.putInt(key,(Integer)value);
        }else if(value instanceof Long) {
            editor.putLong(key,(Long)value);
        }else if(value instanceof Double) {
        	String d = String.valueOf(value);
        	editor.putString(key, d);
        }
        editor.apply();
    }

    /**
     * 获取对应key的键值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(Context context,String key,Object defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Double) {
        	return Double.valueOf(sp.getString(key, String.valueOf(defaultValue)));
        }
        return null;
    }

    /**
     * 移除对应key
     * @param context
     * @param key
     */
    @SuppressLint("NewApi")
	public static void removeKey(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    /**
     * 判断键值是否存在
     * @param context
     * @param key
     * @return
     */
    public static boolean containsKey(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 清除所有数据
     * @param context
     */
    @SuppressLint("NewApi")
	public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
