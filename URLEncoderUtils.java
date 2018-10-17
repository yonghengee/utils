package com.yqh.shop.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncoderUtils {
	
	
	public static String encode(String url){
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decode(String url){
		try {
			return URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String url = "http%3A%2F%2Fweb-bcd.51meets.com%2F%3FshopId%3D123456%23%2Fproduct_detail%3Fid%3Dc62957785da94c7a94f1b3ac0c695a87";
		System.out.println(URLEncoderUtils.decode(url));
		System.out.println(URLDecoder.decode(url));
	}
}
