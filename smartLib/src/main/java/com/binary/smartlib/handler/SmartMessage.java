package com.binary.smartlib.handler;

import android.os.Message;


/**
 * Created by yaoguoju on 15-12-28.
 */
public abstract class SmartMessage {
    private final Class<?> mTag;
    private Message  mMsg;

    private int mWhat;
    private Object mObj;
    public SmartMessage(Class<?> cls,int what,Object object) {
        mTag = cls;
        mWhat = what;
        mObj  = object;

    }

    /**
     * 获取消息
     * @return
     */
    public Message getMsg() {
        return mMsg;
    }

    /**
     * 获取what数值
     * @return
     */
    public int getWhat() {
        return mWhat;
    }

    /**
     * 获取消息传递对象
     * @return
     */
    public Object getObject() {
        return mObj;
    }

    /**
     * 获取类名标签
     * @return
     */
    public Class<?> getClassTag() {
        return mTag;
    }

    protected void create() {
        mMsg = Message.obtain();
        mMsg.what = mWhat;
        mMsg.obj  = mObj;
    }

    /**
     * 比较函数
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        SmartMessage msg = (SmartMessage)o;
        return mTag.getName().equals(msg.getClassTag().getName())
                 && mMsg.what == msg.getMsg().what;
    }

    /**
     * hashCode函数
     * @return
     */
    @Override
    public int hashCode() {
        String hashString = mTag.getName()+mMsg.what;
        return hashString.hashCode();
    }

    /**
     * toString函数
     * @return
     */
    @Override
    public String toString() {
        return mTag.getName()+"："+mMsg.toString();
    }

    /**
     * 消息处理函数
     * @param cls
     * @param msg
     * @return
     */
    public abstract boolean handleMessage(Class<?> cls,Message msg);

}
