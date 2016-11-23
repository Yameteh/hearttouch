package com.binary.smartlib.net;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.widget.ImageView;

import com.binary.smartlib.io.SmartGraphic;
import com.binary.smartlib.log.SmartLog;

import java.util.HashMap;

/**
 * Created by yaoguoju on 15-12-28.
 */
@SuppressLint("NewApi")
public class SmartImageLoader {
    private static final String TAG = "SmartImageLoader";
    private LruCache<String,Bitmap> mCache;

    private int cacheSize = 0;
    private boolean cacheEnable = false;
    private Context context;

    private SmartImageLoader(Context context,boolean cacheEnable,float rate) {
        this.context = context;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am != null) {
            SmartLog.d(TAG,"mem class"+am.getMemoryClass()+",rate = "+rate);
            cacheSize = (int) (1024 * 1024 * am.getMemoryClass()*rate);
        }
        this.cacheEnable = cacheEnable;
        SmartLog.i(TAG,"smart image loader cache size "+cacheSize);
    }

    /**
     * 加载图片
     * @param url
     * @param imageview
     */
    public void loadImage(final String url, final ImageView imageview, final ImageLoadCallback callback) {
        if(callback != null) {
            callback.onLoaderStart();
        }
        if(cacheEnable) {
            Bitmap bm = getImageCache(url);
            if(bm != null) {
                SmartLog.d(TAG,"get cache by url "+url);
                if(imageview != null) {
                    imageview.setImageBitmap(bm);
                    if(callback != null) {
                        callback.onLoaderSuccess(bm);
                    }
                    return ;
                }

            }
        }
        SmartHttp.get(url, null, null, new SmartHttp.RequestCallback() {
            @Override
            public void onRequestStart() {
            }

            @Override
            public void onRequestSucess(byte[] data) {
                Bitmap bm = SmartGraphic.getBitmapFromBytes(data);
                if (cacheEnable && bm != null) {
                    addImageCache(url, bm);
                    if (callback != null) {
                        callback.onLoaderSuccess(bm);
                    }
                }
                if (imageview != null) {
                    imageview.setImageBitmap(bm);
                }
            }

            @Override
            public void onRequestError(int errorCode) {
                if (callback != null) {
                    callback.onLoaderError(errorCode);
                }
            }
        });
    }


    /**
     * 加载图片
     * @param url
     * @param imageview
     */
    public void setImage(final String url, HashMap<String,String> header,final ImageView imageview, final ImageLoadCallback callback) {
        if(callback != null) {
            callback.onLoaderStart();
        }
        if(cacheEnable) {
            Bitmap bm = getImageCache(url);
            if(bm != null) {
                SmartLog.d(TAG,"get cache by url "+url);
                if(imageview != null) {
                    imageview.setImageBitmap(bm);
                    if(callback != null) {
                        callback.onLoaderSuccess(bm);
                    }
                    return ;
                }

            }
        }
        SmartHttp.get(url, null, header, new SmartHttp.RequestCallback() {
            @Override
            public void onRequestStart() {
            }

            @Override
            public void onRequestSucess(byte[] data) {
                Bitmap bm = SmartGraphic.getBitmapFromBytes(data);
                if(bm == null || bm.getByteCount() == 0) {
                    if(callback != null) {
                        callback.onLoaderSuccess(null);
                    }
                    return ;
                }
                if(cacheEnable && bm != null) {
                    addImageCache(url,bm);
                    if(callback != null) {
                        callback.onLoaderSuccess(bm);
                    }
                }
                if(imageview != null) {
                    Drawable drawable =new BitmapDrawable(bm);
                    imageview.setImageDrawable(drawable);
                }
            }

            @Override
            public void onRequestError(int errorCode) {
                if(callback != null) {
                    callback.onLoaderError(errorCode);
                }
            }
        });
    }

    /**
     * 加载图片
     * @param url
     * @param imageview
     */
    public void fillImage(final String url, HashMap<String,String> header,final ImageView imageview, final ImageLoadCallback callback) {
        if(callback != null) {
            callback.onLoaderStart();
        }
        if(cacheEnable) {
            Bitmap bm = getImageCache(url);
            if(bm != null) {
                SmartLog.d(TAG,"get cache by url "+url);
                if(imageview != null) {
                    imageview.setImageBitmap(bm);
                    if(callback != null) {
                        callback.onLoaderSuccess(bm);
                    }
                    return ;
                }

            }
        }
        SmartHttp.get(url, null, header, new SmartHttp.RequestCallback() {
            @Override
            public void onRequestStart() {
            }

            @Override
            public void onRequestSucess(byte[] data) {
                Bitmap bm = SmartGraphic.getBitmapFromBytes(data);
                if(bm == null || bm.getByteCount() == 0) {
                    if(callback != null) {
                        callback.onLoaderSuccess(null);
                    }
                    return ;
                }
                if(cacheEnable && bm != null) {
                    addImageCache(url,bm);
                    if(callback != null) {
                        callback.onLoaderSuccess(bm);
                    }
                }
                if(imageview != null) {
                    Drawable drawable =new BitmapDrawable(bm);
                    imageview.setBackground(drawable);
                }
            }

            @Override
            public void onRequestError(int errorCode) {
                if(callback != null) {
                    callback.onLoaderError(errorCode);
                }
            }
        });
    }
    /**
     * 向缓存中添加图片
     * @param url
     * @param bitmap
     */
    public void addImageCache(String url,Bitmap bitmap) {
        if(mCache == null ) {
            mCache = new LruCache<String,Bitmap>(cacheSize) {

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    if(oldValue != null && oldValue.isRecycled()) {
                        oldValue.recycle();
                        oldValue = null;
                    }
                }

                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };
        }
        if(mCache.get(url) == null) {
            SmartLog.d(TAG,"add cache by url "+url);
            mCache.put(url, bitmap);
        }
    }

    /**
     * 获取Cache数据
     * @param url
     * @return
     */
    public Bitmap getImageCache(String url ) {
        if(mCache != null) {
            return mCache.get(url);
        }else {
            SmartLog.d(TAG, "no cache by url " + url);
            return null;
        }
    }


    /**
     * 移除缓存中数据
     * @param url
     * @return
     */
    public Bitmap removeImageCache(String url) {
        if(mCache != null) {
            return mCache.remove(url);
        }else {
            return null;
        }
    }

    /**
     * 回收loader 清除图片缓存
     */
    public void recycleLoader() {
        if(mCache != null) {
            mCache.evictAll();
        }
        mCache = null;
    }

    /**
     * 创建loader的Builder类
     */
    public static class Builder {

        private float memrate ;
        private boolean enableCache;
        private Context context;
        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        /**
         * 是否需要缓存
         * @param enable
         * @return
         */
        public Builder setCacheEnable(boolean enable) {
            enableCache = enable;
            return this;
        }

        /**
         * 设置缓存大小相对于应用可使用内存的比率
         * @param rate
         * @return
         */
        public Builder setMemRate(float rate) {
            memrate = rate;
            return this;
        }

        /**
         * 创建方法
         * @return
         */
        public SmartImageLoader build() {
            return new SmartImageLoader(context,enableCache,memrate);
        }
    }

    /**
     * 图片加载回调
     */
    public interface ImageLoadCallback {
        public void onLoaderStart();
        public void onLoaderSuccess(Bitmap bm) ;
        public void onLoaderError(int code);
    }

}
