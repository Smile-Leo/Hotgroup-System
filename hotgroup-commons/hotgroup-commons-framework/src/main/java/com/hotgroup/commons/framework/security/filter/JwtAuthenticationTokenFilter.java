package com.hotgroup.commons.framework.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.framework.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * token过滤器 验证token有效性
 *
 * @author Lzw
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    public static final AjaxResult<String> ERROR = AjaxResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统繁忙,请稍后重新再试!");
    public static final AjaxResult<String> EXPIRE_ERR = AjaxResult.error(HttpStatus.FORBIDDEN.value(), "登陆用户超时,请重新登陆!");
    @Resource
    private TokenService tokenService;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {
        try {
            if (!tokenService.verifyToken(request)) {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.getWriter().print(objectMapper.writeValueAsString(EXPIRE_ERR));
                response.getWriter().flush();
                return;
            }

            LoginUser loginUser = tokenService.getLoginUser(request);
            if (Objects.isNull(loginUser) && StringUtils.isNotBlank(tokenService.getToken(request))) {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.getWriter().print(objectMapper.writeValueAsString(EXPIRE_ERR));
                response.getWriter().flush();
                return;
            }
            if (Objects.nonNull(loginUser) && Objects.isNull(SecurityUtils.getAuthentication())) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(objectMapper.writeValueAsString(ERROR));
            response.getWriter().flush();
            return;
        }
        chain.doFilter(request, response);
    }


}
