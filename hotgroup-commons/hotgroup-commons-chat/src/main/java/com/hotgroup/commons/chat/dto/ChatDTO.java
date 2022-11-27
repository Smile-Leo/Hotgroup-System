package com.hotgroup.commons.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Lzw
 * @date 2022/11/27.
 */
@Data
@Schema(title = "消息相关")
public class ChatDTO {
    @Schema(title = "userId或chatId")
    private String id;
    @Schema(title = "消息内容")
    private String msg;
}
