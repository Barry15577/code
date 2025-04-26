package io.java.common.utils;

/**
 * Redis All Keys
 *
 * @author Jiasi Lu
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
