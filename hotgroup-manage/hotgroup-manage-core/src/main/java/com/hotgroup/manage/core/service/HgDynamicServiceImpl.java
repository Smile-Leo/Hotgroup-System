package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.manage.api.IHgDynamicService;
import com.hotgroup.manage.core.mapper.HgDynamicMapper;
import com.hotgroup.manage.core.mapping.HgDynamicMapping;
import com.hotgroup.manage.domain.dto.HgDynamicDto;
import com.hotgroup.manage.domain.entity.HgDynamic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Objects;

/**
 * 用户 业务层处理
 *
 * @author ajm
 */
@Service
@Slf4j
public class HgDynamicServiceImpl implements IHgDynamicService {

    @Resource
    HgDynamicMapper hgDynamicMapper;

    @Override
    public Page<HgDynamic> page(String userId, Integer pageNo, Integer pageSize) {
        Page<HgDynamic> page = new Page<>(pageNo, pageSize);
        hgDynamicMapper.selectPage(page, Wrappers.lambdaQuery(HgDynamic.class).eq(HgDynamic::getUserId,userId));
        return page;
    }

    @Override
    @Transactional
    public void save(HgDynamicDto dto) {
        HgDynamic hgDynamic = HgDynamicMapping.INSTANCE.toEntity(dto);
        hgDynamicMapper.updateById(hgDynamic);
    }

    @Override
    public HgDynamicDto get(String id) {
        HgDynamicDto hgDynamicDto = new HgDynamicDto();
        HgDynamic dynamic = hgDynamicMapper.selectById(id);
        if (Objects.nonNull(dynamic)) {
            hgDynamicDto = HgDynamicMapping.INSTANCE.toDTO(dynamic);
        }
        return hgDynamicDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        hgDynamicMapper.deleteById(id);
    }
}
