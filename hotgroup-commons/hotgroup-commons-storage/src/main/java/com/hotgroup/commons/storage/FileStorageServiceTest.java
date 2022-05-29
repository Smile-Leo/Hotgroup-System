package com.hotgroup.commons.storage;

import io.minio.MinioClient;

import java.io.IOException;
import java.util.List;

/**
 * @author Lzw
 * @date 2022/5/15.
 */
public class FileStorageServiceTest {

    public static void main(String[] args) throws IOException {
        MinioClient client = MinioClient.builder()
                .endpoint("http://114.132.232.4:9000")
                .credentials("admin", "admin@1234")
                .build();
        FileInfoFactory store = new FileInfoFactory("store");
        MinioImpl minio = new MinioImpl(client, "store", store);


      /*  InputStream inputStream = Files.newInputStream(Paths.get("D:\\1.mp4"));
        String test = minio.add("test", MediaTypeEnum.MP4, inputStream);

        System.out.println(test.toString());
        InputStream inputStream1 = minio.get(test);
        System.out.println(inputStream1.toString());*/

        List<String> list = minio.list();
        System.out.println(list);

    }
}
