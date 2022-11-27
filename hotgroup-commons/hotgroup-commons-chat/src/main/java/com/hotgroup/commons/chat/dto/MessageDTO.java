package com.hotgroup.commons.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Socket消息")
public class MessageDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "状态码")
    private int code;

    @Schema(title = "提醒消息")
    private String msg;

    @Schema(title = "分页总数")
    private Long total;

    @Schema(title = "消息类型")
    private MessageEnum type;

    @Schema(title = "数据对象")
    private T data;


    public static <U> MessageDTO<U> success(MessageEnum type, U data) {
        MessageDTO<U> dto = new MessageDTO<>();
        dto.setCode(200);
        dto.setType(type);
        dto.setData(data);
        return dto;
    }

    public static <U> MessageDTO<U> success() {
        MessageDTO<U> dto = new MessageDTO<>();
        dto.setCode(200);
        return dto;
    }

    public static <U> MessageDTO<U> success(String msg) {
        MessageDTO<U> dto = new MessageDTO<>();
        dto.setCode(200);
        dto.setMsg(msg);
        return dto;
    }

    public static <U> MessageDTO<U> error(String msg) {
        MessageDTO<U> dto = new MessageDTO<>();
        dto.setCode(400);
        dto.setMsg(msg);
        return dto;
    }

    public static <U> MessageDTO<U> fail(String msg) {
        MessageDTO<U> dto = new MessageDTO<>();
        dto.setCode(500);
        dto.setMsg(msg);
        return dto;
    }

}


