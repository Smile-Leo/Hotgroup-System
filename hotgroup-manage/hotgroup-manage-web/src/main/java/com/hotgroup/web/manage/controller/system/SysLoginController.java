package com.hotgroup.web.manage.controller.system;

import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.model.StandardUser;
import com.hotgroup.commons.core.domain.model.UserType;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.domain.vo.LoginRequest;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.api.ISysMenuService;
import com.hotgroup.manage.api.ISysUserService;
import com.hotgroup.manage.domain.entity.SysMenu;
import com.hotgroup.manage.domain.vo.RouterVo;
import com.hotgroup.manage.framework.service.SysLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Collections;
import java.util.List;

/**
 * 登录验证
 *
 * @author Lzw
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "管理后台登陆")
public class SysLoginController {
    private final SysLoginService loginService;
    private final ISysMenuService menuService;
    private final ISysUserService sysUserService;
    private final IHgUserService hgUserService;


    /**
     * 登录方法
     *
     * @param loginRequest 登录信息
     * @return 结果
     */
    @Operation(summary ="账号密码登陆")
    @PostMapping("/login")
    public AjaxResult<?> login(@RequestBody LoginRequest loginRequest) {
        Assert.hasText(loginRequest.getUsername(), "请输入登陆信息");
        Assert.hasText(loginRequest.getCode(), "请输入登陆信息");
        Assert.hasText(loginRequest.getUuid(), "请输入登陆信息");

        // 生成令牌
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getCode(),
                loginRequest.getUuid());
        return AjaxResult.success(token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Operation(summary ="用户信息")
    @GetMapping("getInfo")
    public AjaxResult<StandardUser> getInfo(@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        IUser user = null;
        if (loginUser.getType().equals(UserType.SYS)) {
            user = sysUserService.selectUserById(loginUser.getUser().getId());
        }
        if (loginUser.getType().equals(UserType.WX)) {
            user = hgUserService.getById(loginUser.getUser().getId());
        }
        Assert.notNull(user, "未知用户");
        return AjaxResult.success(new StandardUser(user.getId(), user.getNickName(), user.getPhoto()));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @Operation(summary ="用户菜单")
    @GetMapping("getRouters")
    public AjaxResult<List<RouterVo>> getRouters(@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser.getType().equals(UserType.SYS)) {
            // 用户信息
            List<SysMenu> menus = menuService.selectMenuTreeByUserId(loginUser.getUser().getId());
            return AjaxResult.success(menuService.buildMenus(menus));
        }
        return AjaxResult.success(Collections.emptyList());
    }


    /**
     * 登出
     *
     * @return
     */
    @Operation(summary ="登出")
    @GetMapping("logout")
    public AjaxResult<?> logout() {
        return AjaxResult.success();
    }
}
