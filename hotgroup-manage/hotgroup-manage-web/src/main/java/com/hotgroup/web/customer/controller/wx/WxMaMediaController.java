package com.hotgroup.web.customer.controller.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import com.google.common.collect.Lists;
import com.hotgroup.manage.framework.service.WxMaConfiguration;
import com.hotgroup.manage.framework.service.WxMaProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *  小程序临时素材接口
 *  Created by BinaryWang on 2017/6/16.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/media/")
@Tag(name = "微信素材")
public class WxMaMediaController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String appid;

    public WxMaMediaController(WxMaProperties wxMaProperties) {
        this.appid = wxMaProperties.getConfigs().get(0).getAppid();
    }

    /**
     * 上传临时素材
     *
     * @return 素材的media_id列表，实际上如果有的话，只会有一个
     */
    @PostMapping("/upload")
    public List<String> uploadMedia(HttpServletRequest request) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if (!resolver.isMultipart(request)) {
            return Lists.newArrayList();
        }

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multiRequest.getFileNames();
        List<String> result = Lists.newArrayList();
        while (it.hasNext()) {
            try {
                MultipartFile file = multiRequest.getFile(it.next());
                assert file != null;
                File newFile = Files.createTempFile(file.getName(), file.getContentType()).toFile();
                this.logger.info("filePath is ：" + newFile);
                file.transferTo(newFile);
                WxMediaUploadResult uploadResult = wxService.getMediaService()
                        .uploadMedia(WxMaConstants.KefuMsgType.IMAGE, newFile);
                this.logger.info("media_id ： " + uploadResult.getMediaId());
                result.add(uploadResult.getMediaId());
            } catch (IOException e) {
                this.logger.error(e.getMessage(), e);
            }
        }

        return result;
    }

    /**
     * 下载临时素材
     */
    @GetMapping("/download/{mediaId}")
    public File getMedia(@PathVariable String mediaId) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        return wxService.getMediaService().getMedia(mediaId);
    }
}
