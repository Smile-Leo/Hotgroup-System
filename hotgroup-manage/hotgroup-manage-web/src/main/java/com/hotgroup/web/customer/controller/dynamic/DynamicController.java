package com.hotgroup.web.customer.controller.dynamic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgDynamicService;
import com.hotgroup.manage.domain.dto.HgDynamicDto;
import com.hotgroup.manage.domain.entity.HgDynamic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author aijiaming
 * @date 2022/11/27.
 */
@Tag(name = "用户动态")
@RestController
@RequestMapping("dynamic")
@Slf4j
public class DynamicController {

    @Resource
    IHgDynamicService iHgDynamicService;

    @GetMapping("list")
    @Operation(summary ="用户动态列表")
    public AjaxResult<List<HgDynamic>> list(Integer pageNo, Integer pageSize, @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        Page<HgDynamic> page = iHgDynamicService.page(loginUser.getUser().getId(), pageNo, pageSize);
        return AjaxResult.page(page);
    }

    @GetMapping("get")
    @Operation(summary ="用户动态详情")
    public AjaxResult<HgDynamicDto> get(String id) {
        return AjaxResult.success(iHgDynamicService.get(id));
    }

    @GetMapping("save")
    @Operation(summary ="用户动态保存")
    public AjaxResult<String> save(HgDynamicDto dto) {
        iHgDynamicService.save(dto);
        return AjaxResult.success();
    }

    @GetMapping("delete")
    @Operation(summary ="用户动态删除")
    public AjaxResult<String> delete(String id) {
        iHgDynamicService.delete(id);
        return AjaxResult.success();
    }
}
