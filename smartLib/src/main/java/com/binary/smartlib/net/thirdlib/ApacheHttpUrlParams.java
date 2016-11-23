package com.binary.smartlib.net.thirdlib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yao.guoju
 *
 */
public class ApacheHttpUrlParams {
	
	private Map<String,Object> params ;
	
	private ApacheHttpUrlParams(Map<String,Object> params) {
		this.params = params;
	}
	
	public static class Builder {
		
		private Map<String,Object> maps = new HashMap<String,Object>();
		
		public Builder add(String name,Object value) {
			maps.put(name, value);
			return this;
		}
		
	    public ApacheHttpUrlParams build() {
	    	return new ApacheHttpUrlParams(maps);
	    }
		
	}
	
	/**
	 * 拼装url
	 * @param url
	 * @return
	 */
	public String assembleUrl(String url) {
		StringBuilder builder = new StringBuilder(url);
		if(params != null && !params.isEmpty()) {
			builder.append("?");
			
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				try {
					builder.append(entry.getKey().toString())
							.append('=').append(URLEncoder.encode(entry.getValue()
									.toString(), "utf-8")).append('&');
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			builder.deleteCharAt(builder.length()-1);
		}
		return builder.toString();
	}
}
