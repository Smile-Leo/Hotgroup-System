package com.hotgroup.commons.media;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lzw
 * @date 2022/5/14.
 */
public interface MediaService {


    Convert commandConvert(InputStream inputStream) throws IOException;

    Convert cppConvert(InputStream inputStream) throws IOException;

}
