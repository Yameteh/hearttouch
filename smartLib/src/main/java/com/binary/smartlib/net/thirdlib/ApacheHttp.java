package com.binary.smartlib.net.thirdlib;

import android.os.AsyncTask;

import java.io.IOException;

import java.util.List;


import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.binary.smartlib.log.SmartLog;



/**
 * 
 * @author yao.guoju
 *
 */
public class ApacheHttp {
	
	private final static String TAG = "ApacheHttp";
	
	/**
	 * http回调
	 * @author yao.guoju
	 *
	 */
	public interface Callback {
		void onSuccess(byte[] response,int code);
		void onError(int code);
		void onStart();
	}

	/**
	 * Http　Get请求
	 * @param url
	 * @param headers
	 * @param params
	 * @param callback
	 */
	public static void get(final String url,final ApacheHttpHeaders headers,final ApacheHttpUrlParams params,final Callback callback) {

		new AsyncTask<String, Integer, byte[]>() {
			int responseCode = HttpStatus.SC_GONE;

			@Override
			protected void onPreExecute() {
				if(callback != null) {
					callback.onStart();
				}
			}

			@Override
			protected void onPostExecute(byte[] bytes) {
				if(responseCode == HttpStatus.SC_OK) {
					if(callback != null) {
						callback.onSuccess(bytes,responseCode);
					}
				}else {
					callback.onError(responseCode);
				}
			}

			@Override
			protected byte[] doInBackground(String... p) {
				String assembleUrl = url;
				if(params != null) {
					assembleUrl = params.assembleUrl(url);
				}
				HttpClient client = new HttpClient() ;
				GetMethod method = new GetMethod(assembleUrl);

				if(headers != null) {
					List<Header> hs = headers.getHeaders();
					if(hs != null) {
						for(Header h : hs) {
							method.setRequestHeader(h);
						}
					}
				}

				try {
					responseCode = client.executeMethod(method);
					if(responseCode == HttpStatus.SC_OK) {
						return method.getResponseBody();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					method.releaseConnection();
				}

				return null;
			}

		}.execute(url);
	}
	
	/**
	 * Http Post请求
	 * @param url
	 * @param body
	 * @param headers
	 * @param params
	 * @param callback
	 */
	public static void post(final String url,final byte[] body,final ApacheHttpHeaders headers,final ApacheHttpUrlParams params,final Callback callback) {

		new AsyncTask<String, Integer, byte[]>() {
			int responseCode = HttpStatus.SC_GONE;
			@Override
			protected void onPreExecute() {
				if(callback != null) {
					callback.onStart();
				}
			}

			@Override
			protected void onPostExecute(byte[] bytes) {
				if(responseCode == HttpStatus.SC_OK) {
					if(callback != null) {
						callback.onSuccess(bytes,responseCode);
					}
				}else {
					if(callback != null) {
						callback.onError(responseCode);
					}
				}
			}

			@Override
			protected byte[] doInBackground(String... p) {
				String assembleUrl = url;
				if(params != null) {
					assembleUrl = params.assembleUrl(url);
				}

				HttpClient client = new HttpClient() ;
				PostMethod method = new PostMethod(assembleUrl);
				method.getParams().setContentCharset("utf-8");
				if(headers != null) {
					List<Header> hs = headers.getHeaders();
					if(hs != null) {
						for(Header h : hs) {
							method.setRequestHeader(h);
						}
					}
				}
				if(body != null) {
					ByteArrayRequestEntity entity = new ByteArrayRequestEntity(body);
					method.setRequestEntity(entity);
				}
				try {
					responseCode = client.executeMethod(method);
					if(responseCode == HttpStatus.SC_OK) {
						return method.getResponseBody();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					method.releaseConnection();
				}
				return null;
			}
		}.execute(url);
	    
	}
	
	
	/**
	 * Http Put请求
	 * @param url
	 * @param body
	 * @param headers
	 * @param params
	 * @param callback
	 */
	public static void put(final String url,final byte[] body,final ApacheHttpHeaders headers,final ApacheHttpUrlParams params,final Callback callback) {
		new AsyncTask<String,Integer,byte[]>(){
			int responseCode = HttpStatus.SC_GONE;
			@Override
			protected void onPreExecute() {
				if(callback != null) {
					callback.onStart();
				}
			}

			@Override
			protected void onPostExecute(byte[] bytes) {
				if(responseCode == HttpStatus.SC_OK) {
					if(callback != null) {
						callback.onSuccess(bytes,responseCode);
					}
				}else {
					if(callback != null) {
						callback.onError(responseCode);
					}
				}
			}

			@Override
			protected byte[] doInBackground(String... p) {
				String putUrl = url;
				if(params != null) {
					putUrl = params.assembleUrl(url);
				}

				HttpClient client = new HttpClient() ;
				PutMethod method = new PutMethod(putUrl);
				method.getParams().setContentCharset("utf-8");

				if(headers != null) {
					List<Header> hs = headers.getHeaders();
					if(hs != null) {
						for(Header h : hs) {
							method.setRequestHeader(h);
						}
					}
				}

				if(body != null) {
					ByteArrayRequestEntity entity = new ByteArrayRequestEntity(body);
					method.setRequestEntity(entity);
				}
				try {
					responseCode = client.executeMethod(method);
					if(responseCode == HttpStatus.SC_OK) {
						return method.getResponseBody();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					if(callback != null) {
						callback.onError(responseCode);
					}
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if(callback != null) {
						callback.onError(responseCode);
					}
					e.printStackTrace();
				}finally {
					method.releaseConnection();
				}

				return null;
			}
		}.execute(url);


	}
	
	/**
	 * Put multi内容
	 * @param url
	 * @param body
	 * @param headers
	 * @param params
	 * @param callback
	 */
	public static void putMultiBody(final String url,final ApacheHttpMultiBody body,
			final ApacheHttpHeaders headers,final ApacheHttpUrlParams params,final Callback callback) {
		new AsyncTask<String,Integer,byte[]>(){
			int responseCode = HttpStatus.SC_GONE;
			@Override
			protected void onPreExecute() {
				if(callback != null) {
					callback.onStart();
				}
			}

			@Override
			protected void onPostExecute(byte[] bytes) {
				if(responseCode == HttpStatus.SC_OK) {
					if(callback != null) {
						callback.onSuccess(bytes,responseCode);
					}
				}else {
					if(callback != null) {
						callback.onError(responseCode);
					}
				}
			}

			@Override
			protected byte[] doInBackground(String... p) {
				String assembleUrl = url;
				if(params != null) {
					assembleUrl = params.assembleUrl(url);
				}

				HttpClient client = new HttpClient() ;
				PutMethod method = new PutMethod(assembleUrl);
				method.getParams().setContentCharset("utf-8");
				if(headers != null) {
					List<Header> hs = headers.getHeaders();
					if(hs != null) {
						for(Header h : hs) {
							method.setRequestHeader(h);
						}
					}
				}

				if(body != null && body.getParts() != null && body.getParts().size() > 0) {
					Part[] ps = new Part[body.getParts().size()];
					body.getParts().toArray(ps);
					MultipartRequestEntity entity = new MultipartRequestEntity(ps, method.getParams());
					method.setRequestEntity(entity);
				}
				try {
					responseCode = client.executeMethod(method);
					if(responseCode == HttpStatus.SC_OK) {
						return method.getResponseBody();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					method.releaseConnection();
				}
				return null;
			}
		}.execute(url);


	}
	
	/**
	 * Post multi内容
	 * @param url
	 * @param body
	 * @param headers
	 * @param params
	 * @param callback
	 */
	public static void postMultiBody(final String url,final ApacheHttpMultiBody body,
			final ApacheHttpHeaders headers,final ApacheHttpUrlParams params,final Callback callback) {
		new AsyncTask<String,Integer,byte[]>(){
			int responseCode = HttpStatus.SC_GONE;
			@Override
			protected void onPreExecute() {
				if(callback != null) {
					callback.onStart();
				}
			}

			@Override
			protected void onPostExecute(byte[] bytes) {
				if(responseCode == HttpStatus.SC_OK) {
					if(callback != null) {
						callback.onSuccess(bytes,responseCode);
					}
				}else {
					if(callback != null) {
						callback.onError(responseCode);
					}
				}
			}

			@Override
			protected byte[] doInBackground(String... p) {
				String assembleUrl = url;
				if(params != null) {
					assembleUrl = params.assembleUrl(url);
				}

				HttpClient client = new HttpClient() ;
				PostMethod method = new PostMethod(assembleUrl);
				method.getParams().setContentCharset("utf-8");
				if(headers != null) {
					List<Header> hs = headers.getHeaders();
					if(hs != null) {
						for(Header h : hs) {
							method.setRequestHeader(h);
						}
					}
				}

				if(body != null && body.getParts() != null && body.getParts().size() > 0) {
					Part[] ps = new Part[body.getParts().size()];
					body.getParts().toArray(ps);
					MultipartRequestEntity entity = new MultipartRequestEntity(ps, method.getParams());
					method.setRequestEntity(entity);
				}
				try {
					responseCode = client.executeMethod(method);
					if(responseCode == HttpStatus.SC_OK) {
						return method.getResponseBody();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					method.releaseConnection();
				}
				return null;
			}
		}.execute(url);
	}
}
