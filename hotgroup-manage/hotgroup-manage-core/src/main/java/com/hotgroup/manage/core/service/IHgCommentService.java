package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.manage.domain.dto.HgCommentDto;
import com.hotgroup.manage.domain.entity.HgComment;

import javax.transaction.Transactional;
import java.util.List;

public interface IHgCommentService {

    Page<HgComment> page(Integer pageNo, Integer pageSize, String dataId, Integer dataType, String userId);

    void save(HgCommentDto dto);

    HgCommentDto get(String id);

    void delete(String id);

}
