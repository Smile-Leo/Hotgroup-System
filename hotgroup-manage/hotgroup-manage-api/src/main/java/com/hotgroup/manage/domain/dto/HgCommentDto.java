package com.hotgroup.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author ajm
 * @date 2022/11/26.
 */
@Data
@Schema(title = "用户评论")
public class HgCommentDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @Schema(title = "id")
    private String id = "";

    @Schema(title = "动态内容")
    private String content;

    @Schema(title = "用户id")
    private String userId;

    @Schema(title = "数据类型")
    private Integer dataType;

    @Schema(title = "数据来源id")
    private String dataId;

    @Schema(title = "父id")
    private String parentId;

    @Schema(title = "点赞数")
    private Integer likeNum;
}
