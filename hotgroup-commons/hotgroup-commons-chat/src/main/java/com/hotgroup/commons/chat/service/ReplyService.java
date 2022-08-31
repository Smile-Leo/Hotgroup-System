package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
public interface ReplyService {

    void send(ChatMessageDto dto);
}
