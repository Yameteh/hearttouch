package com.beetle.bauhinia.tools;


import android.media.MediaRecorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by houxh on 14-12-3.
 */
public class FileCache {

    public static int DEFAULT_BUFFER_SIZE = 2048;
    public static FileCache instance = new FileCache();
    public static FileCache getInstance() {
        return instance;
    }

    private File dir;
    public void setDir(File dir) {
        this.dir = dir;
    }

    public void storeFile(String key, InputStream inputStream) throws IOException {
        File file = new File(this.dir, getFileName(key));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        copy(inputStream, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    public int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public void storeByteArray(String key, ByteArrayOutputStream byteStream) throws IOException {
        File file = new File(this.dir, getFileName(key));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byteStream.writeTo(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void removeFile(String key) {
        File file = new File(this.dir, getFileName(key));
        file.delete();
    }

    public String getCachedFilePath(String key) {
        File file = new File(this.dir, getFileName(key));
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public boolean isCached(String key) {
        return getCachedFilePath(key) != null;
    }

    private String getFileName(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            byte[] m = md5.digest();
            return BinAscii.bin2Hex(m);
        } catch (NoSuchAlgorithmException e) {
            //opps
            System.exit(1);
            return "";
        }
    }
}
