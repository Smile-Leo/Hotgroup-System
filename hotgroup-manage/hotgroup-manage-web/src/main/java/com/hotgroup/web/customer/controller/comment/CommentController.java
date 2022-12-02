package com.hotgroup.web.customer.controller.comment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.core.service.IHgCommentService;
import com.hotgroup.manage.domain.dto.HgCommentDto;
import com.hotgroup.manage.domain.entity.HgComment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author aijiaming
 * @date 2022/11/27.
 */
@Tag(name = "用户评论")
@RestController
@RequestMapping("comment")
@Slf4j
public class CommentController {

    @Resource
    IHgCommentService iHgCommentService;

    @GetMapping("list")
    @Operation(summary = "评论列表")
    public AjaxResult<List<HgComment>> list(String dataId, Integer dataType, Integer pageNo, Integer pageSize, @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        Page<HgComment> page = iHgCommentService.page(pageNo, pageSize, dataId, dataType, loginUser.getUser().getId());
        return AjaxResult.page(page);
    }

    @GetMapping("get")
    @Operation(summary = "评论详情")
    public AjaxResult<HgCommentDto> get(String id) {
        return AjaxResult.success(iHgCommentService.get(id));
    }

    @PostMapping("save")
    @Operation(summary = "用户评论保存")
    public AjaxResult<String> save(@Validated @RequestBody HgCommentDto dto) {
        iHgCommentService.save(dto);
        return AjaxResult.success();
    }

    @GetMapping("delete")
    @Operation(summary = "用户评论删除")
    public AjaxResult<String> delete(String id) {
        iHgCommentService.delete(id);
        return AjaxResult.success();
    }
}
