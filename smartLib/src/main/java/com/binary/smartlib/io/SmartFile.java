package com.binary.smartlib.io;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author yaoguoju
 *
 */
public class SmartFile {

	/**
	 * 清除指定文件或文件夹
	 * @param file
	 */
	public static void clear(File file) {
		if (file.exists()) { 
			if (file.isFile()) {
				file.delete(); 
			} else if (file.isDirectory()) { 
				File[] files = file.listFiles(); 
				for (File f : files) {
					clear(f); 
				}
			}
			file.delete();
		}
	}
	
	/**
	 * 创建文件或者目录
	 * @param file
	 * @throws IOException 
	 */
	public static void create(File file) throws IOException {
		if(!file.exists()) {
			if(file.isDirectory()) {
				file.mkdirs();
			}else {
				File parent = file.getParentFile();
				if(!file.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
			}
		}
	}
}
