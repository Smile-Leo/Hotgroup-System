package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.manage.core.mapper.HgCommentMapper;
import com.hotgroup.manage.core.mapping.HgCommentMapping;
import com.hotgroup.manage.domain.dto.HgCommentDto;
import com.hotgroup.manage.domain.entity.HgComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Objects;

/**
 * 用户评论 业务层处理
 *
 * @author ajm
 */
@Service
@Slf4j
public class HgCommentServiceImpl implements IHgCommentService {

    @Resource
    HgCommentMapper hgCommentMapper;

    @Override
    public Page<HgComment> page(Integer pageNo, Integer pageSize, String dataId, Integer dataType, String userId) {
        Page<HgComment> page = new Page<>(pageNo, pageSize);
        return hgCommentMapper.selectPage(page,Wrappers.lambdaQuery(HgComment.class).eq(HgComment::getDataId, dataId)
                                    .eq(HgComment::getDataType,dataType));
    }

    @Override
    @Transactional
    public void save(HgCommentDto dto) {
        HgComment hgComment = HgCommentMapping.INSTANCE.toEntity(dto);
        hgCommentMapper.insert(hgComment);
    }

    @Override
    public HgCommentDto get(String id) {
        HgCommentDto hgCommentDto = new HgCommentDto();
        HgComment comment = hgCommentMapper.selectById(id);
        if (Objects.nonNull(comment)) {
            hgCommentDto = HgCommentMapping.INSTANCE.toDTO(comment);
        }
        return hgCommentDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        hgCommentMapper.deleteById(id);
    }
}
