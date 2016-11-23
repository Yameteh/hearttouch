package com.binary.smartlib.container;

import java.util.HashMap;
import java.util.Map;

/**
 * Map操作类
 * Created by yaoguoju on 15-12-24.
 */
public class SmartMap {
    /**
     * 创建Map数据
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> Map<K,V> newMap() {
        return new HashMap<K,V>();
    }

    /**
     * 向map中添加元素
     * @param map
     * @param k
     * @param v
     * @param <K>
     * @param <V>
     */
    public static <K,V> void add(Map<K,V> map,K k,V v) {
        synchronized (map) {
            map.put(k,v);
        }
    }

    /**
     * 删除map中元素
     * @param map
     * @param k
     * @param <K>
     * @param <V>
     */
    public static <K,V> void del(Map<K,V> map,K k) {
        synchronized (map) {
            map.remove(k);
        }
    }

    /**
     * 返回map的大小
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> int size(Map<K,V> map) {
        synchronized (map) {
            return map.size();
        }
    }

    /**
     * 判断map中是否存在k
     * @param map
     * @param k
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> boolean containsKey(Map<K,V> map,K k) {
        synchronized (map) {
            return map.containsKey(k);
        }
    }

    /**
     * 判断map中是否存在v
     * @param map
     * @param v
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> boolean containsValue(Map<K,V> map,V v) {
        synchronized (map) {
            return map.containsValue(v);
        }
    }
}
