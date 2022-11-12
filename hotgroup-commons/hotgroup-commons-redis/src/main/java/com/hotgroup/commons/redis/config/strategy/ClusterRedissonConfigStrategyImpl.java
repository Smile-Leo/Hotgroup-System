package com.hotgroup.commons.redis.config.strategy;

import com.hotgroup.commons.redis.constant.GlobalConstant;
import com.hotgroup.commons.redis.props.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;

/**
 * 集群方式Redisson配置
 *
 * @author Lzw
 * @date 2020-10-22
 */
@Slf4j
public class ClusterRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            String[] addrTokens = address.split(",");
            // 设置cluster节点的服务IP和端口
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            for (int i = 0; i < addrTokens.length; i++) {
                clusterServersConfig.addNodeAddress(
                        GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + addrTokens[i]);
                if (StringUtils.isNotBlank(password)) {
                    clusterServersConfig.setPassword(password);
                }
            }
            log.info("初始化[cluster]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("cluster Redisson init error", e);
        }
        return config;
    }
}
