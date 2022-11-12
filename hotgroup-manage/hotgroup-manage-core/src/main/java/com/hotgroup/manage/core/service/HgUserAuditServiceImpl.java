package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.database.page.PageHelper;
import com.hotgroup.manage.api.IHgUserAuditService;
import com.hotgroup.manage.core.mapper.HgUserInfoAuditMapper;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author ajm
 */
@Service
@Slf4j
public class HgUserAuditServiceImpl implements IHgUserAuditService {

    @Resource
    HgUserInfoAuditMapper hgUserInfoAuditMapper;

    @Override
    public AjaxResult<List<HgUserInfoAudit>> pageUserList(HgUserInfoAudit hgUserInfoAudit) {
        Page<HgUserInfoAudit> userPage = hgUserInfoAuditMapper.selectPage(PageHelper.getPage(hgUserInfoAudit), Wrappers.lambdaQuery(hgUserInfoAudit));
        return AjaxResult.page(userPage);
    }
}
