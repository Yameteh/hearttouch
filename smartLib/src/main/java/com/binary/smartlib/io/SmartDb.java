package com.binary.smartlib.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.binary.smartlib.log.SmartLog;

import java.lang.reflect.Field;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yaoguoju on 16-5-6.
 */
public class SmartDb {
    private final static String TAG = "SmartDb";
    private Context context;
    private SQLiteDatabase db;
    private static volatile SmartDb instance;
    private static final String DBNAME = "smart.db";

    private ConcurrentHashMap<String,SmartDao> daos = new ConcurrentHashMap<String,SmartDao>();

    private SmartDb(Context context,String name){
        this.context = context.getApplicationContext();
        db = this.context.openOrCreateDatabase(name,Context.MODE_PRIVATE,null);
    }

    public static SmartDb getDb(Context context) {
        if(instance == null) {
            synchronized (SmartDb.class) {
                if(instance == null) {
                    instance = new SmartDb(context, DBNAME);
                }
            }
        }
        return instance;
    }

    /**
     * 注册dao
     * @param dao
     */
    public void registerDao(Class<?> dao) {
        Field[] fields = dao.getDeclaredFields();
        if(fields != null && fields.length > 0) {
            SmartDao sd = new SmartDao(this,dao.getSimpleName());
            StringBuilder sql = new StringBuilder();
            sql.append("create table if not exists " + dao.getSimpleName() + " (id integer primary key autoincrement,");
            for(Field f : fields) {
                sql.append(f.getName()+" "+getType(f)+",");
                sd.addColoum(f.getName(),f.getType());
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
            SmartLog.d(TAG, "register dao sql:" +dao.getSimpleName());
            db.execSQL(sql.toString());
            daos.put(dao.getSimpleName(),sd);
        }else {
            SmartLog.e(TAG, "register dao error:" + dao.getName());
        }
    }

    /**
     * 获取dao
     * @param dao
     * @return
     */
    public SmartDao getSmartDao(Class<?> dao) {
        if(daos != null) {
            return daos.get(dao.getSimpleName());
        }
        return null;
    }

    /**
     * 获取数据库对应类型
     * @param filed
     * @return
     */
    private String getType(Field filed) {
        if(filed.getType() == String.class || filed.getType() == boolean.class || filed.getType() == Boolean.class) {
            return "text";
        }else {
            return "integer";
        }
    }

    /**
     * 插入数据库
     * @param table
     * @param nullColumnHack
     * @param values
     */
    public void insert(String table, String nullColumnHack, ContentValues values){
        db.insertOrThrow(table,nullColumnHack,values);
    }


    /**
     * 查询数据库
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        if(db != null) {
            return db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        }
        return null;
    }

    /**
     * 删除数据库
     * @param table
     * @param whereClause
     * @param whereArgs
     */
    public void delete(String table, String whereClause, String[] whereArgs) {
        if(db != null) {
            db.delete(table,whereClause,whereArgs);
        }
    }
}
