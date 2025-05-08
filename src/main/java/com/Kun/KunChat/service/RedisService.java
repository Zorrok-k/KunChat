package com.Kun.KunChat.service;

import java.util.Map;

/**
 * Author: Beta
 * Date: 2025/4/18 10:50
 * Param:
 * Return:
 * Description:
 **/

public interface RedisService {

    void setValue(String key, Object value, long time);

    void setValue(String key, Object value);

    <T> T getValue(String key);

    long getValueTTL(String key);

    void delete(String key);

    void putHash(String key, Map<String, Object> value);

    Map<String, Object> getHash(String key);

    Object getHashField(String key, String field);

    void deleteHashField(String key, String field);

    void deleteHash(String key);

    boolean hasKey(String key);


    // public List<String> getAllHashKeys(String pattern);

}
