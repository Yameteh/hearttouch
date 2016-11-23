package com.binary.smartlib.net;

import android.media.Image;
import android.os.AsyncTask;

import com.binary.smartlib.io.SmartStream;
import com.binary.smartlib.log.SmartLog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * http请求处理类
 * Created by yaoguoju on 15-12-24.
 */
public class SmartHttp {

    private static final String TAG = "SmartHttp";
    public static final int connectTimeOut = 10*1000;
    public static final int HTTP_CODE_200  = 200;

    /**
     * 发送http的Get
     * @param url
     * @param getParams
     * @param headers
     * @param callback
     */
    public static void get(final String url, final Map<String,String> getParams, final Map<String,String> headers
                              , final RequestCallback callback) {

        new AsyncTask<String, Integer, byte[]>() {
            private int responseCode = -1;
            private HttpURLConnection conn = null;
            @Override
            protected void onPreExecute() {
                if(callback != null) {
                    callback.onRequestStart();
                }
            }

            @Override
            protected byte[] doInBackground(String... params) {
                try {
                    Set<Map.Entry<String,String>> entrys = null;
                    StringBuffer urlbuff = new StringBuffer(url);
                    if(getParams != null && !getParams.isEmpty()) {
                        urlbuff.append("?");
                        entrys = getParams.entrySet();
                        for (Map.Entry<String, String> entry : entrys) {
                            urlbuff.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                                    .append("&");
                        }
                        urlbuff.deleteCharAt(urlbuff.length() - 1);
                    }
                    URL getUrl = new URL(urlbuff.toString());
                    SmartLog.d(TAG,"get url "+getUrl.toString());
                    conn = (HttpURLConnection) getUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(connectTimeOut);
                    if(headers != null && !headers.isEmpty()) {
                        entrys = headers.entrySet();
                        for(Map.Entry<String,String> entry : entrys) {
                            conn.setRequestProperty(entry.getKey(),entry.getValue());
                        }
                    }

                    responseCode = conn.getResponseCode();
                    if(responseCode == HTTP_CODE_200) {
                        byte[] responseData = SmartStream.readInputStream(conn.getInputStream());
                        return responseData;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SmartLog.e(TAG,"http get error");
                return null;
            }

            @Override
            protected void onPostExecute(byte[] data) {
                if(responseCode == HTTP_CODE_200 && data != null) {
                    if(callback != null) {
                        callback.onRequestSucess(data);
                    }
                }else {
                    if(callback != null) {
                        callback.onRequestError(responseCode);
                    }
                }
                conn.disconnect();
            }

        }.execute(url);

    }


    /**
     * 发送http的post
     * @param url
     * @param postParams
     * @param headers
     * @param callback
     */
    public static void post(final String url,final byte[] body, final Map<String,String> postParams, final Map<String,String> headers
                                    , final RequestCallback callback) {
            new AsyncTask<String, Integer, byte[]>() {
                private int responseCode = -1;
                private HttpURLConnection conn;
                private OutputStream outputStream;
                @Override
                protected void onPreExecute() {
                    if(callback != null) {
                        callback.onRequestStart();
                    }
                }

                @Override
                protected byte[] doInBackground(String... params) {
                    Set<Map.Entry<String,String>> entrys = null;
                    StringBuffer urlbuff = new StringBuffer(url);
                    try {
                        if(postParams != null && !postParams.isEmpty()) {
                            entrys = postParams.entrySet();
                            for (Map.Entry<String, String> entry : entrys) {
                            	urlbuff.append(entry.getKey()).append("=")
                                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                                        .append("&");
                            }
                            urlbuff.deleteCharAt(urlbuff.length() - 1);
                        }
                        URL postUrl = new URL(urlbuff.toString());
                        SmartLog.d(TAG,"post url "+postUrl.toString());
                        conn = (HttpURLConnection) postUrl.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setConnectTimeout(50000);
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setUseCaches(false);
                        if(headers != null && !headers.isEmpty()) {
                            entrys = headers.entrySet();
                            for(Map.Entry<String,String> entry : entrys) {
                                conn.setRequestProperty(entry.getKey(),entry.getValue());
                            }
                        }

                        outputStream = conn.getOutputStream();
                        outputStream.write(body);
                        responseCode = conn.getResponseCode();
                        if(responseCode == HTTP_CODE_200) {
                            byte[] data = SmartStream.readInputStream(conn.getInputStream());
                            return data;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(byte[] data) {
                    if(responseCode == HTTP_CODE_200 && data != null) {
                        if(callback != null) {
                            callback.onRequestSucess(data);
                        }
                    }else {
                        if(callback != null) {
                            callback.onRequestError(responseCode);
                        }
                    }
                    try {
                        outputStream.flush();
                        outputStream.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }.execute(url);
    }

    /**
     * 上传文件
     * @param url
     * @param params
     * @param file
     */
    public static void uploadFile(final String url,Map<String,String> params, final File file
                                     , final RequestCallback callback) {
        final String BOUNDARY = UUID.randomUUID().toString();
        final String PERFIX = "--";
        final String LINEND = "\r\n";

        final String CHARSET = "UTF-8";
        new AsyncTask<String, Integer, byte[]>() {

            HttpURLConnection conn = null;
            int responseCode = -1;
            DataOutputStream dos=null;
            FileInputStream fis=null;
            @Override
            protected void onPreExecute() {
                if(callback != null) {
                    callback.onRequestStart();
                }
            }

            @Override
            protected byte[] doInBackground(String... params) {
                try {

                    URL postUrl = new URL(url);
                    conn = (HttpURLConnection) postUrl.openConnection();
                    conn.setReadTimeout(5 * 1000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Charset", CHARSET);
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(PERFIX+BOUNDARY+LINEND);
                    dos.writeBytes("Content-Disposition: form-data;"
                            + "name=\"file1\";filename=\"" + file.getName() + "\"" + LINEND);
                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while((len = fis.read(buffer)) != -1) {
                        dos.write(buffer,0,len);
                    }
                    dos.writeBytes(LINEND);
                    dos.writeBytes(PERFIX + BOUNDARY + PERFIX + LINEND);

                    if(conn.getResponseCode() == HTTP_CODE_200) {
                        byte[] data = SmartStream.readInputStream(conn.getInputStream());
                        return data;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(byte[] data) {
                if(responseCode == HTTP_CODE_200 && data != null) {
                    if(callback != null) {
                        callback.onRequestSucess(data);
                    }
                }else {
                    if(callback != null) {
                        callback.onRequestError(responseCode);
                    }
                }
                try {
                    dos.flush();
                    dos.close();
                    fis.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }


    /**
     * 请求回调接口
     */
    public interface RequestCallback {
        public void onRequestStart();
        public void onRequestSucess(byte[] data);
        public void onRequestError(int errorCode);
    }

    
    

}
