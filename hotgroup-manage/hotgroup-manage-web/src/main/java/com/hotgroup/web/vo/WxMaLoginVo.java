package com.hotgroup.web.vo;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import lombok.Data;

/**
 * @author Lzw
 * @date 2022/11/25.
 */
@Data
public class WxMaLoginVo extends WxMaJscode2SessionResult {

    private String token;
}
