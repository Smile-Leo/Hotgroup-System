package com.hotgroup.commons.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lzw
 * @date 2022/4/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    private String object;

    private String path;

    private String url;


}
