package com.hotgroup.manage.core.mapping;

import com.hotgroup.manage.core.mapper.HgCommentMapper;
import com.hotgroup.manage.domain.dto.HgCommentDto;
import com.hotgroup.manage.domain.entity.HgComment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户评论 转换接口
 *
 * @author ajm
 */
@Mapper
public interface HgCommentMapping {

    HgCommentMapper INSTANCE = Mappers.getMapper(HgCommentMapper.class);

    HgCommentDto toDTO(HgComment hgComment);

    HgComment toEntity(HgCommentDto hgCommentDto);

}
