package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.core.mapper.HgUserMapper;
import com.hotgroup.manage.domain.entity.HgUser;
import org.springframework.stereotype.Service;

/**
 * @author Lzw
 * @date 2022/6/1.
 */
@Service
public class HgUserServiceImpl extends ServiceImpl<HgUserMapper, HgUser> implements IHgUserService {
}
