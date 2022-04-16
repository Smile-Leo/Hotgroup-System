package com.hotgroup.commons.redis.message;

/**
 * redis消息
 *
 * @author Lzw
 * @date 2021/03/03
 */
public class RedisMessage {
    /**
     * 消息实体
     */
    private Object message;

    /**
     * 消息频道
     */
    private String channelName;

    /**
     * 构造器
     *
     * @param message
     * @param channelName
     */
    public RedisMessage(String channelName, Object message) {
        super();
        this.message = message;
        this.channelName = channelName;
    }

    public String channelName() {
        return this.channelName;
    }

    @SuppressWarnings("unchecked")
    public <T> T message() {
        return (T) this.message;
    }

    /**
     * 创建消息对象
     *
     * @param channelName
     * @param message
     * @return
     */
    public static RedisMessage createMsg(String channelName, Object message) {
        return new RedisMessage(channelName, message);
    }

}
