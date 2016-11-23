package com.binary.smartlib.net.thirdlib;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;

/**
 * http请求头
 * @author yao.guoju
 *
 */
public class ApacheHttpHeaders {
	
	private List<Header> headers;
	
    private ApacheHttpHeaders(List<Header> headers) {
    	this.headers = headers;
    }
    
	public static class Builder {
		
		private List<Header> list = new ArrayList<Header>();
		
		public Builder add(String name,String value) {
			list.add(new Header(name, value));
			return this;
		}
		
		public ApacheHttpHeaders build() {
			return new ApacheHttpHeaders(list);
		}
	}
	
	public List<Header> getHeaders() {
		return headers;
	}
}
