package com.hotgroup.commons.core.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Lzw
 * @date 2022/11/26.
 */
@Data
@Schema
public class StandardUser implements IUser {
    private String id;
    private String nickName;
    private String photo;
    private String password;

    public StandardUser(String id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public StandardUser(String id, String nickName, String photo) {
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
}
