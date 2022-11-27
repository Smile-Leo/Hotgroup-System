package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.util.JsonUtil;
import com.hotgroup.commons.core.domain.model.LoginUser;

import javax.websocket.Session;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Lzw
 * @date 2022/11/27.
 */
public class ChatChannel {

    private static final Map<String, List<Session>> CHATS = new ConcurrentHashMap<>();
    public static final String ME = "我";
    public static final String ANONYMOUS = "游客";

    public static void join(String chatId, Session session) {
        exit(session);
        CHATS.putIfAbsent(chatId, new CopyOnWriteArrayList<>());
        CHATS.get(chatId).add(session);
    }

    public static void exit(Session session) {
        CHATS.values().removeIf(sessions -> {
            sessions.removeIf(s -> s.getId().equals(session.getId()));
            return sessions.isEmpty();
        });
    }

    public static void sendToChat(Session send, String chatId, String message) {
        Principal userPrincipal = send.getUserPrincipal();
        Integer level = 0;
        String sendName = ANONYMOUS + send.getId().substring(0, 5);

        if (Objects.nonNull(userPrincipal) && userPrincipal instanceof LoginUser) {
            LoginUser user = (LoginUser) userPrincipal;
            sendName = user.getUsername();
            level = user.getUser().getLevel();
        }

        for (Session session : CHATS.getOrDefault(chatId, Collections.emptyList())) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setId(chatId);
            String name = send.getId().equals(session.getId()) ? ME : sendName;
            chatDTO.setMsg(formatMessage(level, name, message));
            session.getAsyncRemote().sendText(
                    JsonUtil.toJson(MessageDTO.success(MessageEnum.SEND_CHAT, chatDTO)));
        }
    }

    private static String formatMessage(Integer level, String name, String message) {
        return new StringBuilder()
                .append(level)
                .append(":")
                .append(name)
                .append(":")
                .append(message)
                .toString();
    }
}
