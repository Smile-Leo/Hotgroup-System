package com.hotgroup.commons.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lzw
 * @date 2022/4/17.
 */
public interface FileStorageService {


    String add(String fileName, MediaTypeEnum fileType, InputStream inputStream) throws IOException;

    String add(String fileName, MediaTypeEnum fileType, MultipartFile file) throws IOException;

    void remove(String url) throws IOException;

    InputStream get(String url) throws IOException;

    long getSize(String url) throws IOException;

}
