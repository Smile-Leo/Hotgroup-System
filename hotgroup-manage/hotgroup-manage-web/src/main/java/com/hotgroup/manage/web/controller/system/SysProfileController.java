package com.hotgroup.manage.web.controller.system;

import com.lead.common.annotation.Log;
import com.lead.common.config.LeadConfig;
import com.lead.common.constant.UserConstants;
import com.lead.common.core.controller.BaseController;
import com.lead.common.core.domain.AjaxResult;
import com.lead.common.core.domain.entity.SysUser;
import com.lead.common.core.domain.model.LoginUser;
import com.lead.common.enums.BusinessType;
import com.lead.common.event.bus.BusEventRote;
import com.lead.common.event.bus.WecomBusEvent;
import com.lead.common.utils.SecurityUtils;
import com.lead.common.utils.ServletUtils;
import com.lead.common.utils.StringUtils;
import com.lead.common.utils.file.FileUploadUtils;
import com.lead.framework.web.service.TokenService;
import com.lead.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("sysDepts", userService.selectUserDeptNameByUserId(user));
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        ajax.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@Validated @RequestBody SysUser user) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser == null || loginUser.getUser() == null) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，用户未登陆");
        }
        user.setUserId(loginUser.getUser().getUserId());

        if (StringUtils.isEmpty(user.getPhonenumber()) && StringUtils.isEmpty(user.getEmail())) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码和邮箱不能同时为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }

        //防止修改账号
        user.setUserName(null);
        user.setUpdateBy(SecurityUtils.getUsername());
        userService.updateUserProfile(user);
        // 更新缓存用户信息
        loginUser.getUser().setNickName(user.getNickName());
        loginUser.getUser().setPhonenumber(user.getPhonenumber());
        loginUser.getUser().setEmail(user.getEmail());
        loginUser.getUser().setSex(user.getSex());
        tokenService.setLoginUser(loginUser);

        SysUser sysUser1 = userService.selectUserById(user.getUserId());
        eventPublisher.publishEvent(
                WecomBusEvent.builder().type(BusEventRote.LOCAL_ROLE_UPDATA_USER).data(sysUser1).build());

        return AjaxResult.success();

    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
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
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(LeadConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
