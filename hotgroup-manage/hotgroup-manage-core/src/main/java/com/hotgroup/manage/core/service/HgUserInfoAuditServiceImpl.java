package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.database.page.PageHelper;
import com.hotgroup.manage.api.IHgUserInfoAuditService;
import com.hotgroup.manage.core.mapper.HgUserInfoAuditMapper;
import com.hotgroup.manage.core.mapper.HgUserMapper;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

import java.util.List;

/**
 * 用户资料审核 业务层处理
 *
 * @author ajm
 */
@Service
public class HgUserInfoAuditServiceImpl implements IHgUserInfoAuditService {

    @Autowired
    HgUserInfoAuditMapper hgUserInfoAuditMapper;

    @Autowired
    HgUserMapper hgUserMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public AjaxResult<List<HgUserInfoAudit>> pageList(HgUserInfoAudit hgUserInfoAudit) {
        Page<HgUserInfoAudit> page = hgUserInfoAuditMapper.selectPage(PageHelper.getPage(hgUserInfoAudit), Wrappers.lambdaQuery(hgUserInfoAudit));
        return AjaxResult.page(page);
    }

    @Override
    @Transactional
    public void audit(HgUserInfoAudit hgUserInfoAudit) throws JsonProcessingException {
        hgUserInfoAuditMapper.updateById(hgUserInfoAudit);
        if (hgUserInfoAudit.getStatus() != null && hgUserInfoAudit.getStatus() == 1) {//审核成功
            HgUser oldUser = hgUserMapper.selectById(hgUserInfoAudit.getUserId());
            if (oldUser != null) {
                //BeanUtils.copyProperties(newUser,oldUser);
                hgUserMapper.updateById(oldUser);
            }
        }
    }
}
