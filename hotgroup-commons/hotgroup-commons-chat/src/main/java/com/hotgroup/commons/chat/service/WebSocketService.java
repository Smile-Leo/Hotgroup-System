package com.hotgroup.commons.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
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
                    MessageDTO<ChatDTO> chatDTOMessageDTO = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
                    });
                    String chatId = chatDTOMessageDTO.getData().getId();
                    Assert.hasText(chatId, "id不能为空");
                    ChatChannel.join(chatId, session);
                    String name = userPrincipal instanceof LoginUser ? ((LoginUser) userPrincipal).getUsername() : "游客" + session.getId();
                    ChatChannel.sendToChat(chatId, name + " 进入了");
                    break;
                case CHAT_EXIT:
                    ChatChannel.exit(session);
                    MessageDTO<ChatDTO> exitDto = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
                    });
                    String exitChatId = exitDto.getData().getId();
                    Assert.hasText(exitChatId, "id不能为空");
                    String name2 = userPrincipal instanceof LoginUser ? ((LoginUser) userPrincipal).getUsername() : "游客" + session.getId();
                    ChatChannel.sendToChat(exitChatId, name2 + " 离开了");
                    break;
                case USER_LOGIN:
                    Assert.isTrue(Objects.nonNull(userPrincipal) && userPrincipal instanceof LoginUser, "请先登陆");
                    LoginUser loginUser = (LoginUser) userPrincipal;
                    String userId = UserIdCreate.getId(loginUser);
                    UserChannel.login(userId, loginUser.getUsername(), session);
                    break;
                case SEND_USER:
                    Assert.isTrue(Objects.nonNull(userPrincipal) && userPrincipal instanceof LoginUser, "请先登陆");
                    MessageDTO<ChatDTO> toUser = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
                    });
                    Assert.notNull(toUser.getData(), "数据结构有误");
                    Assert.hasText(toUser.getData().getId(), "id不能为空");
                    Assert.hasText(toUser.getData().getMsg(), "msg不能为空");
                    UserChannel.sendToUser(toUser.getData().getId(), toUser.getData().getMsg());
                    break;
                case SEND_CHAT:
                    MessageDTO<ChatDTO> toChat = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
                    });
                    Assert.notNull(toChat.getData(), "数据结构有误");
                    Assert.hasText(toChat.getData().getId(), "id不能为空");
                    Assert.hasText(toChat.getData().getMsg(), "msg不能为空");
                    ChatChannel.sendToChat(toChat.getData().getId(), toChat.getData().getMsg());
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
