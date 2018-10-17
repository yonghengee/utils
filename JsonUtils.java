package com.yqh.shop.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yqh.component.utils.DateJsonValueProcessor;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 * @author hzm
 *
 */
public class JsonUtils {

	
	/**  
     * 将json对象中包含的null和JSONNull属性修改成""  
     * @param jsonObj  
     */  
    public static String filterNull(JSONObject jsonObj){  
        Iterator<String> it = jsonObj.keys();  
        Object obj = null;  
        String key = null;  
        while (it.hasNext()) {  
            key = it.next();  
            obj = jsonObj.get(key);  
            if(obj instanceof JSONObject){  
                filterNull((JSONObject)obj);  
            }  
            if(obj instanceof JSONArray){  
                JSONArray objArr = (JSONArray) obj;  
                for(int i=0; i<objArr.size(); i++){  
                    filterNull(objArr.getJSONObject(i));  
                }  
            }  
            if(obj == null || obj instanceof JSONNull){  
                jsonObj.put(key, "");  
            }  
        }  
        return jsonObj.toString();
    } 
    
    /****
     * str转map
     * @param str
     * @return
     */
    public static Map<String, String> toMapStr(String str) {
    	Gson gson = new Gson();
    	Map<String, String> gsonMap = gson.fromJson(str, new TypeToken<Map<String, String>>() {
    	}.getType());
    	return gsonMap;
    }

	/****
	 * str转map
	 * @param str
	 * @return
	 */
	public static Map<String, Object> toMapStrObj(String str) {
		Gson gson = new Gson();
		Map<String, Object> gsonMap = gson.fromJson(str, new TypeToken<Map<String, Object>>() {
		}.getType());
		return gsonMap;
	}
    /****
     * str转map list集合
     * @param str
     * @return
     */
    public static List<Map<String, Object>> toMapListStr(String str) {
    	Gson gson = new Gson();
    	List<Map<String, Object>> gsonMap = gson.fromJson(str, new TypeToken<List<Map<String, Object>>>() {
    	}.getType());
    	return gsonMap;
    }
    
    /**
	 * 
	 * JSON 时间解析器具
	 * 
	 * @param datePattern
	 * 
	 * @return
	 */

	public static JsonConfig configJson(String datePattern) {

		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setExcludes(new String[] { "" });

		jsonConfig.setIgnoreDefaultExcludes(false);

		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor(datePattern));

		jsonConfig.registerJsonValueProcessor(Timestamp.class,
				new DateJsonValueProcessor(datePattern));
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, 
				new DateJsonValueProcessor(datePattern));
		// 设置Integer类型为空的默认值 json-lib默认是0  
		jsonConfig.registerDefaultValueProcessor(Integer.class,  
		        new DefaultValueProcessor() {  
		            public Object getDefaultValue(Class type) {  
		                return null;  
		            }  
		        }); 
		return jsonConfig;

	}
	
	/**
	 * 转换成json格式
	 * @param json
	 * @return
	 */
	public static JSONObject toJson(String json){
		JSONObject jsonObject = JSONObject.fromObject(json);
		return jsonObject;
	}
    
	
	/**
	 * 通过key获取value值
	 * @param key
	 * @return
	 */
	public static String getValue(String key,JSONObject jsonObject){
		if(jsonObject.containsKey(key) && !jsonObject.get(key).equals("")){
			return jsonObject.getString(key);
		}
		return null;
	}
	
}
