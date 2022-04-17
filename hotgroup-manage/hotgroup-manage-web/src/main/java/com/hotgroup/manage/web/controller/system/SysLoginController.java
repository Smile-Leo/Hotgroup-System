package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.ServletUtils;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.ISysMenuService;
import com.hotgroup.manage.domain.entity.SysMenu;
import com.hotgroup.manage.domain.entity.SysUser;
import com.hotgroup.manage.framework.service.SysLoginService;
import com.hotgroup.manage.framework.service.SysPermissionService;
import com.lead.common.core.domain.model.LoginBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author Lzw
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class SysLoginController {
    private final SysLoginService loginService;
    private final ISysMenuService menuService;
    private final SysPermissionService permissionService;
    private final TokenService tokenService;


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult<?> login(@RequestBody LoginBody loginBody) {
        Assert.hasText(loginBody.getUsername(), "请输入登陆信息");
        Assert.hasText(loginBody.getCode(), "请输入登陆信息");
        Assert.hasText(loginBody.getUuid(), "请输入登陆信息");

        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        return AjaxResult.success(token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult<?> getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = (SysUser) loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);

        HashMap<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roles", roles);
        map.put("permissions", permissions);
        return AjaxResult.success(map);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult<?> getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = (SysUser) loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }


}
