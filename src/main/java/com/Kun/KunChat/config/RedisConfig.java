package com.Kun.KunChat.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Author: Beta
 * Date: 2025/4/18 10:44
 * Param:
 * Return:
 * Description:
 **/

@EnableCaching
@Configuration
public class RedisConfig {

    // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化对象
    private final FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
    private final GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

    // String 序列化
    private final StringRedisSerializer serializer = new StringRedisSerializer();

    @Bean("RedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // LocalDate序列化
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 设置序列化格式
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        mapper.registerModule(javaTimeModule);

        // 设置 key 和 value 的序列化方式
        redisTemplate.setKeySerializer(serializer); // key 使用 String 序列化
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);

        // 设置 hash 的 key 和 value 序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

        return redisTemplate;
    }

    @Bean("HashOperations")
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Primary
    @Bean("CacheManager_Nomal")
    public CacheManager cacheManager1(RedisConnectionFactory redisConnectionFactory) {

        // 配置自动缓存的序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)).serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericFastJsonRedisSerializer)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    }

    // 用于设置用户的缓存时间
    @Bean("CacheManager_User")
    public CacheManager cacheManager2(RedisConnectionFactory redisConnectionFactory) {

        // 配置自动缓存的序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(7)).serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericFastJsonRedisSerializer)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    }

    @Bean("KeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            // 类名
            String className = target.getClass().getSimpleName() + ":";
            // 方法名
            String methodName = method.getName() + ":";
            // 字符拼接
            sb.append(className);
            sb.append(methodName);
            // 请求参数的拼接
            for (Object obj : params) {
                sb.append(obj.toString());
                int i = 0;
                if (params.length - i != 1) {
                    sb.append("&");
                }
                i++;
            }
            return sb.toString();
        };

    }

}
