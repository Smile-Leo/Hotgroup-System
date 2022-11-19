package com.hotgroup.manage.core.facade;

import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.api.IHotgroupUserLoginService;
import com.hotgroup.manage.domain.entity.HgUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lzw
 * @date 2022/5/29.
 */
@Component
public class HotgroupUserLoginServiceImpl implements IHotgroupUserLoginService {
    @Resource
    IHgUserService hgUserService;

    @Override
    public IUser login(HgUser user) {

        hgUserService.saveOrUpdate(user);

        return user;
    }
}
