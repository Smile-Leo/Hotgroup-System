package com.hotgroup.manage.core.mapping;

import com.hotgroup.manage.domain.dto.HgUserExtensionDto;
import com.hotgroup.manage.domain.entity.HgUserExtension;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * 用户扩展 转换接口
 *
 * @author ajm
 */
@Mapper
public interface HgUserExtensionMapping {

    HgUserExtensionMapping INSTANCE = Mappers.getMapper(HgUserExtensionMapping.class);

    HgUserExtensionDto toDTO(HgUserExtension hgUserExtension);

}
