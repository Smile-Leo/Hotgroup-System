package com.hotgroup.web.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Lzw
 * @date 2022/12/3.
 */
@Data
public class WxPhoneVo {
    @NotBlank
    private String sessionKey;
    @NotBlank
    private String signature;
    @NotBlank
    private String rawData;
    @NotBlank
    private String encryptedData;
    @NotBlank
    private String iv;

}
