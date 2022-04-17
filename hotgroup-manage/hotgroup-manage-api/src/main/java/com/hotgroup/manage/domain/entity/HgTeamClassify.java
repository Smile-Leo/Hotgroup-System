package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 分类
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HgTeamClassify extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "Id自动生成", groups = InsertGroup.class)
    @TableId
    private Long id;

    /**
     * 用户id⽗级id 第⼀级为0
     */
    private Long parentId;

    /**
     * 封面
     */
    private String coverImg;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;
}
