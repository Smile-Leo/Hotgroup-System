package com.hotgroup.manage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Lzw
 * @date 2022/12/1.
 */
@Data
@Schema(title = "首页对象")
public class HomepageVo {
    @Schema(title = "用户等级")
    private Integer level;
    @Schema(title = "用户名")
    private String nickName;
    @Schema(title = "视频封面")
    private String coverImg;
    @Schema(title = "视频id")
    private String videoId;
}
