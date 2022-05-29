package com.hotgroup.commons.storage;

import lombok.Getter;

/**
 * @author Lzw
 * @date 2022/4/19.
 */
@Getter
public enum MediaTypeEnum {

    MP4("video/mpeg4"),
    MP3("audio/mp3"),
    JPEG("image/jpeg"),
    JPG("image/jpeg"),
    IMG("application/x-img"),
    TIF("image/tiff"),
    TIFF("image/tiff"),
    PDF("application/pdf"),
    PNG("image/png"),
    FLV("video/x-flv"),

    ZIP("application/x-zip-compressed"),
    FILE("application/octet-stream"),
    OTHER("application/octet-stream");

    final String contetType;

    MediaTypeEnum(String contentType) {
        this.contetType = contentType;
    }


}
