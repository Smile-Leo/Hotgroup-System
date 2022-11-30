package com.hotgroup.commons.chat.service.extend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.service.MessageInterceptor;
import com.hotgroup.commons.chat.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.Session;
import java.util.List;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
@Component
public class SendChatWrapper {

    private static List<SendChatInterceptor> interceptors;
    private final static DefaultSendChat SEND_CHAT = new DefaultSendChat();

    @Autowired
    public SendChatWrapper(List<SendChatInterceptor> interceptors) {
        interceptors.sort((i1, i2) -> {
            Order order1 = i1.getClass().getAnnotation(Order.class);
            Order order2 = i2.getClass().getAnnotation(Order.class);
            int o1 = order1 == null ? 0 : order1.value();
            int o2 = order2 == null ? 0 : order2.value();
            return Integer.compare(o1, o2);
        });
        SendChatWrapper.interceptors = interceptors;
    }

    public static void handle(MessageEnum type, String message, Session session) throws Exception {
        MessageDTO<ChatDTO> toChat = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
        });
        Assert.notNull(toChat.getData(), "数据结构有误");
        Assert.hasText(toChat.getData().getId(), "id不能为空");
        Assert.hasText(toChat.getData().getMsg(), "msg不能为空");

        preHandle(type, toChat, session);
        SEND_CHAT.preHandle(type, toChat, session);
        postHandle(type, toChat, session);
    }

    private static void preHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        for (SendChatInterceptor interceptor : interceptors) {
            interceptor.preHandle(type, message, session);
        }
    }

    private static void postHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        for (SendChatInterceptor interceptor : interceptors) {
            interceptor.postHandle(type, message, session);
        }
    }
}
