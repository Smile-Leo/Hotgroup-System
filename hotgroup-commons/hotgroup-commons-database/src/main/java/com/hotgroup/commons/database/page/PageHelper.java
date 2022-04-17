package com.hotgroup.commons.database.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.database.domain.BaseEntity;

/**
 * 分页工具类,兼容mybatis-plus跟pagehelper
 *
 * @author Lzw
 */
public class PageHelper {

    /**
     * @param <T>
     * @param request
     * @return
     */
    public static <T> Page<T> getPage(BaseEntity entity) {
        Page<T> page = new Page<T>();
        page.setSize(entity.getPageSize());
        page.setCurrent(entity.getPageNum());
        return page;
    }


}
