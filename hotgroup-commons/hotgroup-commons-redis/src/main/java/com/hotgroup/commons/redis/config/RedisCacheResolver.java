package com.hotgroup.commons.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lzw
 * @date 2022/9/28.
 */
@RequiredArgsConstructor
public class RedisCacheResolver implements CacheResolver {

    protected final static ThreadLocal<CacheTTL> CACHE_TTL = new ThreadLocal<>();
    private final CacheManager cacheManager;
    private final String keyTemplate = "Cache::%s";

    /**
     * 默认 cacheName
     *
     * @param cacheOperationInvocationContext
     * @return
     */
    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> cacheOperationInvocationContext) {
        CacheTTL annotation = cacheOperationInvocationContext.getMethod().getAnnotation(CacheTTL.class);
        CACHE_TTL.set(annotation);
        Set<String> cacheNames = cacheOperationInvocationContext.getOperation().getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            return cacheNames.stream().map(s -> String.format(keyTemplate, s))
                    .map(cacheManager::getCache)
                    .collect(Collectors.toList());
        }
        String s = String.format(keyTemplate,
                cacheOperationInvocationContext.getTarget().getClass().getName());
        return Collections.singleton(cacheManager.getCache(s));
    }
}
