package com.hotgroup.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ajm
 * @date 2022/11/26.
 */
@Data
@Schema(title = "用户信息")
public class HgUserExtensionDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @Schema(title = "用户id")
    private String userId = "";

    @Schema(title = "关注数")
    private Integer concernNum = 0;

    @Schema(title = "动态数")
    private Integer dynamicNum = 0;

    @Schema(title = "粉丝数")
    private Integer followersNum = 0;

    @Schema(title = "积分")
    private Integer points = 0;

    @Schema(title = "账号")
    private String account = "";

    @Schema(title = "用户名")
    private String userName = "";

    @Schema(title = "头像")
    private String headImg = "";

    @Schema(title = "背景")
    private String background = "";

    @Schema(title = "等级")
    private Integer level = 0;

    @Schema(title = "经验值")
    private Integer currentExperience = 0;

    @Schema(title = "升级所需经验值")
    private Integer levelUpExperience = 0;

    @Schema(title = "星钻数")
    private Integer starDiamondNum = 0;
}
