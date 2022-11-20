package com.hotgroup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动程序
 *
 * @author Lzw
 */
@SpringBootApplication
@Slf4j
public class HotgroupManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotgroupManageApplication.class, args);
    }
}
