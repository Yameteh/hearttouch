package com.binary.smartlib.io;

import com.binary.smartlib.log.SmartLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yaoguoju on 15-12-24.
 */
public class SmartStream {

    public final static String TAG = "SmartStream";
    /**
     * 从输出流中读取byte数据
     * @param input
     * @return
     */
    public static byte[] readInputStream(InputStream input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = input.read(buffer)) != -1) {
                outputStream.write(buffer,0,len);
            }
            input.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            input.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SmartLog.e(TAG,"read inputStream error");
        return null;
    }




}
