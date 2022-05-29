package com.hotgroup.commons.storage;

import io.minio.*;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Lzw
 * @date 2022/4/17.
 */
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


    @Override
    public List<String> list() throws IOException {

        return list("/");
    }

    private List<String> list(String dir) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(defaultBucket).prefix(dir).build());
        Stream.Builder<String> builder = Stream.builder();
        Iterator<Result<Item>> iterator = results.iterator();

        try {
            while (iterator.hasNext()) {
                Result<Item> next = iterator.next();
                if (next.get().isDir()) {
                    List<String> list = list(next.get().objectName());
                    list.forEach(builder::add);
                    continue;
                }
                builder.add(next.get().objectName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder.build().collect(Collectors.toList());
    }
}
