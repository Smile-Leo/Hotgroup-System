package com.hotgroup.commons.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author Lzw
 * @date 2022/9/27.
 */
public class RedisKeyGenerator implements KeyGenerator {

    final ObjectMapper objectMapper = new ObjectMapper()
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length == 0) {
            return 0;
        }
        try {
            return FNVHash1(objectMapper.writeValueAsBytes(params));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private int FNVHash1(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b : data)
            hash = (hash ^ b) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }
}
