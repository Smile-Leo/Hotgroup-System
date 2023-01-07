package com.hotgroup.commons.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Lzw
 * @date 2022/9/28.
 */
@RequiredArgsConstructor
public class RedisCacheResolver extends CachedExpressionEvaluator implements CacheResolver {

    private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private final CacheManager cacheManager;

    private final String keyTemplate = "Cache::%s";

    protected final static ThreadLocal<CacheTTL> CACHE_TTL = new ThreadLocal<>();
    private final Map<String, Expression> keyCache = new ConcurrentHashMap<>(64);


    private Expression getCacheExpression(String cacheName) {
        Expression expression = keyCache.get(cacheName);
        if (expression != null) {
            return expression;
        }
        Expression value = EXPRESSION_PARSER.parseExpression(cacheName);
        keyCache.put(cacheName, value);
        return value;
    }

    /**
     * 默认 cacheName
     *
     * @param context
     * @return
     */
    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        CacheTTL annotation = context.getMethod().getAnnotation(CacheTTL.class);
        CACHE_TTL.set(annotation);
        Set<String> cacheNames = context.getOperation().getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
            MethodBasedEvaluationContext evaluationContext =
                    new MethodBasedEvaluationContext(TypedValue.NULL, context.getMethod(), context.getArgs(), nameDiscoverer);
            return cacheNames.stream().map(s -> {
                        String value = getCacheExpression(s).getValue(evaluationContext, String.class);
                        return String.format(keyTemplate, value);
                    })
                    .map(cacheManager::getCache)
                    .collect(Collectors.toList());
        }
        String s = String.format(keyTemplate,
                context.getTarget().getClass().getName());
        return Collections.singleton(cacheManager.getCache(s));
    }
}

