package com.hotgroup.manage.core.mapping;

import com.hotgroup.manage.domain.dto.HgDynamicDto;
import com.hotgroup.manage.domain.entity.HgDynamic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户动态 转换接口
 *
 * @author ajm
 */
@Mapper
public interface HgDynamicMapping {

    HgDynamicMapping INSTANCE = Mappers.getMapper(HgDynamicMapping.class);

    HgDynamicDto toDTO(HgDynamic hgDynamic);

    HgDynamic toEntity(HgDynamicDto dynamicDto);

}
