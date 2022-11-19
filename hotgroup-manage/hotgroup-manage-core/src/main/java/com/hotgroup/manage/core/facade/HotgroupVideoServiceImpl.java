package com.hotgroup.manage.core.facade;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.manage.api.IHgVideoService;
import com.hotgroup.manage.api.IHotgroupVideoService;
import com.hotgroup.manage.domain.entity.HgVideo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
@Component
public class HotgroupVideoServiceImpl implements IHotgroupVideoService {


    @Resource
    IHgVideoService hgVideoService;

    @Override
    public HgVideo getSuggerVideo() {
        return hgVideoService.getOne(Wrappers.lambdaQuery(HgVideo.class).last("ORDER BY RAND() limit 1"));
    }
}
