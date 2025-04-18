package com.Kun.KunChat.service;

import java.util.List;
import java.util.Map;

/**
 * Author: Beta
 * Date: 2025/4/18 10:50
 * Param:
 * Return:
 * Description:
 **/

public interface RedisService {

    public void putString(String key, String value,int time);

    public void putHash(String key, Map<String, Object> value);

    public Map<String, Object> getHash(String key);

    public Object getHashField(String key, String field);

    public void deleteHashField(String key, String field);

    public void deleteHash(String key);

    public boolean hasHash(String key);


    // public List<String> getAllHashKeys(String pattern);

}
