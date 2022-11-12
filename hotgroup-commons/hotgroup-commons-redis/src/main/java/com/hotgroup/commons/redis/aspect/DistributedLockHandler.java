package com.hotgroup.commons.redis.aspect;

import com.hotgroup.commons.redis.RedissonLock;
import com.hotgroup.commons.redis.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.spring.session.config.EnableRedissonWebSession;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 分布式锁解析器
 *
 * @author Lzw
 * @date 2020-10-22
 * @link https://github.com/TaXueWWL/redis-distributed-lock
 */
@Slf4j
@Aspect
@Component
public class DistributedLockHandler {

    @Resource
    RedissonLock redissonLock;

    /**
     * 切面环绕通知
     *
     * @param joinPoint       ProceedingJoinPoint
     * @param distributedLock DistributedLock
     * @return Object
     */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        //获取锁名称
        String lockName = distributedLock.value();
        //获取超时时间
        int expireSeconds = distributedLock.expireSeconds();

        if (redissonLock.lock(lockName, expireSeconds)) {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("获取Redis分布式锁[异常]，加锁失败", throwable);
                throwable.printStackTrace();
            } finally {
                redissonLock.release(lockName);
            }
        } else {
            log.error("获取Redis分布式锁[失败]");
        }
        return null;
    }
}
