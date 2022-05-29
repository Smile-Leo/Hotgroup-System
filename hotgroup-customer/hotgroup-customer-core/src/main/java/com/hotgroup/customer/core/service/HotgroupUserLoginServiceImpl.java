package com.hotgroup.customer.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.customer.api.IHotgroupUserLoginService;
import com.hotgroup.manage.core.mapper.HgUserMapper;
import com.hotgroup.manage.domain.entity.HgUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2022/5/29.
 */
@Component
public class HotgroupUserLoginServiceImpl implements IHotgroupUserLoginService {
    @Autowired
    HgUserMapper hgUserMapper;

    @Override
    public IUser login(IUser user) {
        HgUser user1 = (HgUser) user;
        LambdaQueryWrapper<HgUser> wrapper = Wrappers.lambdaQuery(HgUser.class)
                .eq(StringUtils.isNotBlank(user1.getAccount()), HgUser::getAccount, user1.getAccount())
                .or()
                .eq(StringUtils.isNotBlank(user1.getPhone()), HgUser::getPhone, user1.getPhone())
                .or()
                .eq(StringUtils.isNotBlank(user1.getOpenId()), HgUser::getOpenId, user1.getOpenId())
                .or()
                .eq(StringUtils.isNotBlank(user1.getUnionId()), HgUser::getUnionId, user1.getUnionId());
        if (hgUserMapper.update(user1, wrapper) == 0) {
            hgUserMapper.insert(user1);
        }
        return user1;
    }
}
