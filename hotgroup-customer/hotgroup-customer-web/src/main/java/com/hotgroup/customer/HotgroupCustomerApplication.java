package com.hotgroup.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动程序
 *
 * @author Lzw
 */
@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class
})
@Slf4j
@ComponentScan(basePackages = "com.hotgroup")
public class HotgroupCustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotgroupCustomerApplication.class, args);
    }
}
