package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.database.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 参数配置表 sys_config
 *
 * @author Lzw
 */
@Data
@ToString
@Schema(title = "参数配置对象")
@EqualsAndHashCode(callSuper = true)
@TableName
public class SysConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(title = "id")
    private String configId;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    @Schema(title = "配置名")
    private String configName;

    /**
     * 参数键名
     */
    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    @Schema(title = "配置主键")
    private String configKey;

    /**
     * 参数键值
     */

    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    @Schema(title = "配置值")
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @Schema(title = "是否系统内置")
    private String configType;


}
