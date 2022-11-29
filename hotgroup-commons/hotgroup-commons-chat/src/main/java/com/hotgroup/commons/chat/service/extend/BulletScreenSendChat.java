package com.hotgroup.commons.chat.service.extend;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.service.MessageCreate;
import com.hotgroup.commons.redis.RedissonManager;
import org.redisson.Redisson;
import org.redisson.api.RDeque;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.websocket.Session;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
@Configuration
@Order(10)
@ConditionalOnClass(Redisson.class)
public class BulletScreenSendChat implements SendChatInterceptor {

    private final Redisson redisson;
    private final Integer max;

    public BulletScreenSendChat(RedissonManager manager,
                                @Value("hotgroup.bullet_screen:50") Integer max) {
        this.redisson = manager.getRedisson();
        this.max = max;
    }

    @Override
    public void preHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        String chatId = message.getData().getId();
        String msg = message.getData().getMsg();


        RDeque<String> deque = redisson.getDeque("CHAT:" + chatId, StringCodec.INSTANCE);

        String to = MessageCreate.to(session, msg, null);
        deque.addLast(to);
        while (deque.size() > max) {
            deque.removeFirst();
        }
    }


}
