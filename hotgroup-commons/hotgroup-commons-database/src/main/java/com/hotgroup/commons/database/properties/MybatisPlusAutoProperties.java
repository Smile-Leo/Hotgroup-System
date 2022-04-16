package com.hotgroup.commons.database.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mybatis-plus 配置
 *
 * @author zlt
 * @date 2020/4/5
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lead.mybatis-plus.auto-fill")
public class MybatisPlusAutoProperties {
    /**
     * 是否开启自动填充字段
     */
    private Boolean enabled = true;
    /**
     * 是否开启了插入填充
     */
    private Boolean enableInsertFill = true;
    /**
     * 是否开启了更新填充
     */
    private Boolean enableUpdateFill = true;
    /**
     * 创建时间字段名
     */
    private String createTimeField = "createTime";
    /**
     * 更新时间字段名
     */
    private String updateTimeField = "updateTime";

    /**
     * 创建人字段名
     */
    private String createByField = "createBy";
    /**
     * 更新人字段名
     */
    private String updateByField = "updateBy";
}
