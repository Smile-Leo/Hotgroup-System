package com.hotgroup.commons.database.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Lzw
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT) // 新增执行
    @Column(name = "create_by", length = 20)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Column(name = "create_time", length = 20)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE) // 更新执行
    @Column(name = "update_by", length = 20)
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @Column(name = "update_time", length = 20)
    private Date updateTime;


    @JsonIgnore
    @TableField(exist = false)
    private transient Integer pageNum = 0;

    @JsonIgnore
    @TableField(exist = false)
    private transient Integer pageSize = 20;


}
