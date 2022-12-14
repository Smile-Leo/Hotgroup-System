package com.hotgroup.web.manage.controller.common;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.storage.FileStorageService;
import com.hotgroup.commons.storage.MediaTypeEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Files;

/**
 * 通用请求处理
 *
 * @author Lzw
 */
@RestController
@Tag(name = "文件操作")
@RequestMapping("common/file")
@Slf4j
public class CommonController {


    @Resource
    @Lazy
    FileStorageService storageService;


    /**
     * 通用上传请求
     */
    @PostMapping("upload")
    public AjaxResult<?> uploadFile(MultipartFile file) throws Exception {
        try {
            MediaTypeEnum mediaTypeEnum = MediaTypeEnum.valueOf(file.getContentType());
            String add = storageService.add(file.getName(), mediaTypeEnum, file.getInputStream());
            return AjaxResult.success(add);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        } finally {
            Files.deleteIfExists(file.getResource().getFile().toPath());
        }
    }

}
