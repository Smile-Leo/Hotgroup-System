package com.hotgroup.web.customer.controller.socket;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lzw
 * @date 2022/11/27.
 */
@Tag(name = "Socket相关")
@RestController
@RequestMapping("chat")
public class ChatController {

    @Operation(summary = "发送(收到)群聊天加入")
    @PostMapping("CHAT_JOIN")
    public AjaxResult<MessageDTO<ChatDTO>> chatJoin(@RequestBody MessageDTO<ChatDTO> dto) {
        return AjaxResult.success(new MessageDTO<>());
    }

    @Operation(summary = "发送(收到)群聊天退出")
    @PostMapping("CHAT_EXIT")
    public AjaxResult<MessageDTO<ChatDTO>> chatExit(@RequestBody MessageDTO<ChatDTO> dto) {
        return AjaxResult.success(new MessageDTO<>());
    }

    @Operation(summary = "用户登陆")
    @PostMapping("USER_LOGIN")
    public AjaxResult<MessageDTO<ChatDTO>> userLogin(@RequestBody MessageDTO<ChatDTO> dto) {
        return AjaxResult.success(new MessageDTO<>());
    }

    @Operation(summary = "发送(收到)用户消息")
    @PostMapping("SEND_USER")
    public AjaxResult<MessageDTO<ChatDTO>> sendUser(@RequestBody MessageDTO<ChatDTO> dto) {
        return AjaxResult.success(new MessageDTO<>());
    }

    @Operation(summary = "发送(收到)群消息")
    @PostMapping("SEND_CHAT")
    public AjaxResult<MessageDTO<ChatDTO>> sendChat(@RequestBody MessageDTO<ChatDTO> dto) {
        return AjaxResult.success(new MessageDTO<>());
    }


}
