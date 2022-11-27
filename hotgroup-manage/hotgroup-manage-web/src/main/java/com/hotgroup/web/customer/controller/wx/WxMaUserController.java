package com.hotgroup.web.customer.controller.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.google.common.collect.Sets;
import com.hotgroup.commons.core.domain.model.*;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.IHotgroupUserLoginService;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.framework.service.WxMaConfiguration;
import com.hotgroup.manage.framework.service.WxMaProperties;
import com.hotgroup.web.vo.WxMaLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user")
@Tag(name = "小程序用户相关")
public class WxMaUserController {

    @Resource
    TokenService tokenService;
    @Resource
    IHotgroupUserLoginService userLoginService;

    private final String appid;

    public WxMaUserController(WxMaProperties wxMaProperties) {
        this.appid = wxMaProperties.getConfigs().get(0).getAppid();
    }


    /**
     * 登陆接口
     */
    @GetMapping("login")
    @Operation(summary = "code登陆")
    public AjaxResult<WxMaLoginVO> login2(String code) throws WxErrorException {

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
        WxMaLoginVO wxMaLoginVO = new WxMaLoginVO();
        IUser user = userLoginService.getUserByUnionId(session.getUnionid());

        if (Objects.nonNull(user)) {
            IUserExt userExtension = userLoginService.getUserExtension(user.getId());
            LoginUser loginUser = new LoginUser(user, userExtension, Sets.newHashSet(), UserType.WX);
            String token = tokenService.createToken(loginUser);
            wxMaLoginVO.setToken(token);
        }
        wxMaLoginVO.setSessionKey(session.getSessionKey());
        wxMaLoginVO.setOpenid(session.getOpenid());
        wxMaLoginVO.setUnionid(session.getUnionid());
        return AjaxResult.success(wxMaLoginVO);
    }


    @Operation(summary = "注册用户信息并登陆")
    @GetMapping("regedit/login")
    @Validated
    public AjaxResult<WxMaLoginVO> regedit(@NotBlank String sessionKey, @NotBlank String signature,
                                           @NotBlank String rawData, @NotBlank String encryptedData,
                                           @NotBlank String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new IllegalArgumentException("user check failed");
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService()
                .getUserInfo(sessionKey, encryptedData, iv);
        HgUser hgUser = HgUser.builder()
//                .phone(phoneNoInfo.getPurePhoneNumber())
                .headImg(userInfo.getAvatarUrl())
                .openId(userInfo.getOpenId())
                .unionId(userInfo.getUnionId())
                .gender(Integer.parseInt(userInfo.getGender()))
                .userName(userInfo.getNickName())
                .build();

        IUser login = userLoginService.login(hgUser);
        WxMaLoginVO wxMaLoginVO = new WxMaLoginVO();
        IUserExt userExtension = userLoginService.getUserExtension(login.getId());
        LoginUser loginUser = new LoginUser(login, userExtension, Sets.newHashSet(), UserType.WX);
        String token = tokenService.createToken(loginUser);
        wxMaLoginVO.setToken(token);
        return AjaxResult.success(wxMaLoginVO);

    }


    @GetMapping("phone")
    @Operation(summary = "获取用户绑定手机号信息")
    public WxMaPhoneNumberInfo phone(String sessionKey, String signature,
                                     String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new IllegalArgumentException("user check failed");
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService()
                .getPhoneNoInfo(sessionKey, encryptedData, iv);

        return phoneNoInfo;
    }

}
