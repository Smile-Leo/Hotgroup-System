package com.hotgroup.commons.framework.exception;

import com.hotgroup.commons.core.constant.HttpStatus;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author Lzw
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 业务参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public AjaxResult businessException(IllegalArgumentException e) {
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 参数绑定异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult missingParmeter(MissingServletRequestParameterException e) {
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public AjaxResult handlerNoFoundException(NoHandlerFoundException e) {
        return AjaxResult.error(HttpStatus.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAuthorizationException(AccessDeniedException e) {
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public AjaxResult handleAccountExpiredException(AccountExpiredException e) {
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public AjaxResult handleUsernameNotFoundException(UsernameNotFoundException e) {
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult validatedBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        if (Objects.nonNull(fieldError)) {
            return AjaxResult.error("参数有误:" + fieldError.getField());
        }
        return AjaxResult.error("解析参数异常");
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 请求方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult notSupported(HttpRequestMethodNotSupportedException e) {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 参数类型异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object paramsExceptionHandler(MethodArgumentTypeMismatchException e) {
        return AjaxResult.error(HttpStatus.BAD_REQUEST, "非法参数");
    }


    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error(HttpStatus.ERROR, e.getMessage());
    }
}
