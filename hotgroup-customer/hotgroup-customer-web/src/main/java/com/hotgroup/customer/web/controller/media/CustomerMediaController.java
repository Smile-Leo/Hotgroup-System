package com.hotgroup.customer.web.controller.media;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.media.MediaService;
import com.hotgroup.commons.storage.FileStorageService;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.domain.entity.HgUserVideoAudit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Lzw
 * @date 2022/5/15.
 */
@RestController
@RequestMapping("us/media")
@Slf4j
@Api(tags = "用户视频")
public class CustomerMediaController {

    @Autowired
    @Lazy
    FileStorageService storageService;
    @Autowired
    MediaService mediaService;
    @Autowired
    IHgUserVideoAuditService videoAuditService;

    @ApiOperation("上传")
    @PostMapping("uplaod")
    public AjaxResult<?> upload(MultipartFile file, @Validated @NotBlank String name) throws IOException {

        HgUserVideoAudit entity = new HgUserVideoAudit();
        entity.setName(name);
        entity.setUserId(SecurityUtils.getUsername());
        videoAuditService.save(entity);
        CompletableFuture.runAsync(
                new MediaProcessRunnber(mediaService, storageService, entity, videoAuditService, file));

        return AjaxResult.success();
    }

}
