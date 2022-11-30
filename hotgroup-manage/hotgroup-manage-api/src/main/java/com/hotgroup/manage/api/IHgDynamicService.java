package com.hotgroup.manage.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.manage.domain.dto.HgDynamicDto;
import com.hotgroup.manage.domain.entity.HgDynamic;

/**
 * 用户动态 业务层
 *
 * @author ajm
 */
public interface IHgDynamicService {


    /**
     * 根据条件分页查询用户动态
     *
     * @return 用户动态信息集合信息
     */
    Page<HgDynamic> page(String userId, Integer pageNo, Integer pageSize);

    /**
     * 保存
     * @param dto
     */
    void save(HgDynamicDto dto);

    /**
     * 获取详情
     * @param id
     * @return
     */
    HgDynamicDto get(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);
}
