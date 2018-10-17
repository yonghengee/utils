package com.yqh.shop.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class HttpUtil {
	
	private static Logger logger = LogManager.getLogger(HttpUtil.class.getName());
	
    private final static int CONNECT_TIMEOUT = 5000; // in milliseconds  
    private final static String DEFAULT_ENCODING = "UTF-8";  
      
    public static String postData(String urlStr, String data){  
        return postData(urlStr, data, null);  
    }  
      
    public static String postData(String urlStr, String data, String contentType){  
        BufferedReader reader = null;  
        try {  
            URL url = new URL(urlStr);  
            URLConnection conn = url.openConnection();  
            conn.setDoOutput(true);  
            conn.setConnectTimeout(CONNECT_TIMEOUT);  
            conn.setReadTimeout(CONNECT_TIMEOUT);  
            if(contentType != null)  
                conn.setRequestProperty("content-type", contentType);  
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);  
            if(data == null)  
                data = "";  
            writer.write(data);   
            writer.flush();  
            writer.close();    
  
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));  
            StringBuilder sb = new StringBuilder();  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                sb.append(line);  
                sb.append("\r\n");  
            }  
            return sb.toString();  
        } catch (IOException e) {  
        	e.printStackTrace();
            logger.error("Error connecting m3d36c " + urlStr + ": " + e.getMessage());
        } finally {  
            try {  
                if (reader != null)  
                    reader.close();  
            } catch (IOException e) {  
            }  
        }  
        return null;  
    }

    /**
     * 获取请求的参数信息
     *
     * @param request
     * @return
     */
    public static Map<String, String> getReqParams(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Enumeration<String> paramNames = request.getParameterNames();
        if (paramNames.hasMoreElements()) {
            Map<String, String> map = new HashMap<String, String>();
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
            while (paramNames.hasMoreElements()) {
                paramName = paramNames.nextElement();
                paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        map.put(paramName, paramValue);
                    }
                }
            }
            return map;
        }
        return null;
    }

    public static Map<String, String> getReqParams(String reqParams) {
        String[] arge = StringUtils.split(reqParams, "&");
        if (arge.length > 0) {
            Map<String, String> map = new HashMap<>();
            for (String str : arge) {
                String[] param = StringUtils.split(str, "=");
                if (param.length == 2) {
                    map.put(param[0], param[1]);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 获取Post数据
     *
     * @param request
     * @return
     * @throws
     */
    public static String getReqPostString(HttpServletRequest request, Logger log) {
        StringBuilder reqData = new StringBuilder();
        String line = null;
        BufferedReader reader = null;
        try {
            request.setCharacterEncoding("utf8");// 设置编码
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                reqData.append(line);
            }
            if (reqData.length() <= 0) {
                return null;
            }
            String strJson = reqData.toString();
            reader.close();
            log.info("获取post数据: strJson=" + strJson);
            return strJson;
        } catch (IOException e) {
            log.error("获取post数据错误:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取Post数据
     *
     * @param request
     * @return
     * @throws
     */
    public static String getReqPostStringByEncode(HttpServletRequest request, Logger log) {
        StringBuilder reqData = new StringBuilder();
        String line = null;
        BufferedReader reader = null;
        try {
            request.setCharacterEncoding("gbk");
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                reqData.append(line);
            }
            if (reqData.length() <= 0) {
                return null;
            }
            String strJson = reqData.toString();
            reader.close();
            log.info("获取post数据: strJson=" + strJson);
            return strJson;
        } catch (IOException e) {
            log.error("获取post数据错误:" + e.getMessage(), e);
        }
        return null;
    }
}
