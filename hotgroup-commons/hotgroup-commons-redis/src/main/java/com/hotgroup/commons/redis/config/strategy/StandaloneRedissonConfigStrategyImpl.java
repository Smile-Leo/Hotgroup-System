package com.hotgroup.commons.redis.config.strategy;

import com.hotgroup.commons.redis.constant.GlobalConstant;
import com.hotgroup.commons.redis.props.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * 单机方式Redisson配置
 *
 * @author Lzw
 * @date 2020-10-22
 */
@Slf4j
public class StandaloneRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + address;
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress(redisAddr);
            singleServerConfig.setDatabase(database);
            singleServerConfig.setKeepAlive(true);
            singleServerConfig.setTcpNoDelay(true);
            if (StringUtils.isNotBlank(password)) {
                singleServerConfig.setPassword(password);
            }
            if (redissonProperties.getConnectionMinimumIdleSize() > 0) {
                singleServerConfig.setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
            }
            log.info("初始化[standalone]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("standalone Redisson init error", e);
        }
        return config;
    }
}
