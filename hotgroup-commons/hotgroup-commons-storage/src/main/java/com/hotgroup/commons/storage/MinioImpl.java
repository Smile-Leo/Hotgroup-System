package com.hotgroup.commons.storage;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Lzw
 * @date 2022/4/17.
 */
@Slf4j
public class MinioImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final String defaultBucket;
    private final FileInfoFactory factory;

    public MinioImpl(MinioClient minioClient, String defaultBucket, FileInfoFactory factory) {
        this.minioClient = minioClient;
        this.defaultBucket = defaultBucket;
        this.factory = factory;
    }


    @Override
    public String add(String fileName, MediaTypeEnum fileType, InputStream inputStream) throws IOException {

        try {
            FileInfo fileInfo = factory.ofMedia(fileType);
            makeBucket(fileInfo);
            minioClient.putObject(PutObjectArgs.builder()
                    .object(fileInfo.getObject())
                    .contentType(fileType.getContetType())
                    .bucket(defaultBucket)
                    .stream(inputStream, -1, 1024 * 1024 * 10)
                    .build());

            return fileInfo.getUrl();

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }


    @Override
    public String add(String fileName, MediaTypeEnum fileType, MultipartFile file) throws IOException {
        try {
            FileInfo fileInfo = factory.ofMedia(fileType);
            makeBucket(fileInfo);
            minioClient.putObject(PutObjectArgs.builder()
                    .object(fileInfo.getObject())
                    .contentType(fileType.getContetType())
                    .bucket(defaultBucket)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());

            return fileInfo.getUrl();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }


    }

    private void makeBucket(FileInfo fileInfo) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(defaultBucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(defaultBucket).build());
        }
        minioClient.putObject(PutObjectArgs.builder()
                .object(fileInfo.getPath())
                .bucket(defaultBucket)
                .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                .build());
    }

    @Override
    public void remove(String url) throws IOException {

        try {
            FileInfo fileInfo = factory.ofId(url);
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(defaultBucket).object(fileInfo.getObject()).build());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

    }

    @Override
    public InputStream get(String url) throws IOException {


        try {
            FileInfo fileInfo = factory.ofId(url);
            return minioClient.getObject(
                    GetObjectArgs.builder().object(fileInfo.getObject()).bucket(defaultBucket).build());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }


    @Override
    public long getSize(String url) throws IOException {
        try {
            FileInfo fileInfo = factory.ofId(url);
            GetObjectResponse object = minioClient.getObject(
                    GetObjectArgs.builder().bucket(defaultBucket).object(fileInfo.getObject()).build());
            return Long.parseLong(Optional.ofNullable(object.headers().get("Content-Length")).orElse("0"));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        MinioClient client = MinioClient.builder()
                .endpoint("http://192.168.1.4:9000")
                .credentials("admin", "admin@1234")
                .build();
        FileInfoFactory store = new FileInfoFactory("store");
        MinioImpl minio = new MinioImpl(client, "store", store);


        InputStream inputStream = Files.newInputStream(Paths.get("D:\\gmp-mm-common.zip"));
        String test = minio.add("test", MediaTypeEnum.ZIP, inputStream);

        System.out.println(test.toString());
        InputStream inputStream1 = minio.get(test);
        System.out.println(inputStream1.toString());

    }
}
