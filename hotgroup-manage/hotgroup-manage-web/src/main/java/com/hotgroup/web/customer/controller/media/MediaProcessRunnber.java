package com.hotgroup.web.customer.controller.media;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.commons.media.Convert;
import com.hotgroup.commons.media.MediaService;
import com.hotgroup.commons.storage.FileStorageService;
import com.hotgroup.commons.storage.MediaTypeEnum;
import com.hotgroup.manage.api.IHgConstant;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.domain.entity.HgUserVideoAudit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * @author Lzw
 * @date 2022/5/31.
 */
@Slf4j
@RequiredArgsConstructor
public class MediaProcessRunnber implements Runnable {

    private final MediaService mediaService;
    private final FileStorageService storageService;
    private final HgUserVideoAudit videoAudit;
    private final IHgUserVideoAuditService videoAuditService;
    private final MultipartFile file;


    @Override
    public void run() {
        try {
            Convert convert = mediaService.commandConvert(file.getInputStream());
            do {
                log.debug("进度:" + convert.getProgress());
                TimeUnit.SECONDS.sleep(5);
                videoAuditService.update(null, Wrappers.lambdaUpdate(HgUserVideoAudit.class)
                        .eq(HgUserVideoAudit::getId, videoAudit.getId())
                        .set(HgUserVideoAudit::getProcess, convert.getProgress()));
            } while (convert.isDone());

            String gif = storageService.add(StringUtils.EMPTY, MediaTypeEnum.GIF, convert.gif());
            String add = storageService.add(file.getName(), MediaTypeEnum.FLV, convert.inputStream());

            videoAudit.setProcess(100);
            videoAudit.setProcessMsg(convert.getError());
            videoAudit.setCoverImg(gif);
            videoAudit.setUrl(add);
            videoAudit.setStatus(IHgConstant.DateStatus.enable);
            videoAudit.setAuditStatus(IHgConstant.AuditStatus.await);

            videoAuditService.updateById(videoAudit);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                Files.deleteIfExists(file.getResource().getFile().toPath());
            } catch (IOException ignored) {
            }
        }
    }
}
