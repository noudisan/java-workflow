package com.it.cloud.common.utils;

import java.util.HashMap;

/**
 * MapUtils
 *
 * @author 司马缸砸缸了
 * @Date 2019-07-12
 */
public class MapUtils extends HashMap<String,Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key,value);
        return this;
    }
}
