package com.clarazheng.model;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by clara on 2017/5/4.
 */
public class ViewObject {
    Map<String,Object> objs=new HashMap<String,Object>();

    public void set(String key,Object value){
        objs.put(key, value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
