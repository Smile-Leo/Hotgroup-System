package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import com.hotgroup.manage.api.ISysRoleService;
import com.hotgroup.manage.api.ISysUserService;
import com.hotgroup.manage.domain.entity.SysRole;
import com.hotgroup.manage.domain.entity.SysUser;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理用户信息
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "管理员管理")
public class SysUserController {
    @Resource
    private ISysUserService userService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("list")
    public AjaxResult<List<SysUser>> list(SysUser user) {

        return userService.pageUserList(user);
    }


    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:info')")
    @GetMapping("info")
    public AjaxResult<SysUser> getInfo(@Validated @NotNull String userId) {
        SysUser data = userService.selectUserById(userId);
        return AjaxResult.success(data);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping("add")
    public AjaxResult<?> add(@Validated(value = {InsertGroup.class}) @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        user.setUserId(null);
        userService.insertUser(user);
        return AjaxResult.success();
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("edit")
    public AjaxResult<?> edit(@Validated @RequestBody SysUser user) {
        SysUser sysUser = userService.selectUserById(user.getUserId());
        if (sysUser == null) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，账号不存在");
        }

        userService.checkUserAllowed(user);
        if (StringUtils.isEmpty(user.getPhone())) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }

        //防止修改账号
        user.setUserName(sysUser.getUserName());
        //不更新密码
        user.setPassword(sysUser.getPassword());

        userService.updateSysUser(user);
        return AjaxResult.success();
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @PostMapping("remove")
    public AjaxResult<?> remove(@Validated @NotEmpty String[] userIds) {
        userService.deleteUserByIds(userIds);
        return AjaxResult.success();
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @PostMapping("/resetPwd")
    public AjaxResult<?> resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.resetPwd(user);
        return AjaxResult.success();
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:changeStatus')")
    @PostMapping("/changeStatus")
    public AjaxResult<?> changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.updateUserStatus(user);
        return AjaxResult.success();
    }

}
