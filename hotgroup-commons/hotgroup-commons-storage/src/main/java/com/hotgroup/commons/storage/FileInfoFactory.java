package com.hotgroup.commons.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Lzw
 * @date 2022/4/21.
 */
@AllArgsConstructor
@Slf4j
public class FileInfoFactory {

    final String defaultBucket;

    public FileInfo ofMedia(@NonNull MediaTypeEnum type) {
        String path = "/" + type.name().toLowerCase(Locale.ROOT) + LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE) + "/";
        String object = path + StringUtils.replace(UUID.randomUUID().toString(), "-", "");
        return new FileInfo(object, path, StringUtils.replace(defaultBucket + "/" + object, "//", "/"));
    }

    public FileInfo ofId(@NonNull String id) {
        String object = StringUtils.substringAfterLast(id, defaultBucket);
        String path = StringUtils.substringBeforeLast(object, "/");
        return new FileInfo(object, path, id);
    }

}
