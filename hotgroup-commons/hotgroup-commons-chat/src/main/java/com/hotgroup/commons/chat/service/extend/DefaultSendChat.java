package com.hotgroup.commons.chat.service.extend;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.service.ChatChannel;

import javax.websocket.Session;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
public class DefaultSendChat implements SendChatInterceptor {

    @Override
    public void preHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        String chatId = message.getData().getId();
        String msg = message.getData().getMsg();
        ChatChannel.sendToChat(session, chatId, msg);
    }


}
