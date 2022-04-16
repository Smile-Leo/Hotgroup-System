package com.hotgroup.commons.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lzw
 * @date 2021/5/8.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboBoxVo {

    private Object id;
    private String label;
    private Object value;

}
