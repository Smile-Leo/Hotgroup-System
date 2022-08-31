package com.hotgroup.commons.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;
import java.io.Serializable;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private ChatMessageTypeEnum type;

    private String chatId;

    private String userId;

    private String message;

    private String sessionId;

    private transient Session session;

}


