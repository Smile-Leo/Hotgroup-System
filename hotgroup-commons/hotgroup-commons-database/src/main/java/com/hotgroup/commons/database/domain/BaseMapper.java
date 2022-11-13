package com.hotgroup.commons.database.domain;

import java.util.Collection;

/**
 * @author Lzw
 * @date 2022/11/13.
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * 扩展批量插入方法
     *
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);

}
