package com.hotgroup.customer.framework.service;

import com.google.common.collect.Sets;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.core.domain.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author Lzw
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       /* if (Objects.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }*/

        return createLoginUser();
    }

    public UserDetails createLoginUser() {
        return new LoginUser(new IUser() {
            @Override
            public Long getId() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return null;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public boolean isAdmin() {
                return false;
            }
        }, Sets.newHashSet());
    }
}
