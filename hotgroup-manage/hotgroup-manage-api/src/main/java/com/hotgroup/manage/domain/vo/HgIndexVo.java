package com.hotgroup.manage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Lzw
 * @date 2022/12/1.
 */
@Data
@Schema(title = "首页对象")
public class HgIndexVo {

    @Schema(title = "用户Id")
    private String userId;

    @Schema(title = "用户名")
    private String nickName;

    @Schema(title = "头像")
    private String photo;

    @Schema(title = "用户热度")
    private Integer heat;

    @Schema(title = "视频id")
    private String videoId;

    @Schema(title = "视频地址")
    private String videoUrl;


}
