package com.hotgroup.commons.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.Objects;

/**
 * @author Lzw
 * @date 2022/10/18.
 */
@Slf4j
public class RedissonCacheManager extends RedissonSpringCacheManager {
    public RedissonCacheManager(RedissonClient redisson) {
        super(redisson);
    }

    public RedissonCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config) {
        super(redisson, config);
    }

    public RedissonCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config, Codec codec) {
        super(redisson, config, codec);
    }

    public RedissonCacheManager(RedissonClient redisson, String configLocation) {
        super(redisson, configLocation);
    }

    public RedissonCacheManager(RedissonClient redisson, String configLocation, Codec codec) {
        super(redisson, configLocation, codec);
    }

    @Override
    protected CacheConfig createDefaultConfig() {
        CacheTTL ttl = RedisCacheResolver.CACHE_TTL.get();
        if (Objects.nonNull(ttl)) {
            CacheConfig cacheConfig = new CacheConfig();
            cacheConfig.setTTL(ttl.unit().toMillis(ttl.value()));
            return cacheConfig;
        }
        return super.createDefaultConfig();
    }

    @Override
    public Cache getCache(String name) {
        if (log.isDebugEnabled()) {
            log.debug("Cached Key-> " + name);
        }
        Cache cache = super.getCache(name);
        RedisCacheResolver.CACHE_TTL.remove();
        return cache;
    }
}
