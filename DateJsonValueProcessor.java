package com.yqh.shop.utils;


import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonValueProcessor implements JsonValueProcessor {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	private DateFormat dateFormat;

	public DateJsonValueProcessor(String datePattern) {

		if (null == datePattern)
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		else
			dateFormat = new SimpleDateFormat(datePattern);
	}
	public DateJsonValueProcessor(){
		
	}

	public Object processArrayValue(Object arg0, JsonConfig arg1) {

		return process(arg0);
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		return process(arg1);
	}

	private Object process(Object value) {
		if(value != null){
			return dateFormat.format((Date) value);
		}else{
			return null;
		}
	}
}
