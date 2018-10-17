package com.yqh.bbct.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class FilesUploadUtils {
	
	private static int TO_FORMAT = 0;
	private static int TO_MAX = 1;
	private static int TO_OK = 200;

	private static String disk;
	@Value("${resource.disk}")
	public static void setDisk(String disk) {
		FilesUploadUtils.disk = disk;
	}



	public static String uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        System.err.println(filePath);
	    String suffix =  fileName.substring(fileName.lastIndexOf("."));
		String uuID = UUID.randomUUID().toString().trim().replaceAll("-","");
		File targetFile = new File(filePath);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
		String fileAllName = filePath+uuID+suffix;
		FileOutputStream out = new FileOutputStream(fileAllName);
		out.write(file);
		out.flush();
		out.close();
		return uuID+suffix;
	}

}
