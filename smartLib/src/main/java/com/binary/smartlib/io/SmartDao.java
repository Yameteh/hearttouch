package com.binary.smartlib.io;

import android.content.ContentValues;
import android.database.Cursor;

import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.rft.SmartRft;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.binary.smartlib.rft.SmartRft.newInstance;

/**
 * Created by yaoguoju on 16-5-6.
 */
public class SmartDao {
    private final static String TAG ="SmartDao";
    private HashMap<String,Class<?>> coloums = new HashMap<String,Class<?>>();
    private SmartDb database;
    private String table;
    protected SmartDao(SmartDb db,String table) {
        database = db;
        this.table = table;
    }

    /**
     * 添加列表
     * @param coloum
     */
    protected void addColoum(String coloum,Class<?> type) {
        if(coloums != null) {
            coloums.put(coloum,type);
        }
    }

    /**
     * 插入数据
     * @param object
     *
     * @throws Exception
     */
    public void insert(Object object) throws Exception {
        ContentValues values = new ContentValues();
        if(coloums != null && coloums.size() > 0) {
            Iterator<Map.Entry<String, Class<?>>> interator =  coloums.entrySet().iterator();
            while(interator.hasNext()) {
                Map.Entry<String,Class<?>> entry = interator.next();
                Object obj = SmartRft.getField(object, entry.getKey());
                SmartLog.d(TAG,"filed:"+entry.getKey()+",value:"+obj);
                putValue(values, entry.getKey(),obj);
            }
        }
        database.insert(table,null,values);
    }

    /**
     * 添加键值对
     * @param values
     * @param key
     * @param obj
     */
    private void putValue(ContentValues values,String key,Object obj) {
        if(obj instanceof String) {
            values.put(key,(String)obj);
        }else if(obj instanceof Integer) {
            SmartLog.d(TAG,"insert interger "+obj);
            values.put(key,(Integer)obj);
        }else if(obj instanceof Boolean) {
            values.put(key,(Boolean)obj);
        }else if(obj instanceof Long) {
            values.put(key,(Long)obj);
        }
    }

    public List<Object> queryAll(Class<?> cls) throws Exception {
        Cursor c  = database.query(table,null,null,null,null,null,null);
        List<Object> objects = new ArrayList<Object>();
        if(c != null ) {
            while (c.moveToNext()) {
                Object obj = SmartRft.newInstance(cls.getName(), null);
                if(coloums != null && coloums.size() > 0) {
                    Iterator<Map.Entry<String, Class<?>>> interator =  coloums.entrySet().iterator();
                    while(interator.hasNext()) {
                        Map.Entry<String,Class<?>> entry = interator.next();
                        int index = c.getColumnIndex(entry.getKey());
                        SmartLog.d(TAG,"get type = "+entry.getValue()+",get key = "+entry.getKey());
                        if(entry.getValue() == String.class) {
                            SmartRft.setField(obj, entry.getKey(), c.getString(index));
                        }else if(entry.getValue() == int.class){
                            SmartRft.setField(obj,entry.getKey(),c.getInt(index));
                        }
                    }
                }
                objects.add(obj);
            }
        }
        return objects;
    }

    public void deleteAll() {
        database.delete(table,"id >= 0",null);
    }

    public void delete(String where) {
        database.delete(table,where,null);
    }

    public void query() {

    }
}

