package com.hotgroup.commons.framework.service;

import com.hotgroup.commons.core.constant.Constants;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.model.StandardUser;
import com.hotgroup.commons.core.domain.model.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * token验证处理
 *
 * @author Lzw
 */
@Component
public class TokenService {
    @Resource
    UserDetailsService userDetailsService;
    @Value("${token.header:Authorization}")
    private String header;
    @Value("${token.secret:2Avu0QmIiFmEdCQFmuRQvw==}")
    private String secret;
    @Value("${token.expireTime:30}")
    private int expireTime;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            //用户id
            String userId = claims.getId();
            //用户名
            String userName = claims.getSubject();
            //用户类型
            UserType type = UserType.valueOf(claims.getAudience());
            //权限
            Set<String> permissions = Stream.of(
                            StringUtils.split(claims.getIssuer(), ","))
                    .collect(Collectors.toSet());
            //等级
            Integer level = claims.get("level", Integer.class);
            StandardUser standardUser = new StandardUser(userId, userName, level);
            return new LoginUser(standardUser, permissions, type);
        }
        return null;
    }


    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        DefaultClaims claims = new DefaultClaims();
        //用户id
        claims.setId(loginUser.getUser().getId());
        //用户名
        claims.setSubject(loginUser.getUsername());
        //用户类型
        claims.setAudience(loginUser.getType().name());
        //权限
        claims.setIssuer(String.join(",", loginUser.getPermissions()));
        //等级
        claims.put("level", loginUser.getUser().getLevel());

        return createToken(claims);
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Claims claims) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(expireTime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.substring(Constants.TOKEN_PREFIX.length());
        }
        return token;
    }

    public boolean verifyToken(HttpServletRequest request) {
        try {
            String token = getToken(request);
            if (StringUtils.isNotBlank(token)) {
                Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            }
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
