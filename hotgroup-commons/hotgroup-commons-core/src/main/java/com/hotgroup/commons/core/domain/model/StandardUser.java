package com.hotgroup.commons.core.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Lzw
 * @date 2022/11/26.
 */
@Data
@Schema(title = "标准登陆用户信息")
public class StandardUser implements IUser, IUserExt {
    @Schema(title = "用户Id")
    private String id;
    @Schema(title = "用户呢称")
    private String nickName;
    @Schema(title = "头像")
    private String photo;
    @Schema(title = "等级")
    private Integer level;

    private String password;

    public StandardUser(String id, String nickName, String password) {
        this.id = id;
        this.nickName = nickName;
        this.password = password;
    }

    public StandardUser(String id, String nickName, Integer level, String photo) {
        this.id = id;
        this.nickName = nickName;
        this.level = level;
    }

    public StandardUser(String id, String nickName, Integer level) {
        this.id = id;
        this.nickName = nickName;
        this.photo = photo;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return "1L".equals(id);
    }

    @Override
    public Integer getLevel() {
        return level;
    }
}
