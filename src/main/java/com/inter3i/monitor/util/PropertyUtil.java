package com.inter3i.monitor.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/19 17:48
 */
public class PropertyUtil {

    private static Map<String,Map<String,String>> propertiesMap = new HashMap<String, Map<String, String>>();

    public static Map<String,String> loadProperties(String propertiesName){
        synchronized (propertiesMap){
            if(propertiesMap.containsKey(propertiesName)){
                return propertiesMap.get(propertiesName);
            }
            Properties props = new Properties();
            InputStream in = null;
            try {
                in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertiesName);
                props.load(in);
                Map<String,String> map = new HashMap<String, String>();
                for(Object key : props.keySet()){
                    map.put(key.toString(),props.getProperty(key.toString()));
                }
                propertiesMap.put(propertiesName,map);
                return map;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(null != in) {
                        in.close();
                    }
                } catch (IOException e) {
                }
            }
            return new HashMap<String, String>();
        }
    }


}
