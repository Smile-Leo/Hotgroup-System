package com.hotgroup.commons.core.domain.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotgroup.commons.core.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lzw
 */
@Data
@ApiModel("通用返回对象")
public class AjaxResult<T> {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("状态码")
    private int code;
    @ApiModelProperty("提醒消息")
    private String msg;
    @ApiModelProperty("是否加密码消息：0未加密, 1已加密")
    private int encryption;
    @ApiModelProperty("数据对象")
    private T data;

    @ApiModelProperty("分页总数")
    private long total;

    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult<String> success() {
        return AjaxResult.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(T data) {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult<String> success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult<>(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult<String> error() {
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult<String> error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> AjaxResult<T> error(String msg, T data) {
        return new AjaxResult<>(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static AjaxResult<String> error(int code, String msg) {
        return new AjaxResult<>(code, msg, null);
    }

    /**
     * 根据结果返回信息
     *
     * @param flag
     * @return
     * @author Lzw
     */
    public static AjaxResult<String> result(boolean flag) {
        return flag ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 分页结果
     *
     * @param page
     * @return
     */
    public static <T> AjaxResult<List<T>> page(IPage<T> page) {
        AjaxResult<List<T>> success = AjaxResult.success(page.getRecords());
        success.setTotal(page.getTotal());
        return success;
    }

    public static <T> AjaxResult<List<T>> page(Integer total, List<T> data) {
        AjaxResult<List<T>> success = AjaxResult.success(data);
        success.setTotal(total);
        return success;
    }


}
