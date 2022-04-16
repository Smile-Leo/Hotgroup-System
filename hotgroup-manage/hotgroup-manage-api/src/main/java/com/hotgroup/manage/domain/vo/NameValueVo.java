package com.hotgroup.manage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lzw
 * @date 2021/4/27.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NameValueVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private long value;
}
