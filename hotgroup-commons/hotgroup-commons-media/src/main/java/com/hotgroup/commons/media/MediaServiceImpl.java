package com.hotgroup.commons.media;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Lzw
 * @date 2022/5/14.
 */
@Component
public class MediaServiceImpl implements MediaService {


    @Override
    public Convert commandConvert(InputStream inputStream) throws IOException {
        return new FFmpegHelper().convert(inputStream);
    }

    @Override
    public Convert cppConvert(InputStream inputStream) throws IOException {
        Path outputFile = Files.createTempFile(UUID.randomUUID().toString(), ".flv");
        FFmpegConvert fFmpegConvert = new FFmpegConvert(inputStream, Files.newOutputStream(outputFile));
        CompletableFuture<String> handle = CompletableFuture.runAsync(fFmpegConvert::handle)
                .handle((unused, throwable) -> throwable.getCause().getMessage());

        return new Convert() {
            @Override
            public int getProgress() {
                return fFmpegConvert.getProgress();
            }

            @Override
            public String getError() {
                try {
                    return handle.get();
                } catch (InterruptedException | ExecutionException e) {
                    return e.getMessage();
                }
            }

            @Override
            public boolean isDone() {
                return handle.isDone();
            }

            @Override
            public InputStream inputStream() throws IOException {
                return Files.newInputStream(outputFile, StandardOpenOption.DELETE_ON_CLOSE);
            }

            @Override
            public InputStream gif() throws IOException {
                return null;
            }
        };
    }


}
