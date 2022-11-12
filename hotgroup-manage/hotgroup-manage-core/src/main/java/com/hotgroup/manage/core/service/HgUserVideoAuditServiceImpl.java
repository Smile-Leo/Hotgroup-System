package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.database.page.PageHelper;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.core.mapper.HgUserVideoAuditMapper;
import com.hotgroup.manage.domain.entity.HgUserVideoAudit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户资料审核 业务层处理
 *
 * @author ajm
 */
@Service
public class HgUserVideoAuditServiceImpl extends ServiceImpl<HgUserVideoAuditMapper, HgUserVideoAudit> implements IHgUserVideoAuditService {

    @Resource
    HgUserVideoAuditMapper hgUserVideoAuditMapper;


    @Override
    public AjaxResult<List<HgUserVideoAudit>> pageList(HgUserVideoAudit hgUserVideoAudit) {
        Page<HgUserVideoAudit> page = hgUserVideoAuditMapper.selectPage(PageHelper.getPage(hgUserVideoAudit),
                Wrappers.lambdaQuery(
                        hgUserVideoAudit));
        return AjaxResult.page(page);
    }

    @Override
    @Transactional
    public void audit(HgUserVideoAudit hgUserVideoAudit) {
        HgUserVideoAudit video = hgUserVideoAuditMapper.selectById(hgUserVideoAudit.getId());
        if (video != null) {
            video.setAuditStatus(hgUserVideoAudit.getAuditStatus());
            hgUserVideoAuditMapper.updateById(video);
        }
    }


}
