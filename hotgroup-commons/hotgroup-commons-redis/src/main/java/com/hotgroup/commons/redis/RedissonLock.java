package com.hotgroup.commons.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现基于Redisson
 *
 * @author Lzw
 * @date 2022/10/9.
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLock {

    final RedissonManager redissonManager;

    /**
     * 加锁操作
     *
     * @return boolean
     */
    public boolean lock(String lockName, long expireSeconds) {
        RLock rLock = redissonManager.getRedisson().getLock(lockName);
        boolean getLock;
        try {
            getLock = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
            if (log.isDebugEnabled()) {
                log.debug("获取Redisson分布式锁[{}],lockName={}", getLock, lockName);
            }
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            Thread.currentThread().interrupt();
            return false;
        }
        return getLock;
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    public void release(String lockName) {
        redissonManager.getRedisson().getLock(lockName).unlock();
    }


}
