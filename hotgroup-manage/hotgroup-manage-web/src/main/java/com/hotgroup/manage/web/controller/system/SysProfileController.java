package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.core.utils.ServletUtils;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.ISysUserService;
import com.hotgroup.manage.domain.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
public class SysProfileController {
    private final ISysUserService userService;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    /**
     * 个人信息
     */
    @GetMapping("info")
    public AjaxResult<?> profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = (SysUser) loginUser.getUser();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        return AjaxResult.success(map);
    }

    /**
     * 修改用户
     */
    @PostMapping("edit")
    public AjaxResult<?> updateProfile(@Validated @RequestBody SysUser user) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser == null || loginUser.getUser() == null) {
            return AjaxResult.error("修改用户'" + user.getUsername() + "'失败，用户未登陆");
        }
        user.setUserId(loginUser.getUser().getId());

        if (StringUtils.isEmpty(user.getPhonenumber()) && StringUtils.isEmpty(user.getEmail())) {
            return AjaxResult.error("修改用户'" + user.getUsername() + "'失败，手机号码和邮箱不能同时为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getPhonenumber() + "'失败，手机号码已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getEmail() + "'失败，邮箱账号已存在");
        }

        //防止修改账号
        user.setUsername(null);
        user.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        userService.updateUserProfile(user);
        // 更新token
        UserDetails userDetails = userDetailsService.loadUserByUsername(SecurityUtils.getLoginUser().getUsername());
        String token = tokenService.createToken((LoginUser) userDetails);

        return AjaxResult.success(token);

    }

    /**
     * 重置密码
     */
    @PostMapping("updatePwd")
    public AjaxResult<?> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @PostMapping("avatar")
    public AjaxResult<?> avatar(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//            String avatar = FileUploadUtils.upload(LeadConfig.getAvatarPath(), file);
//            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("imgUrl", avatar);
//                return ajax;
            return AjaxResult.success();
        }

        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}