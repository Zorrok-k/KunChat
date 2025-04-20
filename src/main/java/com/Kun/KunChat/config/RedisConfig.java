package com.Kun.KunChat.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


/**
 * Author: Beta
 * Date: 2025/4/18 10:44
 * Param:
 * Return:
 * Description:
 **/

@Configuration
public class RedisConfig {

    // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化对象
    private final Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

    // String 序列化
    private final StringRedisSerializer serializer = new StringRedisSerializer();

    @Bean("RedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 设置 key 和 value 的序列化方式
        redisTemplate.setKeySerializer(serializer); // key 使用 String 序列化
        redisTemplate.setValueSerializer(jacksonSerializer); // value 使用 Jackson2Json 序列化

        // 设置 hash 的 key 和 value 序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonSerializer);

        return redisTemplate;
    }

    @Bean("HashOperations")
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean("KeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            // 类名
            String className = target.getClass().getName() + ":";
            // 方法名
            String methodName = method.getName() + ":";
            // 字符拼接
            sb.append("RedisAutoCache:");
            sb.append(className);
            sb.append(methodName);
            // 请求参数的拼接
            for (Object obj : params) {
                sb.append(obj.toString());
                sb.append("&");
            }
            return sb.toString();
        };

    }

    @Bean("CacheManager")
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        // 配置自动缓存的序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
