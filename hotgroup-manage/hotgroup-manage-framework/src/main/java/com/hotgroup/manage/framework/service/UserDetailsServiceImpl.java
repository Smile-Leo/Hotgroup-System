package com.hotgroup.manage.framework.service;

import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.model.UserType;
import com.hotgroup.manage.api.ISysUserService;
import com.hotgroup.manage.domain.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户验证处理
 *
 * @author Lzw
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Resource
    private ISysUserService userService;

    @Resource
    private SysPermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = userService.selectUserByUserName(username);
        if (Objects.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, permissionService.getMenuPermission(user), UserType.SYS);
    }
}
