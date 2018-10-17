package com.yqh.bbct.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wyh in 2018/9/17 17:41
 **/
public class MapUtils {

    public static Map<String, Object> convertToMap(Object obj)
            throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            boolean accessFlag = fields[i].isAccessible();
            fields[i].setAccessible(true);

            Object o = fields[i].get(obj);
            if (o != null)
                map.put(varName, o.toString());

            fields[i].setAccessible(accessFlag);
        }

        return map;
    }

    public static List<Map<String, Object>> convertToListMap(List<? extends Object> list)
            throws Exception {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Object object : list) {
            Map<String, Object> hashMap = convertToMap(object);
            maps.add(hashMap);
        }
        return maps;
    }
}
