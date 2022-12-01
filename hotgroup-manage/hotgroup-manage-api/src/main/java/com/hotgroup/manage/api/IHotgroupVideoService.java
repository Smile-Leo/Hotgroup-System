package com.hotgroup.manage.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.manage.domain.entity.HgVideo;
import com.hotgroup.manage.domain.vo.HgIndexVo;
import com.hotgroup.manage.domain.vo.HomepageVo;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
public interface IHotgroupVideoService {

    HgVideo getSuggerVideo();

    HgIndexVo index();

    Page<HomepageVo> homepage(Integer pageNo, Integer pageSize);
}
