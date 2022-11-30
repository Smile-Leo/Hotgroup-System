package com.hotgroup.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ajm
 * @date 2022/11/26.
 */
@Data
@Schema(title = "用户动态")
public class HgDynamicDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @Schema(title = "id")
    private String id = "";

    @Schema(title = "动态内容")
    private String content = "";

    @Schema(title = "图片数组")
    private String imgs = "";

    @Schema(title = "用户id")
    private String userId = "";

    @Schema(title = "动态数")
    private Integer commentNum = 0;

    @Schema(title = "点赞数")
    private Integer likeNum = 0;

    @Schema(title = "浏览数")
    private Integer viewNum = 0;

    @Schema(title = "转发数")
    private Integer forwardNum = 0;
}
