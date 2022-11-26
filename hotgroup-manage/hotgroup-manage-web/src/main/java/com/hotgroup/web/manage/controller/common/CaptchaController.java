package com.hotgroup.web.manage.controller.common;

import com.google.code.kaptcha.Producer;
import com.hotgroup.commons.core.constant.Constants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.IdUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author Lzw
 */
@RestController
@Tag(name = "验证码")
@RequestMapping("common/captcha")
public class CaptchaController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    private Redisson redisson;

    // 验证码类型
    @Value("${lead.captchaType:char}")
    private String captchaType;

    /**
     * 生成验证码
     */
    @GetMapping("create")
    @Operation(summary ="生成base64验证码")
    public AjaxResult<?> getCode(HttpServletResponse response) {

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisson.getBucket(verifyKey).set(code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", new String(Base64.getEncoder().encode(os.toByteArray())));
        return AjaxResult.success(map);
    }
}
