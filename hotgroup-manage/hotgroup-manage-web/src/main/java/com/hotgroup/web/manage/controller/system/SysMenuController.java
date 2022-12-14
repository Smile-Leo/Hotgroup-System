package com.hotgroup.web.manage.controller.system;

import com.hotgroup.commons.core.constant.Constants;
import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.ISysMenuService;
import com.hotgroup.manage.domain.entity.SysMenu;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/system/menu")
@Tag(name = "菜单信息")
public class SysMenuController {
    @Resource
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("list")
    @Operation(summary ="列表")
    public AjaxResult<?> list(SysMenu menu,
                              @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        String userId = loginUser.getUser().getId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "info")
    @Operation(summary ="信息")
    public AjaxResult<SysMenu> getInfo(String menuId) {
        SysMenu data = menuService.selectMenuById(menuId);
        return AjaxResult.success(data);
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("treeselect")
    @Operation(summary ="树结构")
    public AjaxResult<?> treeselect(SysMenu menu,
                                    @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        String userId = loginUser.getUser().getId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect")
    @Operation(summary ="角色对应树结构")
    public AjaxResult<?> roleMenuTreeselect(String roleId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getId());
        Map<String, Object> ajax = new HashMap<String, Object>();

        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return AjaxResult.success(ajax);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping("add")
    @Operation(summary ="新增")
    public AjaxResult<?> add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) &&
                !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menuService.insertMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("edit")
    @Operation(summary ="修改")
    public AjaxResult<?> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.startsWithAny(menu.getPath(),
                Constants.HTTP, Constants.HTTPS)) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @PostMapping("remove")
    @Operation(summary ="删除")
    public AjaxResult<?> remove(String menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        menuService.deleteMenuById(menuId);
        return AjaxResult.success();
    }
}