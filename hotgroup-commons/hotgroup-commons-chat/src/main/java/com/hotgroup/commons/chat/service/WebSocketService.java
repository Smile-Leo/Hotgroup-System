package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;
import com.hotgroup.commons.chat.util.JsonUtil;
import com.hotgroup.commons.core.domain.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Lzw
 * @date 2022/5/24.
 */
@Component
@ServerEndpoint(value = "/websocket/chat")
@Slf4j
public class WebSocketService {

    private static ChatService chatService;

    @Resource
    public void init(ChatService chatService) {
        WebSocketService.chatService = chatService;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        Authentication userPrincipal = (Authentication) session.getUserPrincipal();
        if (Objects.nonNull(userPrincipal)) {
            LoginUser loginUser = (LoginUser) userPrincipal.getPrincipal();
        }

        ChatService.USER_LIST.putIfAbsent(session.getId(), new CopyOnWriteArrayList<>());
        ChatService.USER_LIST.get(session.getId()).add(session);
        session.getAsyncRemote()
                .sendText(JsonUtil.toJson(ChatMessageDto.builder().message("welcoming").build()));
    }

    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        removeSession(session);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws IOException {
        removeSession(session);
    }

    private void removeSession(Session session) throws IOException {
        for (List<Session> sessions : ChatService.USER_LIST.values()) {
            sessions.removeIf(v -> session.getId().equals(v.getId()));
        }
        for (List<Session> sessions : ChatService.CHAT_LIST.values()) {
            sessions.removeIf(v -> session.getId().equals(v.getId()));
        }
        ChatService.USER_LIST.remove(session.getId());
        session.close();
    }


    @OnMessage
    public void onMessage(String message, Session session) {

        ChatMessageDto dto = JsonUtil.toObject(message, ChatMessageDto.class);

        switch (dto.getType()) {
            case CHAT_CREATE:
            case CHAT_JOIN:
                ChatService.CHAT_LIST.putIfAbsent(dto.getChatId(), new CopyOnWriteArrayList<>());
                ChatService.CHAT_LIST.get(dto.getChatId()).add(session);
                break;
            case CHAT_EXIT:
                Optional.ofNullable(ChatService.CHAT_LIST.get(dto.getChatId()))
                        .ifPresent(sessions -> sessions.removeIf(session1 -> session1.getId().equals(session.getId())));
                break;
            case CHAT_DESTORY:
                ChatService.CHAT_LIST.remove(dto.getChatId());
                break;
            default:

        }

        chatService.send(dto);

    }

}
