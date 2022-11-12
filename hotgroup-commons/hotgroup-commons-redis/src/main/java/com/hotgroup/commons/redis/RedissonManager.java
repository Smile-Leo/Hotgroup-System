package com.hotgroup.commons.redis;

import com.hotgroup.commons.redis.config.strategy.*;
import com.hotgroup.commons.redis.constant.RedisConnectionType;
import com.hotgroup.commons.redis.props.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

/**
 * Redisson核心配置，用于提供初始化的redisson实例
 *
 * @author Lzw
 * @date 2022/10/9.
 */
@Slf4j
public class RedissonManager implements DisposableBean {

    private Redisson redisson = null;

    public RedissonManager() {
    }

    public RedissonManager(RedissonProperties redissonProperties) {
        Config config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);

        final RetryTemplate retryTemplate = RetryTemplate.builder()
                .retryOn(RuntimeException.class)
                .exponentialBackoff(500L, 3.0d, 10000L)
                .infiniteRetry()
                .build();
        redisson = retryTemplate.execute(retryContext -> {
            try {
                config.setCodec(JsonJacksonCodec.INSTANCE);
                RedissonClient redissonClient = Redisson.create(config);
                return (Redisson) redissonClient;
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("redis连接错误,正在重新连接...  当前重试{}次", retryContext.getRetryCount());
                throw new RedisException();
            }
        });

    }

    public Redisson getRedisson() {
        return redisson;
    }

    @Override
    public void destroy() throws Exception {
        redisson.shutdown();
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    public static class RedissonConfigFactory {

        private static final RedissonConfigFactory FACTORY = new RedissonConfigFactory();

        private RedissonConfigFactory() {
        }

        public static RedissonConfigFactory getInstance() {
            return FACTORY;
        }


        /**
         * 根据连接类型获取对应连接方式的配置,基于策略模式
         *
         * @param redissonProperties redisson配置
         * @return Config
         */
        public Config createConfig(RedissonProperties redissonProperties) {
            Assert.notNull(redissonProperties, "redissonProperties cannot be NULL");
            Assert.notNull(redissonProperties.getAddress(), "redisson.lock.server.address cannot be NULL!");
            Assert.notNull(redissonProperties.getType(), "redisson.lock.server.password cannot be NULL");
            String connectionType = redissonProperties.getType();
            // 声明配置上下文
            RedissonConfigContext redissonConfigContext = null;
            if (connectionType.equals(RedisConnectionType.STANDALONE.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new StandaloneRedissonConfigStrategyImpl());
            } else if (connectionType.equals(RedisConnectionType.SENTINEL.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new SentinelRedissonConfigStrategyImpl());
            } else if (connectionType.equals(RedisConnectionType.CLUSTER.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new ClusterRedissonConfigStrategyImpl());
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new MasterslaveRedissonConfigStrategyImpl());
            } else {
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigContext.createRedissonConfig(redissonProperties);
        }
    }


}
