package com.hotgroup.manage.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lzw
 * @date 2021/4/27.
 */
@Data
public class SysMenuParamDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String menuName;

    private String menuType;

    private String orderNum;
}
