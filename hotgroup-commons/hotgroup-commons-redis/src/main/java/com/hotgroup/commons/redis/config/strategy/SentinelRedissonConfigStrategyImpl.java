package com.hotgroup.commons.redis.config.strategy;

import com.hotgroup.commons.redis.constant.GlobalConstant;
import com.hotgroup.commons.redis.props.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;

/**
 * 哨兵方式Redis连接配置
 *
 * @author Lzw
 * @date 2020-10-22
 */
@Slf4j
public class SentinelRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String sentinelAliasName = addrTokens[0];
            // 设置redis配置文件sentinel.conf配置的sentinel别名
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(sentinelAliasName);
            sentinelServersConfig.setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                sentinelServersConfig.setPassword(password);
            }
            // 设置sentinel节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                sentinelServersConfig.addSentinelAddress(GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + addrTokens[i]);
            }
            log.info("初始化[sentinel]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("sentinel Redisson init error", e);
        }
        return config;
    }
}
