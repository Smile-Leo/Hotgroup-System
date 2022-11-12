package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
//@RestController
//@RequestMapping("/system/user")
@Api(tags = "管理员管理")
public class SysUserController {
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysRoleService roleService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("list")
    public AjaxResult<?> list(SysUser user) {
        return userService.pageUserList(user);
    }


    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("info")
    public AjaxResult<?> getInfo(@Validated @NotNull String userId) {
        Map<String, Object> ajax = new HashMap<>(2);
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream()
                .filter(r -> !r.isAdmin())
                .collect(Collectors.toList()));
        SysUser sysUser = userService.selectUserByAuth(userId);
        Assert.notNull(sysUser, "用户不存在");
        ajax.put("user", sysUser);
        ajax.put("roleIds", roleService.selectRoleListByUserId(userId));

        return AjaxResult.success(ajax);
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
        if (StringUtils.isEmpty(user.getPassword())) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，密码不能为空");
        }

        user.setUserId(null);
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        int i = userService.insertUser(user);

        return AjaxResult.success();
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("edit")
    public AjaxResult<?> edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isEmpty(user.getPhone())) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }


        //防止修改账号
        SysUser sysUser = userService.selectUserById(user.getUserId());
        if (sysUser == null) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，账号不存在");
        }
        user.setUserName(sysUser.getUserName());
        user.setUpdateBy(SecurityUtils.getUsername());

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
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        int i = userService.resetPwd(user);
        return AjaxResult.success();
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/changeStatus")
    public AjaxResult<?> changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        int i = userService.updateUserStatus(user);
        return AjaxResult.success();
    }

}
