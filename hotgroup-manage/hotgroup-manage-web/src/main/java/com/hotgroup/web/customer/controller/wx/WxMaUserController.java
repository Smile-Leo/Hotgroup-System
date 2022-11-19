package com.hotgroup.web.customer.controller.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.google.common.collect.Sets;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.core.domain.model.LoginUser;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.framework.service.TokenService;
import com.hotgroup.manage.api.IHotgroupUserLoginService;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.framework.service.WxMaConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user/{appid}")
@Api(tags = "小程序用户相关")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    TokenService tokenService;
    @Resource
    IHotgroupUserLoginService userLoginService;



    @GetMapping("login")
    @ApiOperation("小程序登陆")
    @Validated
    public AjaxResult<?> login(@PathVariable String appid, @NotBlank String code, @NotBlank String signature,
                               @NotBlank String rawData, @NotBlank String encryptedData, @NotBlank String iv) {

        try {
            final WxMaService wxService = WxMaConfiguration.getMaService(appid);

            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            String sessionKey = session.getSessionKey();
            // 用户信息校验
            if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                return AjaxResult.error("user check failed");
            }
            // 解密用户信息
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            // 解密
            WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
            HgUser hgUser = HgUser.builder()
                    .phone(phoneNoInfo.getPurePhoneNumber())
                    .headImg(userInfo.getAvatarUrl())
                    .openId(userInfo.getOpenId())
                    .unionId(userInfo.getUnionId())
                    .gender(Integer.parseInt(userInfo.getGender()))
                    .userName(userInfo.getNickName())
                    .build();

            IUser login = userLoginService.login(hgUser);
            String token = tokenService.createToken(new LoginUser(login, Sets.newHashSet()));
            return AjaxResult.success(token);

        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 登陆接口
     */
//    @GetMapping("/login2")
    public WxMaJscode2SessionResult login2(@PathVariable String appid, String code) {


        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            return session;
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }

    }



    @ApiOperation("获取用户信息接口")
    @GetMapping("/info")
    public WxMaUserInfo info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new IllegalArgumentException("user check failed");
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return userInfo;
    }


    @GetMapping("/phone")
    @ApiOperation("获取用户绑定手机号信息")
    public WxMaPhoneNumberInfo phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new IllegalArgumentException("user check failed");
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return phoneNoInfo;
    }

}
