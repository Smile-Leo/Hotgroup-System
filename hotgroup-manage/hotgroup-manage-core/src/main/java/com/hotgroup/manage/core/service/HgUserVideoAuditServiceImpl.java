package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.database.page.PageHelper;
import com.hotgroup.manage.api.IHgUserInfoAuditService;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.core.mapper.HgUserInfoAuditMapper;
import com.hotgroup.manage.core.mapper.HgUserMapper;
import com.hotgroup.manage.core.mapper.HgUserVideoMapper;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import com.hotgroup.manage.domain.entity.HgUserVideo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户资料审核 业务层处理
 *
 * @author ajm
 */
@Service
public class HgUserVideoAuditServiceImpl implements IHgUserVideoAuditService {

    @Autowired
    HgUserVideoMapper hgUserVideoMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public AjaxResult<List<HgUserVideo>> pageList(HgUserVideo hgUserVideo) {
        Page<HgUserVideo> page = hgUserVideoMapper.selectPage(PageHelper.getPage(hgUserVideo), Wrappers.lambdaQuery(hgUserVideo));
        return AjaxResult.page(page);
    }

    @Override
    @Transactional
    public void audit(HgUserVideo hgUserVideo) {
        HgUserVideo video = hgUserVideoMapper.selectById(hgUserVideo.getId());
        if (video != null) {
            video.setAuditStatus(hgUserVideo.getAuditStatus());
            hgUserVideoMapper.updateById(video);
        }
    }
}
