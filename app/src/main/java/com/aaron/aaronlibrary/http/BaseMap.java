package com.aaron.aaronlibrary.http;

import java.util.HashMap;

/**
 * 接口参数基类
 * Created by wpc on 2016/12/12 0012.
 */
public class BaseMap extends HashMap<String, String> {

    public BaseMap() {
        super();
    }

    public BaseMap(String page) {
        this();
        put("currentPage", page);
    }

    @Override
    public String put(String key, String value) {
//        if (!"token".equals(key))
//            value = AES.encrypt(value);
        return super.put(key, value);
    }
}
