package com.hotgroup.customer.web.controller.index;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.customer.api.IHotgroupVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
@Api(tags = "首页")
@RestController
@RequestMapping("public/index")
@Slf4j
public class PublicMediaController {

    @Autowired
    IHotgroupVideoService videoService;

    @GetMapping("media")
    @ApiOperation("视频推荐")
    public AjaxResult<?> getMedia() {

        return AjaxResult.success(videoService.getSuggerVideo());
    }

}
