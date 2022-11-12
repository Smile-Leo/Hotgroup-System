package com.hotgroup.commons.media;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.SinglePassFFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Lzw
 * @date 2022/5/15.
 */
public class FFmpegHelper implements Convert {
    private static final int maxTime = 600;
    private static FFmpeg fFmpeg;
    private static FFprobe fFprobe;

    static {
        try {
            FFmpegHelper.fFmpeg = new FFmpeg();
            FFmpegHelper.fFprobe = new FFprobe();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Void> runAsync;
    private InputStream outStream;
    private InputStream gifStream;
    private double frameNumber;
    private long progressFrame;

    private String error;

    public Convert convert(InputStream inputStream) throws IOException {

        Path inputFile = Files.createTempFile(UUID.randomUUID().toString(), null);
        Files.copy(inputStream, inputFile, StandardCopyOption.REPLACE_EXISTING);

        Path outputFile = Files.createTempFile(UUID.randomUUID().toString(), ".flv");
        Path outGif = Files.createTempFile(UUID.randomUUID().toString(), ".gif");

        FFmpegProbeResult probe = fFprobe.probe(inputFile.toString());
        frameNumber = probe.getFormat().duration;
        if (frameNumber > maxTime) {
            error = "视频长度限制";
            runAsync = CompletableFuture.completedFuture(null);
            return this;
        }

        FFmpegBuilder gifbuild = new FFmpegBuilder()
                .setInput(inputFile.toString())
                .overrideOutputFiles(true)
                .setVerbosity(FFmpegBuilder.Verbosity.DEBUG)
                .addExtraArgs("-ss", "3")
                .addExtraArgs("-t", "3")
                .addOutput(outGif.toString())
                .addExtraArgs("-loop", "0")
                .addExtraArgs("-vf", "scale=iw/2:-1:flags=lanczos,fps=5,crop=iw/2:ih:0:0,split[s1][s2];[s1]palettegen[p];[s2][p]paletteuse")
                .done();
        CompletableFuture.runAsync(new SinglePassFFmpegJob(fFmpeg, gifbuild)).thenAccept((v) -> {
            try {
                gifStream = Files.newInputStream(
                        outputFile, StandardOpenOption.READ, StandardOpenOption.DELETE_ON_CLOSE);
            } catch (IOException ignored) {
            }
        });


        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.toString())
                .overrideOutputFiles(true)
                .setVerbosity(FFmpegBuilder.Verbosity.ERROR)
                .addOutput(outputFile.toString())
                .setVideoCodec("libx264")
                .setStrict(FFmpegBuilder.Strict.STRICT)
                .done();

        runAsync = CompletableFuture.runAsync(
                        new SinglePassFFmpegJob(fFmpeg, builder,
                                progress -> progressFrame = TimeUnit.NANOSECONDS.toSeconds(progress.out_time_ns)))
                .handle((v, err) -> {
                    try {
                        outStream = Files.newInputStream(outputFile, StandardOpenOption.READ,
                                StandardOpenOption.DELETE_ON_CLOSE);
                    } catch (IOException ignored) {
                    }
                    try {
                        Files.deleteIfExists(inputFile);
                    } catch (IOException ignored) {
                    }

                    this.error = err.getCause().getMessage();
                    return null;
                });

        return this;


    }

    @Override
    public int getProgress() {
        if (runAsync.isDone()) {
            return 100;
        }
        return (int) (progressFrame * 1d / frameNumber * 100);
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public boolean isDone() {
        return runAsync.isDone();
    }

    @Override
    public InputStream inputStream() {
        return outStream;
    }

    @Override
    public InputStream gif() throws IOException {
        return gifStream;
    }


}
