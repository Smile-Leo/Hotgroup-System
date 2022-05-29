package com.hotgroup.commons.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Lzw
 * @date 2022/4/17.
 */
public interface FileStorageService {


    String add(String fileName, MediaTypeEnum fileType, InputStream inputStream) throws IOException;

    void remove(String url) throws IOException;

    InputStream get(String url) throws IOException;

    long getSize(String url) throws IOException;


    List<String> list() throws IOException;
}
