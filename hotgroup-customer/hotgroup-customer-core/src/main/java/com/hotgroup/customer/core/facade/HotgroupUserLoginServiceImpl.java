package com.hotgroup.customer.core.facade;

import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.customer.api.IHotgroupUserLoginService;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.domain.entity.HgUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2022/5/29.
 */
@Component
public class HotgroupUserLoginServiceImpl implements IHotgroupUserLoginService {
    @Autowired
    IHgUserService hgUserService;

    @Override
    public IUser login(HgUser user) {

        hgUserService.saveOrUpdate(user);

        return user;
    }
}