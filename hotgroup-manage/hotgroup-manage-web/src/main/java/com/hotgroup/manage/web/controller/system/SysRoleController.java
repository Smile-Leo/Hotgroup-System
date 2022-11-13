package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.manage.api.ISysRoleService;
import com.hotgroup.manage.domain.entity.SysRole;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理")
public class SysRoleController {
    @Resource
    private ISysRoleService roleService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("list")
    public AjaxResult<?> list(SysRole role) {

        return roleService.selectRoleList(role);
    }


    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:info')")
    @GetMapping(value = "info")
    public AjaxResult<?> getInfo(String roleId) {

        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @PostMapping("add")
    public AjaxResult<?> add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        roleService.insertRole(role);
        return AjaxResult.success();

    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PostMapping("edit")
    public AjaxResult<?> edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        roleService.updateRole(role);
        return AjaxResult.success();
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PostMapping("dataScope")
    public AjaxResult<?> dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.authDataScope(role);
        return AjaxResult.success();
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PostMapping("/changeStatus")
    public AjaxResult<?> changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.updateRoleStatus(role);
        return AjaxResult.success();
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @PostMapping("remove")
    public AjaxResult<?> remove(String[] roleIds) {
        roleService.deleteRoleByIds(roleIds);
        return AjaxResult.success();
    }


}
