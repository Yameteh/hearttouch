package com.beetle.bauhinia.tools;

import android.content.Context;

import com.beetle.bauhinia.api.IMHttpAPI;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yaoguoju on 18-1-17.
 */

public class   PicassoHelper {

    static Picasso picasso;

    public static Picasso get(Context context) {
        if(picasso == null) {
            synchronized (PicassoHelper.class) {
                if(picasso == null) {
                    create(context);
                }
            }
        }
        return picasso;
    }




    private static void create(Context context) {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader(IMHttpAPI.AUTH_KEY, IMHttpAPI.getAuthValue())
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();
        picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(client)).build();
    }



}
