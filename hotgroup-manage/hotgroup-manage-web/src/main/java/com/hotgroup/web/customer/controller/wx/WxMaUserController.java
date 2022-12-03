package com.hotgroup.web.customer.controller.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.google.common.collect.Sets;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.core.domain.model.IUserExt;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.model.UserType;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.IHotgroupUserLoginService;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.framework.service.WxMaConfiguration;
import com.hotgroup.manage.framework.service.WxMaProperties;
import com.hotgroup.web.vo.WxMaLoginVo;
import com.hotgroup.web.vo.WxUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user")
@Tag(name = "小程序用户相关")
@Slf4j
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
    @PostMapping("login")
    @Operation(summary = "code注册并登陆")
    public AjaxResult<WxMaLoginVo> login(@Validated @RequestBody WxUserInfoVo vo) throws WxErrorException {
        String code = vo.getCode();
        String rawData = vo.getRawData();
        String signature = vo.getSignature();
        String encryptedData = vo.getEncryptedData();
        String iv = vo.getIv();
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
        WxMaLoginVo wxMaLoginVO = new WxMaLoginVo();
        String sessionKey = session.getSessionKey();
        wxMaLoginVO.setSessionKey(sessionKey);
        IUser user = StringUtils.isNotBlank(session.getUnionid()) ?
                userLoginService.getUserByUnionId(session.getUnionid()) :
                userLoginService.getUserByOpenid(session.getOpenid());

        if (Objects.isNull(user)) {
            // 用户信息校验
            if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                throw new IllegalArgumentException("user check failed");
            }
            // 解密用户信息
            WxMaUserInfo userInfo = wxService.getUserService()
                    .getUserInfo(sessionKey, encryptedData, iv);
            log.debug("userInfo->{}", userInfo.toString());
            HgUser hgUser = HgUser.builder()
                    .headImg(userInfo.getAvatarUrl())
                    .openId(session.getOpenid())
                    .unionId(session.getUnionid())
                    .gender(Integer.parseInt(userInfo.getGender()))
                    .userName(userInfo.getNickName())
                    .build();
            user = userLoginService.login(hgUser);
        }

        IUserExt userExtension = userLoginService.getUserExtension(user.getId());
        LoginUser loginUser = new LoginUser(user, userExtension, Sets.newHashSet(), UserType.WX);
        String token = tokenService.createToken(loginUser);
        wxMaLoginVO.setToken(token);

        return AjaxResult.success(wxMaLoginVO);
    }


    @PostMapping("phone")
    @Operation(summary = "获取用户绑定手机号信息")
    public WxMaPhoneNumberInfo phone(@Validated @RequestBody WxUserInfoVo vo) {
        String sessionKey = vo.getSessionKey();
        Assert.hasText(sessionKey, "sessionKey不能为空");
        String rawData = vo.getRawData();
        String signature = vo.getSignature();
        String encryptedData = vo.getEncryptedData();
        String iv = vo.getIv();
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
