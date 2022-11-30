package com.hotgroup.commons.chat.service.extend;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.service.MessageInterceptor;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
public interface SendChatInterceptor extends MessageInterceptor<MessageDTO<ChatDTO>> {
}
