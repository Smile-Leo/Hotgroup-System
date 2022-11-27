package com.hotgroup.commons.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Schema(title = "消息类型")
public enum MessageEnum {

    @Schema(title = "用户登陆")
    USER_LOGIN,
    @Schema(title = "发送(收到)用户私聊消息")
    SEND_USER,
    @Schema(title = "发送(收到)群聊消息")
    SEND_CHAT,
    @Schema(title = "加入群聊")
    CHAT_JOIN,
    @Schema(title = "退出群聊")
    CHAT_EXIT,

}
