package com.binary.smartlib.net.thirdlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 * 
 * @author yaoguoju
 *
 */
public class ApacheHttpMultiBody {
	private List<Part> parts;
	
	private ApacheHttpMultiBody(List<Part> parts) {
		this.parts = parts;
	}
	
	/**
	 * 获取parts
	 * @return
	 */
	public List<Part> getParts() {
		return parts;
	}
	
	public static class Builder {
		private List<Part> parts = new ArrayList<Part>();
		
		public Builder addStringPart(String name,String value) {
			StringPart part = new StringPart(name, value);
			parts.add(part);
			return this;
		}
		
		public Builder addFilePart(String name,File file) throws FileNotFoundException {
			FilePart part;
		    part = new FilePart(name, file);
			parts.add(part);
			return this;
		}
		
		public ApacheHttpMultiBody build() {
			return new ApacheHttpMultiBody(parts);
		}
		
	}
}
