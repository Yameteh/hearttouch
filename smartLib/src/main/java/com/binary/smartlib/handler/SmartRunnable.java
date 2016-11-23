package com.binary.smartlib.handler;

/**
 * Created by yaoguoju on 15-12-28.
 */
public abstract class SmartRunnable {

    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            if(callback!=null) {
                callback.onRunStart();
            }
            process();
            if(callback!=null) {
                callback.onRunStop();
            }
        }
    };

    private final RunCallBack callback;

    /**
     * 传入带有运行回调的构造方法，可以为null
     * @param cb
     */
    public SmartRunnable(RunCallBack cb) {
        this.callback = cb;
    }

    /**
     * 获取真实Runnable
     * @return
     */
    public Runnable getRunable() {
        return r;
    }

    /**
     * 用户实现的run方法
     */
    public abstract void process() ;

    /**
     * runnable运行回调接口
     */
    public interface RunCallBack {
        public void onRunStart();
        public void onRunStop();
    }
}
