package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotgroup.manage.api.IHgVideoService;
import com.hotgroup.manage.core.mapper.HgVideoMapper;
import com.hotgroup.manage.domain.entity.HgVideo;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
@Component
public class HgVideoServiceImpl extends ServiceImpl<HgVideoMapper, HgVideo> implements IHgVideoService {
}
