package com.binary.smartlib.container;

import java.util.ArrayList;
import java.util.List;

/**
 * list操作类
 * Created by yaoguoju on 15-12-23.
 */
public class SmartList {

    /**
     * 创建list
     * @param <T>
     * @return
     */
    public static <T> List<T> newList() {
        return new ArrayList<T>();
    }

    /**
     * 向list里添加数据
     * @param list
     * @param t
     * @param <T>
     */
    public static <T> void add(List<T> list,T t) {
        synchronized (list) {
            if(!list.contains(t)) {
                list.add(t);
            }
        }
    }

    /**
     * 向list里删除数据
     * @param list
     * @param t
     * @param <T>
     */
    public static <T> void del(List<T> list,T t) {
        synchronized (list) {
            if(list.contains(t)) {
                list.remove(t);
            }
        }
    }

    /**
     * 获取i的元素
     * @param list
     * @param i
     * @param <T>
     * @return
     */
    public static <T> T get(List<T> list,int i) {
        synchronized (list) {
            return list.get(i);
        }
    }
    /**
     * 计算list的大小
     * @param list
     * @param <T>
     * @return
     */
    public static <T> int size(List<T> list) {
        synchronized (list) {
            return list.size();
        }
    }

    /**
     * 判断元素是否存在list中
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean contains(List<T> list,T t) {
        synchronized (list) {
            return list.contains(t);
        }
    }


    /**
     * 清除list数据
     * @param list
     * @param <T>
     */
    public static <T> void clear(List<T> list) {
        synchronized (list) {
            list.clear();
        }
    }
}


