package com.hotgroup.commons.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.service.extend.ChatJoinWrapper;
import com.hotgroup.commons.chat.service.extend.SendChatWrapper;
import com.hotgroup.commons.chat.util.JsonUtil;
import com.hotgroup.commons.core.domain.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

/**
 * @author Lzw
 * @date 2022/5/24.
 */
@Component
@ServerEndpoint(value = "/websocket/chat")
@Slf4j
public class WebSocketService {


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        session.getAsyncRemote()
                .sendText(JsonUtil.toJson(MessageDTO.success("welcoming")));
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
        ChatChannel.exit(session);
        UserChannel.exit(session);
        session.close();
    }


    @OnMessage
    public void onMessage(String message, Session session) {

        try {
            MessageDTO dto = JsonUtil.toObject(message, MessageDTO.class);
            Principal userPrincipal = session.getUserPrincipal();
            switch (dto.getType()) {
                case CHAT_JOIN:
                    ChatJoinWrapper.handle(dto.getType(), message, session);
                    break;
                case CHAT_EXIT:
                    ChatChannel.exit(session);
                    MessageDTO<ChatDTO> exitDto = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
                    });
                    Assert.notNull(exitDto.getData(), "??????????????????");
                    String exitChatId = exitDto.getData().getId();
                    Assert.hasText(exitChatId, "id????????????");
                    ChatChannel.sendToChat(session, exitChatId, "?????????");
                    break;
                case USER_LOGIN:
                    Assert.isTrue(Objects.nonNull(userPrincipal) && userPrincipal instanceof LoginUser, "????????????");
                    LoginUser loginUser = (LoginUser) userPrincipal;
                    String userId = UserIdCreate.getId(loginUser);
                    UserChannel.login(userId, loginUser.getUsername(), session);
                    break;
                case SEND_USER:
                    Assert.isTrue(Objects.nonNull(userPrincipal) && userPrincipal instanceof LoginUser, "????????????");
                    MessageDTO<ChatDTO> toUser = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
                    });
                    Assert.notNull(toUser.getData(), "??????????????????");
                    Assert.hasText(toUser.getData().getId(), "id????????????");
                    Assert.hasText(toUser.getData().getMsg(), "msg????????????");
                    UserChannel.sendToUser(session, toUser.getData().getId(), toUser.getData().getMsg());
                    break;
                case SEND_CHAT:
                    SendChatWrapper.handle(dto.getType(), message, session);
                    break;
                default:
            }
            session.getAsyncRemote()
                    .sendText(JsonUtil.toJson(MessageDTO.success()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            session.getAsyncRemote()
                    .sendText(JsonUtil.toJson(MessageDTO.error(e.getMessage())));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getAsyncRemote()
                    .sendText(JsonUtil.toJson(MessageDTO.fail(e.getMessage())));
        }


    }

}
