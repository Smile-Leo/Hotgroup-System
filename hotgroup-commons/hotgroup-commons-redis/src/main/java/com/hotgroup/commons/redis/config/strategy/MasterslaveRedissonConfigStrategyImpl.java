package com.hotgroup.commons.redis.config.strategy;

import com.hotgroup.commons.redis.constant.GlobalConstant;
import com.hotgroup.commons.redis.props.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 主从方式Redisson配置
 * 连接方式：主节点,子节点,子节点
 * <p>格式为: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381</p>
 */
@Slf4j
public class MasterslaveRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String masterNodeAddr = addrTokens[0];
            // 设置主节点ip
            MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
            masterSlaveServersConfig.setMasterAddress(masterNodeAddr);
            if (StringUtils.isNotBlank(password)) {
                masterSlaveServersConfig.setPassword(password);
            }
            masterSlaveServersConfig.setDatabase(database);
            // 设置从节点，移除第一个节点，默认第一个为主节点
            List<String> slaveList = new ArrayList<>();
            for (String addrToken : addrTokens) {
                slaveList.add(GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + addrToken);
            }
            slaveList.remove(0);

            masterSlaveServersConfig.addSlaveAddress(slaveList.toArray(new String[0]));
            log.info("初始化[MASTERSLAVE]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("MASTERSLAVE Redisson init error", e);
        }
        return config;
    }
}
