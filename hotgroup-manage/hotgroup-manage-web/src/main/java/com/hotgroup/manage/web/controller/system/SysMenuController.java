package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.constant.Constants;
import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.core.utils.ServletUtils;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.ISysMenuService;
import com.hotgroup.manage.domain.dto.SysMenuParamDto;
import com.hotgroup.manage.domain.entity.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.BeanMapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "菜单信息")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("list")
    @ApiOperation("列表")
    public AjaxResult<?> list(SysMenuParamDto menuDto) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userId = loginUser.getUser().getId();
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuDto, menu);
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "info")
    @ApiOperation("信息")
    public AjaxResult<?> getInfo(Long menuId) {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("treeselect")
    @ApiOperation("树结构")
    public AjaxResult<?> treeselect(SysMenuParamDto menuDto) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userId = loginUser.getUser().getId();
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuDto, menu);
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect")
    @ApiOperation("角色对应树结构")
    public AjaxResult<?> roleMenuTreeselect(Long roleId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
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
    @ApiOperation("新增")
    public AjaxResult<?> add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.startsWithAny(menu.getPath(),
                Constants.HTTP, Constants.HTTPS)) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        int i = menuService.insertMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("edit")
    @ApiOperation("修改")
    public AjaxResult<?> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.startsWithAny(menu.getPath(),
                Constants.HTTP, Constants.HTTPS)) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        int menu1 = menuService.updateMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @PostMapping("remove")
    @ApiOperation("删除")
    public AjaxResult<?> remove(Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        int i = menuService.deleteMenuById(menuId);
        return AjaxResult.success();
    }
}