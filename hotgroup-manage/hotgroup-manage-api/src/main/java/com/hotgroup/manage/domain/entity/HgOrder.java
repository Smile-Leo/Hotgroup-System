package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 团队用户关联表
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "hg_order")
@Entity
public class HgOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "Id自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_UUID)
    @Id
    private String id;

    /**
     * 团队id
     */
    @Column(name = "team_id", length = 11)
    private String teamId;

    /**
     * 用户id
     */
    @Column(name = "user_id", length = 11)
    private String userId;

    /**
     * 团队创建者id
     */
    @Column(name = "team_creator", length = 11)
    private String teamCreator;

    /**
     * 订单号
     */
    @Column(name = "order_number", length = 50)
    private String orderNumber;

    /**
     * 支付金额
     */
    @Column(name = "amount", length = 50)
    private BigDecimal amount;

    /**
     * 支付时间
     */
    @Column(name = "pay_time", length = 20)
    private String payTime;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    @Column(name = "status", length = 1)
    private Integer status;
}
