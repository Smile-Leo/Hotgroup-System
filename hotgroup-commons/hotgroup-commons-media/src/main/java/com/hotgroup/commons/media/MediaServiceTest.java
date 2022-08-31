package com.hotgroup.commons.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

/**
 * @author Lzw
 * @date 2022/5/15.
 */
public class MediaServiceTest {


    public static void main(String[] args) throws Exception {
        commandTest();
    }

    static void cppTest(){
        try {
            Convert convert = new MediaServiceImpl().cppConvert(Files.newInputStream(Paths.get("d:/3.mp4")));
            do {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(convert.getProgress());
            } while (!convert.isDone());

            Files.copy(convert.inputStream(), Paths.get("D:/4.flv"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void commandTest() throws IOException, InterruptedException {
        Convert convert = new FFmpegHelper().convert(Files.newInputStream(Paths.get("d:/3.mp4")));
        do {
            System.out.println(convert.getProgress());
            TimeUnit.SECONDS.sleep(2);
        } while (!convert.isDone());

        Files.copy(convert.inputStream(), Paths.get("D:/9.flv"), StandardCopyOption.REPLACE_EXISTING);
    }
}
