package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author: Beta
 * Date: 2025/4/18 10:55
 * Param:
 * Return:
 * Description:
 **/
@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, HashOperations<String, String, Object> hashOperations) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = hashOperations;
    }

    @Override
    public void putString(String key, String value,int time) {
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public void putHash(String key, Map<String, Object> value) {
        hashOperations.putAll(key, value);
    }

    @Override
    public Map<String, Object> getHash(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Object getHashField(String key, String field) {
        return hashOperations.get(key, field);
    }

    @Override
    public void deleteHashField(String key, String field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void deleteHash(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasHash(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


    // @Override
    // public List<String> getAllHashKeys(String pattern) {
    //     Set<String> keys = redisTemplate.keys(pattern);
    //     return new ArrayList<>(Optional.ofNullable(keys).orElse(Collections.emptySet()));
    // }
}
