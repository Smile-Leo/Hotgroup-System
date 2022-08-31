package com.hotgroup.commons.media;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lzw
 * @date 2022/5/15.
 */
public interface Convert {
    int getProgress();

    String getError();

    boolean isDone();

    InputStream inputStream() throws IOException;

    InputStream gif() throws IOException;
}
