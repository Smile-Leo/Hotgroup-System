package com.hotgroup.customer.api;

import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.manage.domain.entity.HgUser;

/**
 * @author Lzw
 * @date 2022/5/29.
 */
public interface IHotgroupUserLoginService {

    IUser login(HgUser user);
}
