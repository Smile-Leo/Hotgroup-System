package com.hotgroup.commons.database.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.database.properties.MybatisPlusAutoProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Objects;

/**
 * 自定义填充公共字段
 *
 * @author Lzw
 * @date 2018/12/11
 * <p>
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

    private final MybatisPlusAutoProperties autoFillProperties;

    private final String IS_AUTO_UPDATA_Fill = "IS_AUTO_UPDATA_Fill";


    public DateMetaObjectHandler(MybatisPlusAutoProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object createBy = getFieldValByName(autoFillProperties.getCreateByField(), metaObject);
        if (createTime == null) {
            strictInsertFill(metaObject, autoFillProperties.getCreateTimeField(), Date.class, new Date());
        }
        if (null == createBy && Objects.nonNull(SecurityUtils.getAuthentication())) {
            strictInsertFill(metaObject, autoFillProperties.getCreateByField(), String.class,
                    SecurityUtils.getLoginUser().getUsername());
        }
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object isAutoUpdate = getFieldValByName(IS_AUTO_UPDATA_Fill, metaObject);
        if (null == isAutoUpdate) {
            //修复更新字段不更新问题
            setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
            if (Objects.nonNull(SecurityUtils.getAuthentication())) {
                setFieldValByName(autoFillProperties.getUpdateByField(), SecurityUtils.getLoginUser().getUsername(),
                        metaObject);
            }
        }

    }
}