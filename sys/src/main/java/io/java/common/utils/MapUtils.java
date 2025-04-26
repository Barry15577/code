package io.java.common.utils;

import java.util.HashMap;


/**
 * Map tools type
 *
 * @author Jiasi Lu
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
