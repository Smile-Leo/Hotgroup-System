package com.hotgroup.web.customer.controller.user;

import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.domain.dto.HgUserExtensionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author aijiaming
 * @date 2022/11/27.
 */
@Tag(name = "用户")
@RestController
@RequestMapping("us")
@Slf4j
public class UserController {

    @Resource
    IHgUserService userService;

    @GetMapping("info")
    @Operation(summary ="用户信息")
    public AjaxResult<HgUserExtensionDto> getUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        return AjaxResult.success(userService.getHgUserInfo(loginUser.getUser().getId()));
    }

}
