package com.hotgroup.web.manage.controller.system;

import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.ISysUserService;
import com.hotgroup.manage.domain.entity.SysUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * 个人信息 业务处理
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
@Tag(name = "个人信息")
public class SysProfileController {
    private final ISysUserService userService;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    /**
     * 个人信息
     */
    @GetMapping("info")
    @Operation(summary ="info")
    public AjaxResult<?> profile(@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        return AjaxResult.success(loginUser.getUser());
    }

    /**
     * 修改用户
     */
    @PostMapping("edit")
    @Operation(summary ="修改")
    public AjaxResult<?> updateProfile(@Validated @RequestBody SysUser user,
                                       @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {

        user.setUserId(loginUser.getUser().getId());

        if (StringUtils.isEmpty(user.getPhone())) {
            return AjaxResult.error("修改用户'" + user.getNickName() + "'失败，手机号码为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getPhone() + "'失败，手机号码已存在");
        }

        //防止修改账号
        user.setUserName(null);
        userService.updateUserProfile(user);
        // 更新token
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUser.getUsername());
        String token = tokenService.createToken((LoginUser) userDetails);

        return AjaxResult.success(token);

    }

    /**
     * 重置密码
     */
    @PostMapping("updatePwd")
    @Operation(summary ="重置密码")
    public AjaxResult<?> updatePwd(String oldPassword, String newPassword,
                                   @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        userService.resetUserPwd(userName, newPassword);

        return AjaxResult.success();

    }

    /**
     * 头像上传
     */
    @PostMapping("avatar")
    @Operation(summary = "上传头像")
    public AjaxResult<?> avatar(@Validated @NotBlank String url,
                                @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) throws IOException {
        userService.updateUserAvatar(loginUser.getUser().getId(), url);
        return AjaxResult.success();

    }
}