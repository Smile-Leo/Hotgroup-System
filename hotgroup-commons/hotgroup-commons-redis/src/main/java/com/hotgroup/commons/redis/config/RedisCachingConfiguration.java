package com.hotgroup.commons.redis.config;

import com.hotgroup.commons.redis.RedissonManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Lzw
 * @date 2022/9/28.
 */
@EnableCaching
@RequiredArgsConstructor
@Configuration
public class RedisCachingConfiguration extends CachingConfigurerSupport {

    final RedisConnectionFactory redisConnectionFactory;
    final RedisSerializer<String> keySerializer;
    final RedisSerializer<Object> valueSerializer;
    final RedissonManager redissonManager;

    @Override
    @Bean
    public CacheManager cacheManager() {

        return new RedissonCacheManager(redissonManager.getRedisson());
    }

    @Override
    public CacheResolver cacheResolver() {
        return new RedisCacheResolver(cacheManager());
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new RedisKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new IgnoreCacheErrorHandle();
    }
}
