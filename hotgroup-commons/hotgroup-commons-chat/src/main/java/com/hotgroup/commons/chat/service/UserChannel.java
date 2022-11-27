package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.dto.UserDTO;
import com.hotgroup.commons.chat.util.JsonUtil;
import com.hotgroup.commons.core.domain.model.LoginUser;

import javax.websocket.Session;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Lzw
 * @date 2022/11/27.
 */
public class UserChannel {

    private static final Map<String, List<Session>> USERS = new ConcurrentHashMap<>();
    public static final String ME = "我";

    public static void login(String userId, String nickName, Session session) {
        USERS.putIfAbsent(userId, new CopyOnWriteArrayList<>());
        USERS.get(userId).add(session);
        UserDTO data = new UserDTO();
        data.setId(userId);
        data.setNickName(nickName);
        session.getAsyncRemote().sendText(
                JsonUtil.toJson(MessageDTO.success(MessageEnum.USER_LOGIN, data)));
    }

    public static void exit(Session session) {
        USERS.values().removeIf(sessions -> {
            sessions.removeIf(s -> s.getId().equals(session.getId()));
            return sessions.isEmpty();
        });
    }

    public static void sendToUser(Session send, String userId, String message) {
        LoginUser user = (LoginUser) send.getUserPrincipal();
        for (Session session : USERS.getOrDefault(userId, Collections.emptyList())) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setId(userId);
            String name = send.getId().equals(session.getId()) ? ME : user.getUsername();
            chatDTO.setMsg(formatMessage(user.getUser().getLevel(), name, message));
            session.getAsyncRemote().sendText(
                    JsonUtil.toJson(MessageDTO.success(MessageEnum.SEND_USER, chatDTO)));
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
