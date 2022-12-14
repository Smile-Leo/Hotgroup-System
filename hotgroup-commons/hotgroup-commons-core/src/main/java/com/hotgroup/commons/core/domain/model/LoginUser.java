package com.hotgroup.commons.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * 登录用户身份权限
 *
 * @author Lzw
 */
@Data
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Set<String> permissions;

    private StandardUser user;

    private UserType type;

    public LoginUser() {
    }

    public LoginUser(StandardUser user, Set<String> permissions, UserType type) {
        this.user = user;
        this.permissions = permissions;
        this.type = type;
    }

    public LoginUser(IUser user, Set<String> permissions, UserType type) {
        this.permissions = permissions;
        this.user = new StandardUser(user.getId(), user.getNickName(), user.getPassword());
        this.type = type;
    }

    public LoginUser(IUser user, IUserExt ext, Set<String> permissions, UserType type) {
        this.permissions = permissions;
        this.user = new StandardUser(user.getId(), user.getNickName(), ext.getLevel(), user.getPhoto());
        this.type = type;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
}
