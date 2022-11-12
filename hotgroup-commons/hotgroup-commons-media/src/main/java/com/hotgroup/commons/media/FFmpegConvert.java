package com.hotgroup.commons.media;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Lzw
 * @date 2022/5/15.
 */
public class FFmpegConvert {

    private static final String DEFAULT_FORMAT = "flv";

    private static final int maxTime = 600 * avutil.AV_TIME_BASE;
    private final FFmpegFrameGrabber grabber;
    private final OutputStream outputStream;
    private volatile int progress;
    private FFmpegFrameRecorder recorder;

    public FFmpegConvert(InputStream inputStream, OutputStream outputStream) {
        this.outputStream = outputStream;
        grabber = new FFmpegFrameGrabber(inputStream);
    }

    public int getProgress() {
        return progress;
    }


    public void handle() {
        try {
            grabber.start();
            long lengthInTime = grabber.getLengthInTime();
            if (maxTime < lengthInTime) {
                throw new IllegalArgumentException("视频长度限制");
            }
            recorder = new FFmpegFrameRecorder(outputStream, grabber.getImageWidth(), grabber.getImageHeight(),
                    grabber.getAudioChannels());

            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat(DEFAULT_FORMAT);
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoQuality(0);
            recorder.setAudioBitrate(grabber.getAudioBitrate());
            recorder.setSampleRate(grabber.getSampleRate());
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            /*
              权衡quality(视频质量)和encode speed(编码速度) values(值)：
              ultrafast(终极快),superfast(超级快), veryfast(非常快), faster(很快), fast(快),
              medium(中等), slow(慢), slower(很慢), veryslow(非常慢)
              ultrafast(终极快)提供最少的压缩（低编码器CPU）和最大的视频流大小；而veryslow(非常慢)提供最佳的压缩（高编码器CPU）的同时降低视频流的大小
             */
            recorder.setVideoOption("preset", "ultrafast");
            recorder.setVideoOption("crf", "18");


            recorder.start();
            while (true) {
                Frame frame = grabber.grab();
                if (frame == null) {
                    progress = 100;
                    break;
                }
                progress = (int) (frame.timestamp * 1d / lengthInTime * 100);
                recorder.record(frame);
            }
        } catch (FFmpegFrameRecorder.Exception | FrameGrabber.Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                if (Objects.nonNull(recorder)) {
                    recorder.close();
                }
            } catch (FrameRecorder.Exception ignored) {
            }
            try {
                grabber.close();
            } catch (FrameGrabber.Exception ignored) {
            }
        }
    }

}
