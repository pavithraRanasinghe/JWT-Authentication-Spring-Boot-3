package com.robot.cairo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(scanBasePackages = "com.robot")
@ComponentScan(basePackages = "com.robot")
public class CairoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CairoApplication.class, args);
    }

}
