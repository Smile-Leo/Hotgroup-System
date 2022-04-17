package com.hotgroup.commons.database.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lzw
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT) // 新增执行
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE) // 更新执行
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_Time", fill = FieldFill.UPDATE)
    private Date updateTime;


    @JsonIgnore
    @TableField(exist = false)
    private transient Integer pageNum = 0;

    @JsonIgnore
    @TableField(exist = false)
    private transient Integer pageSize = 20;

    @JsonIgnore
    @TableField(exist = false)
    private transient Map<String, Object> params = new HashMap<>();

}
